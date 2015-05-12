package com.sishuai.sharer.action;

import org.eclipse.jface.action.Action;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.views.ClientView;


/**
 * 
 * @author sishuai
 * 局域网支持广播的前提下，将udp转换成tcp稳定连接
 */
public class TCPConnect extends Action {
	//用来建立tcp连接
	private ClientView view;
	private ClientInfo clientInfo;
	private static TCPConnect tcpConnect;
	
	public TCPConnect(String text) {
		super(text);
	}
	
	public static TCPConnect getTcpConnect() {
		if (tcpConnect == null)
			tcpConnect = new TCPConnect("加入该网络");
		return tcpConnect;
	}
	
	public void setView(ClientView clientView) {
		view = clientView;
	}

	public void run() {
		
		clientInfo = (ClientInfo) view.getSelectedItem();
		if (clientInfo == null) {
			return;
		}
		NetworkMgr.getMgr().attempLink(clientInfo.getIp());
	}
}
