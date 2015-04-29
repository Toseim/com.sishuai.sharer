package com.sishuai.sharer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class SendSender {
	public static void main(String[] args) {
		try {
			DatagramSocket ds = new DatagramSocket(12341);
			byte[] buf = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress("127.0.0.1", 8647));
			ds.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
