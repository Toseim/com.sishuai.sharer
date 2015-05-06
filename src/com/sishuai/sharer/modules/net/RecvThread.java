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
import com.sishuai.sharer.util.Logging;

public class RecvThread implements Runnable {
	private boolean isMulti;
	private MulticastSocket multicastSocket;
	private DatagramSocket datagramSocket;
	private byte[] buf = new byte[1024];

	public RecvThread(DatagramSocket ds, boolean isMulti) {
		// TODO Auto-generated constructor stub
		Logging.getLogger().setFileName("RecvThread");
		this.isMulti = isMulti;
		this.datagramSocket = ds;
		Logging.info("创建一个UDP接受线程运行");
	}

	public RecvThread(MulticastSocket ms, boolean isMulti) {
		// TODO Auto-generated constructor stub
		Logging.getLogger().setFileName("RecvThread");
		this.isMulti = isMulti;
		this.multicastSocket = ms;
		Logging.info("创建一个多播接受线程运行");
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while (true) {
				DatagramPacket dp = new DatagramPacket(buf, buf.length);
				if (isMulti && multicastSocket != null) {
					multicastSocket.receive(dp);
					Logging.info("接收到一个多播包文件");
				} else if (datagramSocket != null) {
					datagramSocket.receive(dp);
					Logging.info("接收到一个UDP包文件");
				}
				parse(dp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Server is closed");
		}
	}

	public void parse(DatagramPacket dp) {
		Logging.getLogger().setFileName("RecvThread");
		Logging.info("正在解包中...");
		ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
		DataInputStream dis = new DataInputStream(bais);
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
				Logging.info("关闭解包管道");
				if (dis != null)
					dis.close();
				if (bais != null)
					bais.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
