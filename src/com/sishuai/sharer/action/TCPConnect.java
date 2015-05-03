package com.sishuai.sharer.action;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
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
	private Socket socket;
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
		//网络已经被占用
		NetworkMgr.setState(true);
		//if (网络处于组播支持的环境下)
		clientInfo = (ClientInfo) view.getSelectedItem();
		if (clientInfo == null) {
			return;
		}
		//if (网络
		ss = NetworkMgr.getMgr().getServersocket();
		
		//发送尝试连接消息（对方ip ，ip ，port）
		NetworkMgr.getMgr().attempLink(clientInfo.getIp());
		view.showMessage("等待对面的用户想到一块去");

		new Thread(new ConnectThread()).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(30000);
					if (socket == null)

						ss.close();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	class ConnectThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				socket = ss.accept();
				clientInfo.setSocket(socket);
				clientInfo.setConnected(true);
				
				ContentManager.getManager().updateItems();
				//未测试过
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						view.showMessage("We connect!");
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						MessageDialog.openError(view.getSite().getShell(), "TIME OUT!", "连接超时，对方未响应");
						view.showMessage("");
					}
				});
			}
			
			NetworkMgr.setState(false);
		}
	}
}
