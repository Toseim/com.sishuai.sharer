package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.util.Logging;

/**
 * 用户单独发送给某个其他用户的建立tcp链接的消息
 * @author 四帅
 *
 */
public class LinkMsg implements Msg {
	private static int msgType = Msg.MSG_LINK;
	private String objectIP;
	private String localIP;
	private int localPort;
	
	public LinkMsg() {
	}
	public LinkMsg(String objectIP, String localIP, int localPort) {
		this.objectIP = objectIP;
		this.localIP = localIP;
		this.localPort = localPort;
	}

	@Override
	public void send(DatagramSocket ds, Object name, int port) {
		// TODO Auto-generated method stub
		Logging.getLogger().setFileName("LinkMsg");
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			Logging.info("构建一个建立连接信息");
			Logging.info("写入头信息");
			dos.writeInt(msgType);
			Logging.info("写入本地IP");
			dos.writeUTF(localIP);
			Logging.info("写入当前Tcp开放端口");
			dos.writeInt(localPort);
			Logging.info("写入当前用户名");
			dos.writeUTF((String)name);
			dos.flush();
			byte[] buf = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, 
					new InetSocketAddress(objectIP, NetworkMgr.getMgr().getUDPport()));
			
			NetworkMgr.getMgr().createTempSend(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ClientInfo findClient(String IP, String name) {
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
		ClientInfo.getIPList().add(IP);
		return clientInfo;
	}
	
	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		//users can choose whether to accept the link request
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						ClientInfo clientInfo = null;
						try {
							Logging.getLogger().setFileName("LinkMsg");
							String remoteIP = dis.readUTF();
							int remotePort = dis.readInt();
							String name = dis.readUTF();
							Logging.info("读取到用户的连接信息: \nIP Address: "+ 
							remoteIP + "\nPort: " + remotePort + "\n Name:" + name);
							Logging.info("打开MessageBox等待用户选择操作");
							MessageBox messageBox = new MessageBox(new Shell(Display.getDefault()), 
									SWT.OK | SWT.CANCEL);
							//this line may has something wrong   [new Shell()] I can't differ
							messageBox.setMessage(name + " is trying to connect you, what is your opinion");
							messageBox.setText("Linking");
							if (messageBox.open() == SWT.CANCEL) {
								Logging.info("选择拒绝连接= =");
								return;
							}
							Logging.info("接受了来自对方的连接");
							boolean exist = ClientInfo.getIPList().contains(remoteIP);
							//创建新对象
							clientInfo = findClient(remoteIP, name);
							Logging.info("使用连接消息包的IP和端口连接。。");
							//连接。。。
							Socket socket = new Socket(remoteIP, remotePort);
							Logging.info("成功连接到对方的socket");
							
							Logging.info("添加用户到用户组，配置用户的基本信息");
							clientInfo.setConnected(true);
							
							Logging.info("架设与对方用户的连接管道");
							clientInfo.setSocket(socket);
							
							//传送发信端名字
							if (!exist) {
								Logging.info("向对方发送本地用户设置的姓名");
								clientInfo.getDataOutputStream().writeUTF(NetworkMgr.getMgr().getName());
								clientInfo.getDataOutputStream().flush();
							}
							//refresh
						} catch (SocketException e) {
							Logging.fatal("连接失败，对方关闭了连接");
							NetworkMgr.getMgr().disconnect(clientInfo);
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Logging.fatal("主机不存在，数据信息已经无效");
							NetworkMgr.getMgr().disconnect(clientInfo);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Logging.fatal("传输本地用户名失败，连接已经关闭");
							NetworkMgr.getMgr().disconnect(clientInfo);
						}
						ContentManager.getMgr().updateItems();
					}
				});
			}
		}).start();
	}
}
