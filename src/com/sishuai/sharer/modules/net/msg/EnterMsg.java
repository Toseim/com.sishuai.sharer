package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.util.Logging;

/**
 * 用户登入的消息
 * @author 四帅
 *
 */
public class EnterMsg implements Msg {
	private static int msgType = MSG_ENTER;
	private String IP;
	private String nickName;
	
	public EnterMsg(String IP, String nickName) {
		this.IP = IP;
		this.nickName = nickName;
	}
	public EnterMsg() {
	}
	
	@Override
	public void send(Object group, int port) {
		// TODO Auto-generated method stub
		Logging.getLogger().setFileName("EnterMsg");
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(EnterMsg.msgType);
			Logging.info("Construct the login information.");
			dos.writeUTF(IP);
			dos.writeUTF(nickName);
			Logging.info("Write the IP :"+IP+"\tNickName:"+nickName);
			dos.flush();

			buf = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, (InetAddress)group, port);
			Logging.info("Sending login packet");
			NetworkMgr.getMgr().getMulticastSocket().send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("Write error, setting up the  package failed ");
		} finally {
			if (dos != null)
				try {
					dos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		Logging.getLogger().setFileName("EnterMsg");
		try {
			String uIP = dis.readUTF();
			String uNickName = dis.readUTF();
			Logging.info("Receives a user login information\nIP Address: "+uIP+"\nNickName: "+uNickName);
			if (!ClientInfo.getIPList().contains(uIP)) {
				ClientInfo.getIPList().add(uIP);
				ClientInfo.getClients().add(new ClientInfo(uIP, uNickName));
				Logging.info("Add users into groups of users");
				ContentManager.getMgr().updateItems();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logging.fatal("Unwrapping failed... Discarded packet..");
		}
	}
}
