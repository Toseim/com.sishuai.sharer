package com.sishuai.sharer.modules;

import java.util.ArrayList;

public class ClientInfo implements ItemInfo{
	//1
	private String name;
	//2
	private int msgs = 0;
	//3
	private int newFileCount = 0;
	//4
	private String ip;
	//5
	private boolean isConnected = false;
	
	private ArrayList<FileInfo> files = new ArrayList<FileInfo>();
	private static ArrayList<ClientInfo> clients;
	private static ArrayList<String> iptable;
	
	public ClientInfo(String ip, String name) {
		this.ip = ip;
		this.name = name;
	}
	//新文件夹计数
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

	//获取交互文件
	public ArrayList<FileInfo> getFiles() {
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
	
	public void showMsg() {
		//用户之间的对话窗口
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
}
