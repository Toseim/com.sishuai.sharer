package com.sishuai.sharer.modules.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.regex.Pattern;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.net.msg.LinkMsg;
import com.sishuai.sharer.util.Logging;

/**
 * 网络管理的类，应尽量把网络的相关东西搬到这里，方便管理
 * @author 四帅
 *
 */
public class NetworkMgr {
	public static Pattern pattern1 = Pattern.compile("^192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3}");
	public static Pattern pattern2 = Pattern.compile("^10\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	public static Pattern pattern3 = Pattern.compile("^172\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	
	private static NetworkMgr networkMgr;
	private Random random = new Random();
	private ServerSocket serverSocket;
	private int TCPport = 0;
	private final int UDPport = 37384;   //默认的端口
	private MulticastServer ms;
	private DatagramSocket datagramSocket;
	private String name;
	private String IP;
	private static boolean state = false;   
	//返回用户是否处于尝试连接tcp的阶段，用来阻止其他tcp连接
	
	public static NetworkMgr getMgr() {
		if (networkMgr == null)
			networkMgr = new NetworkMgr();
		return networkMgr;
	}
	
	public String getIP() {
		if (IP == null) {
			try {
				//获得本机所有IP
				InetAddress[] addresses = InetAddress.getAllByName(InetAddress.
						getLocalHost().getHostName());
				for (int i=0; i< addresses.length; i++) {
					String s = addresses[i].getHostAddress();
					//正则匹配
					if (NetworkMgr.pattern1.matcher(s).find() || NetworkMgr.pattern2.matcher(s).find() 
							|| NetworkMgr.pattern3.matcher(s).find()) {
						IP = s;
						break;
					}
				}
				ClientInfo.getIPList().add(IP);
				Logging.info("");
				return IP;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//MessageDialog.openWarning(new Shell(Display.getDefault()), "State", "你现在不处于局域网中");
			return null;
		}
		return IP;
	}
	
	public static void setState(boolean s) {
		state = s;
	}
	
	public static boolean getState() {
		return state;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ServerSocket getServersocket() {
		Logging.getLogger().setFileName("NetWorkMgr");
		if (serverSocket == null || serverSocket.isClosed()) {
			while (true) {
				try {
					serverSocket = new ServerSocket(getTCPport());
				} catch (IOException e) {
					Logging.fatal(getTCPport()+"端口不可用，尝试其他端口");
					continue;
				}
				Logging.info("开启一个serversocket服务，端口在"+getTCPport());
				break;
			}
		}
		return serverSocket;
	}
	public int getTCPport() {
		if (TCPport == 0) 
			TCPport = random.nextInt(55535) +10000;
		return TCPport;
	}
	
	public DatagramSocket getDatagramSocket() {
		if (datagramSocket == null || datagramSocket.isClosed()) {
			while (true) {
				try {
					datagramSocket = new DatagramSocket(UDPport);
					new Thread(new RecvThread(datagramSocket, false)).start();
				} catch (Exception e) {
					Logging.fatal("");
				}
				Logging.getLogger().setFileName("NetWorkMgr");
				Logging.info("开启一个DatagramSocket服务，端口在"+UDPport);
				break;
			}
		}
		return datagramSocket;
	}

	public MulticastServer getMulticastServer() {
		if (ms == null)
			ms = new MulticastServer();
		return ms;
	}
	
	public void createTempSend(DatagramPacket dp) {
		Logging.getLogger().setFileName("NetWorkMgr");
		while (true) {
			try {
				DatagramSocket ds = new DatagramSocket(random.nextInt(55535)+10000);
				Logging.info("打开一个UDP端口，发送数据包");
				ds.send(dp);
				Logging.info("端口关闭中..");
				ds.close();
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logging.fatal("发送数据失败，正在重新发送");
				continue;
			}
		}
	}
	
	public int getUDPport() {
		return UDPport;
	}
	
	public void attempLink(String objectIP) {
		//发送尝试连接的信息
		LinkMsg linkMsg = new LinkMsg(objectIP, getIP(), TCPport);
		//开放端口来发送文件
		DatagramSocket ds = NetworkMgr.getMgr().getDatagramSocket();
		
		linkMsg.send(ds, getName(), 0);
	}
	
	public void disconnect(ClientInfo clientInfo) {
		if (clientInfo == null) return;
		try {
			Logging.warning("正在从用户组中删除"+clientInfo.getName());
			clientInfo.setConnected(false);
			if (clientInfo.getDataInputStream() != null)
				clientInfo.getDataInputStream().close();
			if (clientInfo.getDataOutputStream() != null)
				clientInfo.getDataOutputStream().close();
			if (clientInfo.getSocket() != null)
				clientInfo.getSocket().close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
