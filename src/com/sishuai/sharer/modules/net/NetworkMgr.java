package com.sishuai.sharer.modules.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.net.msg.LinkMsg;
import com.sishuai.sharer.util.Logging;
import com.sishuai.sharer.util.Utils;
import com.sishuai.sharer.views.ClientView;

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
	private ServerSocket tempSocket;
	private ServerSocket mainSocket;
	private MulticastSocket multicastSocket;
	private DatagramSocket datagramSocket;
	private Socket socket;

	private int tempPort = 0;
	private int TCPport = 0;
	private static final int multiPort = 8647;
	private final int UDPport = 37384;   //默认的端口
	
	private ClientView view;
	
	private String name;
	private String IP;
	
	private static boolean isTimeOut = false;
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
				Logging.getLogger().setFileName("NetworkMgr");
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
				Logging.info("Access the localhost IP in LAN "+IP);
				return IP;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		return IP;
	}
	
	public void setView(ClientView view) {
		this.view = view;
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
		Logging.getLogger().setFileName("NetworkMgr");
		if (mainSocket == null) {
			while (true) {
				try {
					mainSocket = new ServerSocket(getTCPport());
				} catch (IOException e) {
					Logging.fatal(getTCPport()+"The port is not available, try other ports");
					continue;
				}
				Logging.info("Open a ServerSocket service, the port is in the "+getTCPport());
				break;
			}
		}
		return mainSocket;
	}
	
	public int getTCPport() {
		if (TCPport == 0) {
		TCPport = Utils.getRandom().nextInt(55535) +10000;	
		}

		return TCPport;
	}
	
	public ServerSocket getTempSocket() {
		Logging.getLogger().setFileName("NetworkMgr");
		if (tempSocket == null || tempSocket.isClosed()) {
			while (true) {
				try {
					tempSocket = new ServerSocket(getTempPort());
					Logging.info("New a temporary serversocket");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Logging.fatal("Open failed, try again ");
					continue;
				}
				break;
			}
		}
		return tempSocket;
	}
	
	public int getTempPort() {
		if (tempSocket == null || tempSocket.isClosed()) {
			tempPort = Utils.getRandom().nextInt(55535)+10000;
		}
		return tempPort;
	}
	
	public DatagramSocket getDatagramSocket() {
		if (datagramSocket == null || datagramSocket.isClosed()) {
			while (true) {
				try {
					datagramSocket = new DatagramSocket(UDPport);
					Logging.getLogger().setFileName("NetworkMgr");
					Logging.info("Open a DatagramSocket service, the port is in the "+UDPport);
					new Thread(new RecvThread(datagramSocket, false)).start();
				} catch (Exception e) {
					Logging.fatal("An error occurred on the UDP service..");
					continue;
				}
				break;
			}
		}
		return datagramSocket;
	}

	public MulticastSocket getMulticastSocket() {
		if (multicastSocket == null) {
			try {
				multicastSocket = new MulticastSocket(multiPort);
				multicastSocket.joinGroup(InetAddress.getByName("224.2.2.2"));
				MulticastServer.getMulticastServer().setConfig(
						multiPort, InetAddress.getByName("224.2.2.2") );
			} catch (IOException e) {
				com.sishuai.sharer.util.Logging.fatal("An error occurred . Please check your configure such the net . ");
				e.printStackTrace();
			}
		}
		return multicastSocket;
	}
	
	public void createTempSend(DatagramPacket dp) {
		Logging.getLogger().setFileName("NetworkMgr");
		while (true) {
			try {
				DatagramSocket ds = new DatagramSocket(Utils.getRandom().nextInt(55535)+10000);
				Logging.info("Open a UDP port, send datagrampacket");
				ds.send(dp);
				Logging.info("Closing the port.");
				ds.close();
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Logging.fatal("Failed sending data, try again ");
			}
		}
	}
	
	public int getUDPport() {
		return UDPport;
	}
	
//	public void disconnect(ClientInfo clientInfo) {
//		if (clientInfo == null) return;
//		try {
//			Logging.getLogger().setFileName("NetworkMgr");
//			Logging.warning("正在从用户组中删除"+clientInfo.getName());
//			clientInfo.setConnected(false);
//			if (clientInfo.getDataInputStream() != null) 
//				clientInfo.getDataInputStream().close();
//			if (clientInfo.getDataOutputStream() != null)
//				clientInfo.getDataOutputStream().close();
//			if (clientInfo.getSocket() != null)
//				clientInfo.getSocket().close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void attempLink(String objectIP) {
		setState(true);
		//防止servercokset为空
		getServersocket();
		getTempSocket();
		
		LinkMsg linkMsg = new LinkMsg(objectIP, getIP(), tempPort);
		linkMsg.send(getName(), UDPport);
		view.showMessage("Waiting the respond of { "+ objectIP +" }");
		
		new Thread(new ConnectionThread(objectIP)).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(45000);
					if (socket == null && !tempSocket.isClosed()) {
						Logging.fatal("TimeOut");
						tempSocket.close();
						isTimeOut = true;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static ClientInfo findClient(String IP, String name) {
		ClientInfo clientInfo = null;
		if (ClientInfo.getIPList().contains(IP)) {
			
			//使用已有显示的账户连接
			for (int i = 0; i < ClientInfo.getClients().size(); i++) {
				ClientInfo c = ClientInfo.getClients().get(i);
				if (c.getIp().equals(IP))
						return c;
			}
		}
		//添加新的账户并连接
		clientInfo = new ClientInfo(IP, name);
		ClientInfo.getClients().add(clientInfo);
		Logging.info("Add the user's information to the group of users");
		ClientInfo.getIPList().add(IP);
		Logging.info("Add the IP to the known IP list");
		return clientInfo;
	}
	class ConnectionThread implements Runnable {
		private String objectIP;
		public ConnectionThread(String objectIP) {
			this.objectIP = objectIP;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Logging.getLogger().setFileName("NetworkMgr");
			DataInputStream dis = null;
			DataOutputStream dos = null;
			String remoteName = null;
			
			try {
				Logging.info("The temporary TCP server try to accept connections");
				socket = tempSocket.accept();
				Logging.info("Success is to accept each other connection, setting up in the pipeline");
				
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				
				if (!ClientInfo.getIPList().contains(objectIP)) {
					remoteName = dis.readUTF();
					Logging.info("Accept the remoteName "+ remoteName);
				}
				dos.writeInt(TCPport);
				dos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				view.showMessage("");
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						setState(false);
						if (!isTimeOut) return;
						MessageDialog.openError(view.getSite().getShell(), "TIME OUT!", "Timeout, the other did not respond");
					}
				});
				return;
			} finally {
				try {
					if (dos != null) dos.close();
					if (dis != null) dis.close();
					if (tempSocket != null) tempSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					com.sishuai.sharer.util.Logging.fatal("Failed to close ");
					e.printStackTrace();
				}
			}
				
			try {
				ClientInfo clientInfo = findClient(objectIP, remoteName);
				clientInfo.setSocket(mainSocket.accept());
				Logging.info("The main port has connected and set up the pipeline");
				
				clientInfo.setConnected(true);
				//refresh
				ContentManager.getMgr().updateItems();
				//消息通知
				view.showMessage("we connect!");
			} catch (IOException e) {
				com.sishuai.sharer.util.Logging.fatal("IOException . ");
				e.printStackTrace();
			}
			setState(false);
		}
	}
	
	public static void dispose() {
		if (networkMgr.multicastSocket != null) {
			networkMgr.multicastSocket.close();
		}
		for (int i = 0; i < ClientInfo.getClients().size(); i++) {
			ClientInfo clientInfo = ClientInfo.getClients().get(i);
			clientInfo.disconnect();
		}
		
		if (networkMgr.datagramSocket != null) {
			networkMgr.datagramSocket.close();
		}
		try {
			if (networkMgr.tempSocket != null) {
				networkMgr.tempSocket.close();
			}
			if (networkMgr.socket != null) {
				networkMgr.socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logging.fatal("IOException . ");
			e.printStackTrace();
		}
	}
}
