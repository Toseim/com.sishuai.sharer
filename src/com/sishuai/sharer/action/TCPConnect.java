package com.sishuai.sharer.action;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.net.MulticastServer;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.modules.net.msg.LinkMsg;
import com.sishuai.sharer.views.ClientView;

public class TCPConnect extends Action {
	//用来建立tcp连接
	private ClientView view;
	private IWorkbenchPart target;
	private ClientInfo clientInfo;
	private ServerSocket ss;
	private static TCPConnect tcpConnect;
	
	public TCPConnect(String text) {
		super(text);
	}
	
	public static TCPConnect getTcpConnect() {
		if (tcpConnect == null) 
			tcpConnect = new TCPConnect("Join in");
System.out.println("tcp inited");
		return tcpConnect;
	}
	
	public void setView(ClientView clientView) {
		view = clientView;
	}

	public void run() {
		clientInfo = (ClientInfo) view.getSelectedItem();
		if (clientInfo == null) {
System.out.println("null");
			return;
		}
System.out.println(clientInfo.getName());
		ss = NetworkMgr.getManager().getServersocket();
		
		//发送尝试连接消息（对方ip ，ip ，port）
		LinkMsg linkMsg = new LinkMsg(clientInfo.getIp(), 
				MulticastServer.getMulticastServer().getIP(), 
				NetworkMgr.getManager().getTCPport());
		
		DatagramSocket ds = NetworkMgr.getManager().getDatagramSocket();
		linkMsg.send(ds, null, NetworkMgr.getManager().getUDPport());
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
System.out.println("Wait for connecting");
							MessageDialog.openInformation(view.getSite().getShell(), "connecting", "Waiting for answer");
							Socket socket = ss.accept();
							//拒绝呢。。。直接接受吧
System.out.println("another user has connected!");
						} catch (IOException e) {
							System.out.println("连接失败");
						}
					}
				});
			}
		});
	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub
		this.target = arg1;
	}

}
