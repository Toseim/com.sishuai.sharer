package com.sishuai.sharer.modules.interfaces;

import java.io.DataInputStream;
import java.net.DatagramSocket;

public interface Msg {
	public static final int MSG_ENTER = 0;
	public static final int MSG_EXIT = 1;
	public static final int MSG_LINK = 2;
	
	public void send(DatagramSocket ds,Object other,int port);
	public void parse(DataInputStream dis);
}
