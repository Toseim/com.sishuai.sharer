package com.sishuai.sharer.modules.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.NewWizardAction;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.Header;

public class MulticastServer {
	Pattern pattern1 = Pattern.compile("^192\\.168\\.");
	Pattern pattern2 = Pattern.compile("^10\\.");
	Pattern pattern3 = Pattern.compile("^172\\.[0-6]{1,3}\\.");
	TreeViewer viewer;
	InetAddress group;
	int port = 8647;
	DatagramPacket mydp = null;
	MulticastSocket multicastSocket;
	
	public MulticastServer(TreeViewer viewer) {
		this.viewer = viewer;
	}
	
	public String getIP() {
		try {
			group = InetAddress.getByName("224.0.0.2");
			multicastSocket = new MulticastSocket(port);
			multicastSocket.joinGroup(group);
			String ip = null;
			InetAddress[] addresses = InetAddress.getAllByName(InetAddress.
					getLocalHost().getHostName());
			for (int i=0; i< addresses.length; i++) {
				String s = addresses[i].getHostAddress();
				if (pattern1.matcher(s).find() || pattern2.matcher(s).find() 
						|| pattern3.matcher(s).find()) {
					ip = s;
					break;
				}
			}
			return ip;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void disConnect() {
		 try {
				multicastSocket.leaveGroup(group);
			} catch (IOException e) {
				e.printStackTrace();
			}
         multicastSocket.close();
	}
	
	public void run() {
		String ip = getIP();
		if (ip == null) return;
		//用户名为haha，以后改
		byte[] buffer = (ip+" haha").getBytes();
		ClientInfo.getIPList().add(ip);
		mydp = new DatagramPacket(buffer, buffer.length, group, port);
		try {
			multicastSocket.send(mydp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//加入自己的ip，for test
		ClientInfo.getClients().add(new ClientInfo(ip, "haha"));
		System.out.println("start");
		new Thread(new ClientThread()).start();
		//Display.getCurrent().asyncExec(new ClientThread());
	}
	
	
	class ClientThread implements Runnable {
		byte[] buffer = new byte[1024];
		
		public void run() {
			DatagramPacket dp;
			while (true) {
			    try {
					dp = new DatagramPacket(buffer, buffer.length);
					MulticastServer.this.multicastSocket.receive(dp);
					String s = new String(dp.getData(), 0, dp.getLength());
					System.out.println(s);
					//s1为ip， s2为名称
					String s1 = s.substring(0, s.indexOf(" "));
					String s2 = s.substring(s.indexOf(" ")+1);
					//信息如果已经记录，不发送
					if (!ClientInfo.getIPList().contains(s1)) {
						ClientInfo.getClients().add(new ClientInfo(s1, s2));
						ClientInfo.getIPList().add(s1);
						multicastSocket.send(mydp);
						//ContentManager.getManager().addToCache(new ClientInfo(s1, s2), null);
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ContentManager.getManager().addItem(new ClientInfo(s1,s2), null); 
							}
						});
					}
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		} 
	}
}
