package com.sishuai.sharer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.util.Date;

import com.sishuai.sharer.modules.interfaces.Msg;


public class MulticastSender {

	public static void server() throws Exception {
		InetAddress group = InetAddress.getByName("224.0.0.2");// 组播地址
		int port = 8647;
		MulticastSocket mss = null;
		
		try {
			mss = new MulticastSocket(port);
			mss.joinGroup(group);
			System.out.println("发送数据包启动！（启动时间" + new Date() + ")");

			while (true) {
//				byte[] buf = new byte[1024];
//				DatagramPacket dp = new DatagramPacket(buf, buf.length);
//				mss.receive(dp);
//				System.out.println("receive a packet");
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);
				dos.writeInt(Msg.MSG_ENTER);
				dos.writeUTF("192.168.31.132");
				dos.writeUTF("wo shi shui");
				dos.flush();
				byte[] buffer = baos.toByteArray();
				DatagramPacket dp = new DatagramPacket(buffer, buffer.length, group, port);
				mss.send(dp);
				System.out.println("发送数据包给 " + group + ":" + port);
				
				while (true) {
					mss.receive(dp);
					System.out.println("get a packet");
					ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, buffer.length);
					DataInputStream dis = new DataInputStream(bais);
					if (Msg.MSG_LINK == dis.readInt()) {
						break;
					}
				}
				Socket socket = new Socket("192.168.31.193", 18888);
				
				
				Thread.sleep(10000);
				
				baos = new ByteArrayOutputStream();
				dos = new DataOutputStream(baos);
				dos.writeInt(Msg.MSG_EXIT);
				dos.writeUTF("192.168.31.192");
				dos.flush();
				buffer = baos.toByteArray();
				dp = new DatagramPacket(buffer, buffer.length, group, port);
				mss.send(dp);
				System.out.println("exit msg send!");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (mss != null) {
					mss.leaveGroup(group);
					mss.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public static void main(String[] args) throws Exception {
		server();
	}
}
