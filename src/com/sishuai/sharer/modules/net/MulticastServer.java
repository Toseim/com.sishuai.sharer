package com.sishuai.sharer.modules.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.eclipse.jface.action.Action;

import com.sishuai.sharer.modules.net.msg.EnterMsg;
import com.sishuai.sharer.modules.net.msg.ExitMsg;
/**
 * 与局域网广播相关的网络处理
 * @author 四帅
 *
 */
public class MulticastServer extends Action{
	
	private static MulticastServer multicastServer;
	private int port;
	private InetAddress group;
	private String IP;
	private EnterMsg enterMsg;
	private MulticastSocket multicastSocket;
	private static boolean state = false;
	
	public MulticastServer() {
		super("Open the multicast");
		this.IP = NetworkMgr.getMgr().getIP();
	}
	
	public static MulticastServer getMulticastServer() {
		if (multicastServer == null)
			multicastServer = new MulticastServer();
		return multicastServer;
	}
	
	public void setConfig(int port, InetAddress group) {
		this.port = port;
		this.group = group;
	}
	
	public void sendMyPacket() {
		enterMsg.send(group, port);
	}
	
	public void disConnect() {
		try {
			if (multicastSocket != null)
				new ExitMsg(IP).send(group, port);
			multicastSocket.leaveGroup(group);
		} catch (IOException e) {
			e.printStackTrace();
		}
		multicastSocket.close();
	}
	
	public void run() {
		state = !state;
		if (!state) {
			setText("open multicast");
			if (multicastSocket != null) {
				multicastSocket.close();
				multicastSocket = null;
			}
			return;
		}
		setText("close multicast");
			
		multicastSocket = NetworkMgr.getMgr().getMulticastSocket();
		
		//发送自己的数据包
		enterMsg = new EnterMsg(IP, NetworkMgr.getMgr().getName());
		enterMsg.send(group, port);
		//接收线程启动
		new Thread(new RecvThread(multicastSocket, true)).start();
	}
}