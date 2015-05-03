package com.sishuai.sharer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

import com.sishuai.sharer.modules.interfaces.Msg;



/**
 * 
 * 用来临时测试的地方
 *
 */
public class Test {
	public static void main(String[] args) throws SocketException{
		DatagramSocket ds = new DatagramSocket(12343);
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(Msg.MSG_LINK);
			dos.writeUTF("134.134.13.14");
			dos.writeInt(1341);
			dos.writeUTF("134.134.134.14");
			buf = baos.toByteArray();
			DatagramPacket dPacket = new DatagramPacket(buf, buf.length, new InetSocketAddress("192.168.31.193", 37384));
			ds.send(dPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
