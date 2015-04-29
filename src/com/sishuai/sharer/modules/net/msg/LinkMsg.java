package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.modules.net.MulticastServer;

public class LinkMsg implements Msg {
	private static int msgType = Msg.MSG_LINK;
	private String objectIP;
	private String localIP;
	private int localPort;
	
	public LinkMsg() {
	}
	public LinkMsg(String objectIP, String localIP, int localPort) {
		this.objectIP = objectIP;
		this.localIP = localIP;
		this.localPort = localPort;
	}

	@Override
	public void send(DatagramSocket ds, Object other, int port) {
		// TODO Auto-generated method stub
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeInt(msgType);
			dos.writeUTF(localIP);
			dos.writeInt(localPort);
			dos.flush();
			
			byte[] buf = baos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buf, buf.length, 
					new InetSocketAddress(objectIP, MulticastServer.
							getMulticastServer().getPort()));
			
			ds.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stub
		try {
			String remoteIP = dis.readUTF();
			int remotePort = dis.readInt();
			for (int i = 0; i < ClientInfo.getClients().size(); i++) {
				ClientInfo clientInfo = ClientInfo.getClients().get(i);
				if (clientInfo.getIp().equals(remoteIP)) {
					Socket socket = new Socket(remoteIP, remotePort);
					clientInfo.setSocket(socket);
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
