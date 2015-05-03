package com.sishuai.sharer.modules.net;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;

import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.modules.net.msg.EnterMsg;
import com.sishuai.sharer.modules.net.msg.ExitMsg;
import com.sishuai.sharer.modules.net.msg.LinkMsg;

public class RecvThread implements Runnable {
	private boolean isMulti;
	private MulticastSocket multicastSocket;
	private DatagramSocket datagramSocket;
	private byte[] buf = new byte[1024];
	
	public RecvThread(DatagramSocket ds, boolean isMulti) {
		// TODO Auto-generated constructor stub
		this.isMulti = isMulti;
		this.datagramSocket = ds;
	}
	
	public RecvThread(MulticastSocket ms, boolean isMulti) {
		// TODO Auto-generated constructor stub
		this.isMulti = isMulti;
		this.multicastSocket = ms;
	}
	@Override
	public void run() {
		while (true) {
			// TODO Auto-generated method stub
			try {
System.out.println("running a recvthread");
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				if (isMulti && multicastSocket != null) {
					multicastSocket.receive(dp);
System.out.println("receive a multipacket");
				} else if (datagramSocket != null) {
					datagramSocket.receive(dp);
System.out.println("receive a singlepacket");
				}
				parse(dp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
System.out.println("Server is closed");
			}
		}
	}
	public void parse(DatagramPacket dp) {
		ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
		DataInputStream dis = new DataInputStream(bais);
System.out.println("parse a packet");
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
		} finally {
			try {
				if (dis != null) dis.close();
				if (bais != null) bais.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
