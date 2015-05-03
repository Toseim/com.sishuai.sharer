package com.sishuai.sharer.modules;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import com.sishuai.sharer.action.ChatDialog;
import com.sishuai.sharer.modules.interfaces.ItemInfo;

/**
 * 
 * @author 四帅
 * 用来存放用户相关信息的类
 */
public class ClientInfo implements ItemInfo{
	//1
	private String name;
	//2用户对话框新信息数
	private int msgs = 0;
	//3文件新动态数
	private int newFileCount = 0;
	//4
	private String ip;
	//5
	private boolean isConnected = false;
	
	//一些与界面相关的信息
	private boolean isDialogOpened = false;
	private ChatDialog chatDialog;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private ArrayList<FileInfo> files;
	private static ArrayList<ClientInfo> clients;
	private static ArrayList<String> iptable;
	
	public ClientInfo(String ip, String name) {
		this.ip = ip;
		this.name = name;
	}

	public int getNewFileCount() {
		return newFileCount;
	}

	public void setNewFileCount(int newFileCount) {
		this.newFileCount = newFileCount;
	}
	
	//用户消息
	public void incMsg(){
		msgs++;
	}
	public void clearMsg(){
		msgs = 0;
	}
	public int getMsg() {
		return msgs;
	}
	
	//获得用户列表
	public static ArrayList<ClientInfo> getClients() {
		if (clients == null) 
			clients = new ArrayList<ClientInfo>();
		return clients;
	}
	
	//获得ip列表
	public static ArrayList<String> getIPList() {
		if (iptable == null) 
			iptable = new ArrayList<String>();
		return iptable;
	}
	
	//连接判断
	public boolean isConnected() {
		return isConnected;
	}
	
	//设置连接
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			this.dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			this.dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			new Thread(new RecvThread()).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DataInputStream getDataInputStream() {
		return dis;
	}
	public DataOutputStream getDataOutputStream() {
		return dos;
	}

	//获取交互文件
	public ArrayList<FileInfo> getFiles() {
		if (files == null)
			files = new ArrayList<FileInfo>();
		return files;
	}

	public String getIp() {
		return ip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public ChatDialog getChatDialog() {
		return chatDialog;
	}

	public void setChatDialog(ChatDialog chatDialog) {
		this.chatDialog = chatDialog;
	}

	public boolean isDialogOpened() {
		return isDialogOpened;
	}

	public void setDialogOpened(boolean isDialogOpened) {
		this.isDialogOpened = isDialogOpened;
	}

	@Override
	public String getOne() {
		// TODO Auto-generated method stub
		return name;
	}
	@Override
	public String getTwo() {
		// TODO Auto-generated method stub
		return "Msg[" + msgs+"]";
	}
	@Override
	public String getThree() {
		return "NewFile["+newFileCount+"]";
	}
	@Override
	public String getFour() {
		// TODO Auto-generated method stub
		return ip+"";
	}
	@Override
	public String getFive() {
		// TODO Auto-generated method stub
		if (!isConnected) return "No";
		return files.size()+"";
	}
	
	class RecvThread implements Runnable {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				try {
					String string = dis.readUTF();
					//对消息进行分类处理
					//一共两种，一个是普通的文本对话消息，另一个是flie的内容
					chatDialog.getDialog().append(name+": "+string);
					if (!isDialogOpened) msgs++;
					
					ContentManager.getManager().updateItems();
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("与"+getName()+"的连接已经断开");
					try {
						if (dis != null) dis.close();
						if (dos != null) dos.close();
						if (socket != null) socket.close();
					} catch (IOException e_1) {
						// TODO Auto-generated catch block
						e_1.printStackTrace();
					}
				} 
			}
		}
	}
}
