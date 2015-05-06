package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.util.Logging;

/**
 * 用户离开的消息
 * @author 四帅
 *
 */
public class ExitMsg implements Msg {
	private static int msgType = Msg.MSG_EXIT;
	private String IP;
	
	public ExitMsg(String IP) {
		this.IP = IP;
	}
	public ExitMsg() {
	}
	
	@Override
	public void send(DatagramSocket ds, Object group, int port) {
		// TODO Auto-generated method stub
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			Logging.getLogger().setFileName("ExitMsg");
			Logging.info("构建用户离开信息包");
			Logging.info("写入头信息");
			dos.writeInt(ExitMsg.msgType);
			Logging.info("写入IP");
			dos.writeUTF(IP);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buf = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buf, buf.length, (InetAddress)group, port);
		try {
			Logging.info("发送用户离开信息");
			((MulticastSocket)ds).send(dp);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("网络连接错误");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("写入数据包失败");
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		Logging.getLogger().setFileName("ExitMsg");
		try {
			String uIP = dis.readUTF();
			Logging.info("移除"+uIP+"地址的用户信息");
			if (ClientInfo.getIPList().contains(uIP)) {
				ClientInfo.getIPList().remove(uIP);
				int len = ClientInfo.getClients().size();
				for (int i = 0; i<len; i++) {
					ClientInfo clientInfo = ClientInfo.getClients().get(i);
					if (clientInfo.getIp().equals(uIP)) {
						ClientInfo.getClients().remove(clientInfo);
						ContentManager.getManager().updateItems();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("移除过程中发生错误");
		}
	}

}
