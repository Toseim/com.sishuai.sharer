package com.sishuai.sharer.modules.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import org.eclipse.jface.action.Action;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.net.msg.EnterMsg;
import com.sishuai.sharer.modules.net.msg.ExitMsg;
/**
 * 与局域网广播相关的网络处理
 * @author 四帅
 *
 */
public class MulticastServer extends Action{
	
	private InetAddress group;
	public static final int port = 8647;
	private String IP = null;
	private EnterMsg enterMsg;
	private MulticastSocket multicastSocket;
	private static boolean state = false;
	
	public MulticastServer() {
		super("打开组播");
	}
	
	public void sendMyPacket() {
		enterMsg.send(multicastSocket, group, port);
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
		if (!state) {
			setText("open multicast");
			if (multicastSocket != null) {
				multicastSocket.close();
				multicastSocket = null;
			}
			return;
		}
		setText("close multicast");

		//初始化
		try {
			group = InetAddress.getByName("224.2.2.2");
			multicastSocket = new MulticastSocket(port);
			multicastSocket.joinGroup(group);

			//发送自己的数据包
			enterMsg = new EnterMsg(IP, NetworkMgr.getMgr().getName());
			enterMsg.send(multicastSocket, group, port);
			
			//接收线程启动
			new Thread(new RecvThread(multicastSocket, true)).start();
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
}