package com.sishuai.sharer.modules.interfaces;

import java.io.DataInputStream;
/**
 * 消息类型
 * @author 四帅
 *
 */
public interface Msg {
	public static final int MSG_ENTER = 0;
	public static final int MSG_EXIT = 1;
	public static final int MSG_LINK = 2;
	public static final int MSG_REFUSE = 3;
	
	public void send(Object other,int port);
	public void parse(DataInputStream dis);
}
