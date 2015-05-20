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
import org.eclipse.swt.widgets.Text;

import com.sishuai.sharer.modules.interfaces.ItemInfo;
import com.sishuai.sharer.util.Logging;
import com.sishuai.sharer.util.Utils;

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
	private Text dialogText;
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
			Logging.info("Initialize the user group");
			clients = new ArrayList<ClientInfo>();
		}
		return clients;
	}

	// 获得ip列表
	public static ArrayList<String> getIPList() {
		if (iptable == null) {
			Logging.getLogger().setFileName("ClientInfo");
			Logging.info("The initialization list of known IP");
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
			Logging.info("Setting up the user connected pipeline");
			this.dis = new DataInputStream(new BufferedInputStream(
					socket.getInputStream()));
			this.dos = new DataOutputStream(new BufferedOutputStream(
					socket.getOutputStream()));
			new Thread(new RecvThread()).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Logging.fatal("Setting up the pipeline failed.");
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
			Logging.info("Initialize the user list file");
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

	public Text getDialog() {
		return dialogText;
	}

	public void setDialog(Text dialogText) {
		this.dialogText = dialogText;
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
	
	public int returnID(String fileName) {
		for (int i = 0; i < files.size(); i++) {
			FileInfo fileInfo = files.get(i);
			if (fileInfo.getFilename().equals(fileName)) 
				return fileInfo.getfid();
		}
		return FileInfo.getPublicID();
	}

	public boolean sendFile(String filePath) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			dos.writeUTF("$");
			String filename = filePath.substring(filePath.lastIndexOf(Utils.getSeparator())+1);
			dos.writeUTF(filename);
			
			dos.flush();
			long fileLen = new File(filePath).length();
			dos.writeLong(fileLen);
			dos.flush();
			bis = new BufferedInputStream(new FileInputStream(filePath));
			bos = new BufferedOutputStream(new FileOutputStream(
					ContentManager.getMgr().getTmpFolder()+
					Utils.getFourFormat().format(returnID(filename))+".tmp"));
			byte[] buf = new byte[(int)fileLen];
			bis.read(buf, 0, buf.length);
			dos.write(buf, 0, buf.length);
			dos.flush();
			bos.write(buf, 0, buf.length);
			bos.flush();
			Logging.info("The file has been transferred");
			
		} catch (Exception e) {
			e.printStackTrace();
			Logging.fatal("File error");
			return false;
		}  finally {
			try {
				if (bis != null) bis.close();
				if (bos != null) bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	public void acceptFile() {
		Logging.getLogger().setFileName("ClientInfo");
		try {
			String filename = dis.readUTF();
			long fileLen = dis.readLong();
			System.out.println(fileLen);
			Logging.info("接收文件就绪，文件名 " + filename);
			String filePath = ProjectMgr.path + "/"+filename;
			byte[] buf = new byte[(int)fileLen];
			dis.read(buf, 0, buf.length);
			ProjectMgr.createJavaProject(filename.substring(0, 
					filename.indexOf('.')), new String(buf), name);
			
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
					if (string.startsWith("#")) {
						setName(string.substring(1));
						
						continue;
					}
					// 对消息进行分类处理
					if (isDialogOpened)
						new Thread(new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Display.getDefault().asyncExec(new Runnable() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										dialogText.append(string);
									}
								});
							}
						}).start();
					else {
						msgs++;
						temp += temp + string;
						ContentManager.getMgr().updateItems();
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				disconnect();
				ContentManager.getMgr().updateItems();
			}
		}
	}
	public void disconnect() {
		try {
			if (socket != null && !socket.isClosed()) {
				Logging.warning("The connection with " + getName() + " has been disconnected");
				if (dis != null)
					dis.close();
				if (dos != null)
					dos.close();
				socket.close();
			}
			ClientInfo.getIPList().remove(getIp());
			ClientInfo.getClients().remove(this);
		} catch (IOException e_1) {
			// TODO Auto-generated catch block
			e_1.printStackTrace();
		}
	}
}
