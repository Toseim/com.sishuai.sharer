package com.sishuai.sharer.action;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

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
	private ServerSocket ss;
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
		//if (网络处于组播支持的环境下)
		clientInfo = (ClientInfo) view.getSelectedItem();
		if (clientInfo == null) {
			return;
		}
		//if (网络
		ss = NetworkMgr.getManager().getServersocket();
		
		//发送尝试连接消息（对方ip ，ip ，port）
		NetworkMgr.getManager().attempLink(clientInfo.getIp());
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Socket socket = ss.accept();
					clientInfo.setSocket(socket);
					clientInfo.setConnected(true);
					view.showMessage("We connect!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
	}

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub
	}

}
