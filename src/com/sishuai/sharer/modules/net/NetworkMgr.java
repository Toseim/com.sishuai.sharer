package com.sishuai.sharer.modules.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Random;
import java.util.regex.Pattern;

import com.sishuai.sharer.modules.net.msg.LinkMsg;

public class NetworkMgr {
	public static Pattern pattern1 = Pattern.compile("^192\\.168\\.[0-9]{1,3}\\.[0-9]{1,3}");
	public static Pattern pattern2 = Pattern.compile("^10\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	public static Pattern pattern3 = Pattern.compile("^172\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	
	private Random random = new Random();
	private static NetworkMgr networkMgr;
	private ServerSocket serverSocket;
	private int TCPport = 0;
	private int UDPport = 27384;
	private static MulticastServer ms;
	private DatagramSocket datagramSocket;
	
	public ServerSocket getServersocket() {
		if (serverSocket == null) {
			while (true) {
				try {
					serverSocket = new ServerSocket(getTCPport());
				} catch (IOException e) {
					continue;
				}
				break;
			}
		}
		return serverSocket;
	}
	public int getTCPport() {
		if (TCPport == 0) 
			TCPport = random.nextInt(25535) + 10000;
		return TCPport;
	}
	
	public DatagramSocket getDatagramSocket() {
		if (datagramSocket == null) {
			while (true) {
				try {
					datagramSocket = new DatagramSocket(getUDPport());
				} catch (Exception e) {
					continue;
				}
				break;
			}
		}
		return datagramSocket;
	}

	public static MulticastServer getMulticastServer() {
		if (ms == null)
			ms = new MulticastServer();
		return ms;
	}
	
	public int getUDPport() {
		return UDPport;
	}
	public static NetworkMgr getManager() {
		if (networkMgr == null) 
			networkMgr = new NetworkMgr();
		return networkMgr;
	}
	
	public void attempLink(String objectIP) {
		LinkMsg linkMsg = new LinkMsg(objectIP, 
				NetworkMgr.getMulticastServer().getIP(), TCPport);
		
		DatagramSocket ds = NetworkMgr.getManager().getDatagramSocket();
		linkMsg.send(ds, null, 0);
	}
}
