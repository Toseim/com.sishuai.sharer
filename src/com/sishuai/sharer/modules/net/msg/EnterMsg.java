package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.eclipse.swt.widgets.Display;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.interfaces.Msg;

/**
 * 用户登入的消息
 * @author 四帅
 *
 */
public class EnterMsg implements Msg {
	private static int msgType = Msg.MSG_ENTER;
	private String IP;
	private String nickName;
	
	public EnterMsg(String IP, String nickName) {
		this.IP = IP;
		this.nickName = nickName;
	}
	public EnterMsg() {
	}
	
	@Override
	public void send(DatagramSocket ds, Object group, int port) {
		// TODO Auto-generated method stub
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try {
			dos.writeInt(EnterMsg.msgType);
			dos.writeUTF(IP);
			dos.writeUTF(nickName);
			dos.flush();

			buf = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, (InetAddress)group, port);
			
			((MulticastSocket)ds).send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		try {
			String uIP = dis.readUTF();
			String nickName = dis.readUTF();
			if (!ClientInfo.getIPList().contains(uIP)) {
				ClientInfo.getIPList().add(uIP);
				ClientInfo.getClients().add(new ClientInfo(uIP, nickName));
				ContentManager.getManager().updateItems();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
