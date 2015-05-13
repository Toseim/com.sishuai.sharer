package com.sishuai.sharer.modules;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;

import com.sishuai.sharer.action.ChatDialog;
import com.sishuai.sharer.modules.interfaces.ItemInfo;
import com.sishuai.sharer.util.Logging;

/**
 * 
 * @author 四帅 用来存放用户相关信息的类
 */
public class ClientInfo implements ItemInfo {
	// 1
	private String name;
	// 2用户对话框新信息数
	private int msgs = 0;
	// 3文件新动态数
	private int newFileCount = 0;
	// 4
	private String ip;
	// 5
	private boolean isConnected = false;

	// 一些与界面相关的信息
	private boolean isDialogOpened = false;
	private ChatDialog chatDialog;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;

	private String temp = "";

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

	// 用户消息
	public void incMsg() {
		msgs++;
	}

	public void clearMsg() {
		msgs = 0;
	}

	public int getMsg() {
		return msgs;
	}

	// 获得用户列表
	public static ArrayList<ClientInfo> getClients() {
		if (clients == null) {
			Logging.getLogger().setFileName("ClientInfo");
			Logging.info("初始化用户组");
			clients = new ArrayList<ClientInfo>();
		}
		return clients;
	}

	// 获得ip列表
	public static ArrayList<String> getIPList() {
		if (iptable == null) {
			Logging.getLogger().setFileName("ClientInfo");
			Logging.info("初始化已知IP列表");
			iptable = new ArrayList<String>();
		}
		return iptable;
	}

	// 连接判断
	public boolean isConnected() {
		return isConnected;
	}

	// 设置连接
	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
		try {
			Logging.getLogger().setFileName("ClientInfo");
			Logging.info("架设用户的连接管道");
			this.dis = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			this.dos = new DataOutputStream(new BufferedOutputStream(
					socket.getOutputStream()));
			new Thread(new RecvThread()).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("构建管道失败..");
		}
	}

	public DataInputStream getDataInputStream() {
		return dis;
	}

	public DataOutputStream getDataOutputStream() {
		return dos;
	}

	// 获取交互文件
	public ArrayList<FileInfo> getFiles() {
		if (files == null) {
			Logging.getLogger().setFileName("ClientInfo");
			Logging.info("初始化用户文件列表");
			files = new ArrayList<FileInfo>();
		}
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

	public String getTempString() {
		String string = temp;
		temp = "";
		msgs = 0;
		ContentManager.getMgr().updateItems();
		return string;
	}

	@Override
	public String getOne() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getTwo() {
		// TODO Auto-generated method stub
		return "Msg[" + msgs + "]";
	}

	@Override
	public String getThree() {
		return "NewFile[" + newFileCount + "]";
	}

	@Override
	public String getFour() {
		// TODO Auto-generated method stub
		return ip + "";
	}

	@Override
	public String getFive() {
		// TODO Auto-generated method stub
		if (!isConnected)
			return "No";
		return getFiles().size() + "";
	}

	public void sendFile(String filePath) {
		BufferedInputStream bis = null;
		try {
			dos.writeUTF("$");
			String os = System.getProperty("os.name");
			
			if (os.equals("Linux"))
				dos.writeUTF(filePath.substring(filePath.lastIndexOf("/") + 1));
			else if (os.indexOf("Windows") != -1)
				dos.writeUTF(filePath.substring(filePath.lastIndexOf("\\") + 1));
			
			dos.flush();
			long fileLen = new File(filePath).length();
			dos.writeLong(fileLen);
			bis = new BufferedInputStream(new FileInputStream(filePath));
			byte[] buf = new byte[(int)fileLen];
			bis.read(buf, 0, buf.length);
			dos.write(buf, 0, buf.length);
			dos.flush();
			Logging.info("文件已传输");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bis != null)
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public void acceptFile() {
		Logging.getLogger().setFileName("ClientInfo");
		BufferedOutputStream bos = null;
		try {
			String filename = dis.readUTF();
			long fileLen = dis.readLong();
			Logging.info("接收文件就绪，文件名 " + filename);

			ProjectMgr.createJavaProject(
					filename.substring(0, filename.indexOf('.')), "");
			String filePath = ProjectMgr.path + "/"+filename;
			bos = new BufferedOutputStream(new FileOutputStream(filePath));
			byte[] buf = new byte[(int)fileLen];
			dis.read(buf, 0, buf.length);
			bos.write(buf, 0, buf.length);
			bos.flush();
			Logging.info("success to accept file");
			getFiles().add(new FileInfo(filePath, filename, fileLen));
			newFileCount++;
			ContentManager.getMgr().updateItems();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bos != null)
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	class RecvThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (true) {
					String string = dis.readUTF();
					if (string.equals("$")) {
						acceptFile();
						continue;
					}

					// 对消息进行分类处理
					// 一共两种，一个是普通的文本对话.消息，另一个是file的内容
					if (isDialogOpened)
						new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										chatDialog.getDialog().append(string);
									}
								});
							}
						}).start();
					else {
						msgs++;
						temp += temp + string;
					}
					ContentManager.getMgr().updateItems();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				Logging.warning("与" + getName() + "的连接已经断开");
				try {
					if (dis != null)
						dis.close();
					if (dos != null)
						dos.close();
					if (socket != null)
						socket.close();
					ClientInfo.getIPList().remove(ClientInfo.this.getIp());
					ClientInfo.getClients().remove(ClientInfo.this);
					ContentManager.getMgr().updateItems();
				} catch (IOException e_1) {
					// TODO Auto-generated catch block
					e_1.printStackTrace();
				}
			}
		}
	}
}
