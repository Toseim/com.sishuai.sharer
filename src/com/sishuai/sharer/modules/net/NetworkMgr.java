package com.sishuai.sharer.modules.net;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class NetworkMgr {
	private Random random = new Random();
	private static NetworkMgr networkMgr;
	private ServerSocket serverSocket;
	private int TCPport = 0;
	private int UDPport = 0;
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
	
	public int getUDPport() {
		if (UDPport == 0) 
			UDPport = random.nextInt(25535) + 10000;
		return UDPport;
	}
	public static NetworkMgr getManager() {
		if (networkMgr == null) 
			networkMgr = new NetworkMgr();
		return networkMgr;
	}
}
