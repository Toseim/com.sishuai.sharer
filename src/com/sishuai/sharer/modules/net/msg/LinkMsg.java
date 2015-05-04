package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.modules.net.NetworkMgr;

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
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
System.out.println("send a packet with {" + localIP + "\t" + localPort + "\t" + 
		name + "\tto " + objectIP + ":" + NetworkMgr.getMgr().getUDPport());
			dos.writeInt(msgType);
			dos.writeUTF(localIP);
			dos.writeInt(localPort);
			dos.writeUTF((String)name);
			dos.flush();
System.out.println("helloworld");
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
		ClientInfo clientInfo = null;
		try {
			String remoteIP = dis.readUTF();
			int remotePort = dis.readInt();
			String name = dis.readUTF();
System.out.println("receive a packet\t" + remoteIP + "\t" + remotePort + "\t" + name);
			//users can choose whether to accept the link request
			MessageBox messageBox = new MessageBox(new Shell(Display.getDefault()), 
					SWT.OK | SWT.CANCEL);
			//this line may has something wrong   [new Shell()] I can't differ
			messageBox.setMessage(name + " is trying to connect you, what is your opinion");
			messageBox.setText("Linking");
			if (messageBox.open() == SWT.CANCEL) return;

			boolean exist = ClientInfo.getIPList().contains(remoteIP);
			//创建新对象
			clientInfo = findClient(remoteIP, name);
			
			//连接。。。
			Socket socket = new Socket(remoteIP, remotePort);
System.out.println("succeed to connect " + name);
			clientInfo.setConnected(true);
			
			//refresh
			ContentManager.getManager().updateItems();
			
			//架设管道
			clientInfo.setSocket(socket);
			
			//传送发信端名字
			if (!exist) {
				clientInfo.getDataOutputStream().writeUTF(NetworkMgr.getMgr().getName());
				clientInfo.getDataOutputStream().flush();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			NetworkMgr.getMgr().disconnect(clientInfo);
		}
	}
}
