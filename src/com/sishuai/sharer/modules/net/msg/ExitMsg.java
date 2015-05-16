package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.util.Logging;

/**
 * 用户离开的消息
 * @author 四帅
 *
 */
public class ExitMsg implements Msg {
	private static int msgType = MSG_EXIT;
	private String IP;
	
	public ExitMsg(String IP) {
		this.IP = IP;
	}
	public ExitMsg() {
	}
	
	@Override
	public void send(Object group, int port) {
		// TODO Auto-generated method stub
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			Logging.getLogger().setFileName("ExitMsg");
			Logging.info("Setting up of user leave packets");
			Logging.info("Writing the head information");
			dos.writeInt(ExitMsg.msgType);
			Logging.info("Writing the IP");
			dos.writeUTF(IP);
			dos.flush();
			buf = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, (InetAddress)group, port);
			Logging.info("Sending user information(leave)");
			NetworkMgr.getMgr().getMulticastSocket().send(dp);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("Network connection error");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("Writint data packet failed");
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
		Logging.getLogger().setFileName("ExitMsg");
		try {
			String uIP = dis.readUTF();
			Logging.info("Removing "+uIP+"'s information of address .");
			if (ClientInfo.getIPList().contains(uIP)) {
				ClientInfo.getIPList().remove(uIP);
				int len = ClientInfo.getClients().size();
				for (int i = 0; i<len; i++) {
					ClientInfo clientInfo = ClientInfo.getClients().get(i);
					if (clientInfo.getIp().equals(uIP)) {
						ClientInfo.getClients().remove(clientInfo);
						ContentManager.getMgr().updateItems();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("Error occurred in the process of removing");
		}
	}

}
