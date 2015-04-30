package com.sishuai.sharer.modules.net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.modules.net.msg.EnterMsg;
import com.sishuai.sharer.modules.net.msg.ExitMsg;
import com.sishuai.sharer.modules.net.msg.LinkMsg;

public class MulticastServer {
	
	private InetAddress group;
	public static final int port = 8647;
	private String IP;
	private EnterMsg enterMsg;
	private MulticastSocket multicastSocket;
	
	public void sendMyPacket() {
		enterMsg.send(multicastSocket, group, port);
	}
	
	public String getIP() {
		if (IP != null) return IP;
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
			return IP;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void disConnect() {
		try {
			//发出离开消息
			if (multicastSocket != null)
				new ExitMsg(IP).send(multicastSocket, group, port);
			multicastSocket.leaveGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
		}
		multicastSocket.close();
	}
	
	public void run() {
		//获得ip
		IP = null;
		if ((IP = getIP()) == null) {
			System.out.println("不在局域网内");
			return;   
		}
		ClientInfo.getIPList().add(IP);

		//初始化
		try {
			group = InetAddress.getByName("224.0.0.2");
			multicastSocket = new MulticastSocket(port);
			multicastSocket.joinGroup(group);

			//发送自己的数据包
			enterMsg = new EnterMsg(IP, "hahaha");
			enterMsg.send(multicastSocket, group, port);
			
			//接收线程启动
			new Thread(new ClientThread()).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			disConnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			disConnect();
		}
	}
	
	
	class ClientThread implements Runnable {
		byte[] buf = new byte[1024];
		public void run() {
			DatagramPacket dp;
			while (true) {
				try {
					dp = new DatagramPacket(buf, buf.length);
					multicastSocket.receive(dp);
					parse(dp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		public void parse(DatagramPacket dp) {
			ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
			DataInputStream dis = new DataInputStream(bais);
			try {
				int msgType = dis.readInt();
				switch (msgType) {
				case Msg.MSG_ENTER:
					new EnterMsg().parse(dis);
					break;
				case Msg.MSG_EXIT:
					new ExitMsg().parse(dis);
					break;
				case Msg.MSG_LINK:
					new LinkMsg().parse(dis);
					break;
				default:
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}