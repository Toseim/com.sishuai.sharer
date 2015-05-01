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

import org.eclipse.swt.widgets.Display;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.interfaces.Msg;

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
			dos.writeInt(ExitMsg.msgType);
			dos.writeUTF(IP);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		buf = baos.toByteArray();
		DatagramPacket dp = new DatagramPacket(buf, buf.length, (InetAddress)group, port);
		try {
			((MulticastSocket)ds).send(dp);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			if (ClientInfo.getIPList().contains(uIP)) {
				ClientInfo.getIPList().remove(uIP);
				int len = ClientInfo.getClients().size();
				for (int i = 0; i<len; i++) {
					ClientInfo clientInfo = ClientInfo.getClients().get(i);
					if (clientInfo.getIp().equals(uIP))
						new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										ContentManager.getManager().removeItem(clientInfo, null);
										return;
									}
								});
							}
						}).start();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
