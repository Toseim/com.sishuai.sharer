package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.util.Logging;

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
		Logging.getLogger().setFileName("EnterMsg");
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(EnterMsg.msgType);
			Logging.info("构建登录信息包..");
			dos.writeUTF(IP);
			dos.writeUTF(nickName);
			Logging.info("写入IP:"+IP+"\tNickName:"+nickName);
			dos.flush();

			buf = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, (InetAddress)group, port);
			Logging.info("发送登录包");
			((MulticastSocket)ds).send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("写入错误，构建包失败");
		}
		
	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		Logging.getLogger().setFileName("EnterMsg");
		try {
			String uIP = dis.readUTF();
			String uNickName = dis.readUTF();
			Logging.info("接收到一个用户登录信息\nIP Address: "+uIP+"\nNickName: "+uNickName);
			if (!ClientInfo.getIPList().contains(uIP)) {
				ClientInfo.getIPList().add(uIP);
				ClientInfo.getClients().add(new ClientInfo(uIP, uNickName));
				Logging.info("添加用户入用户组");
				ContentManager.getManager().updateItems();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("解包失败...丢弃包..");
		}
	}
}
