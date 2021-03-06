package com.sishuai.sharer.modules;

import java.util.Date;

import com.sishuai.sharer.modules.interfaces.ItemInfo;
import com.sishuai.sharer.util.Utils;

/**
 * 存放文件的信息
 * @author 四帅
 *
 */
public class FileInfo implements ItemInfo{
	//1
	private String filename;
	//2文件状态标识
	private int stateIn = 2;
	public static final String[] state = {"commited", "updated", "new", "done"};
	//3编辑的行数
	private int lineEdit;
	//4
	private String fTime;
	//5
	private String len;
	
	private String filePath;
	private boolean diffible = false;
	private int fid;
	private ClientInfo clientInfo;
	
	private static int fileID = 0;
	
	public FileInfo(String filePath, String filename, long len) {
		this.filePath = filePath;
		this.filename = filename;
		this.len = Utils.getOnePointFormat().format(len / 1024.0);
		this.fTime = Utils.getSimpleDataFormat().format(new Date());
	}
	
	public static int getPublicID() {
		return ++fileID;
	}
	
	public void setClientInfo(ClientInfo clientInfo) {
		this.clientInfo = clientInfo;
	}
	
	public ClientInfo getClientInfo() {
		return clientInfo;
	}
	
	public String getState() {
		return state[stateIn];
	}
	
	public void setStateIn(int stateIn) {
		this.stateIn = stateIn;
	}
	
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getTimes() {
		return fTime;
	}
	public void updateTime() {
		this.fTime = Utils.getSimpleDataFormat().format(new Date());
	}
	public String getLen() {
		return len;
	}

	public int getLineEdit() {
		return lineEdit;
	}
	
	public void setLineEdit(int lineEdit) {
		this.lineEdit = lineEdit;
	}

	public String getFilePath() {
		return filePath;
	}
	
	public void setDiffible(boolean diffible) {
		this.diffible = diffible;
	}
	
	public boolean isDiffible() {
		return diffible;
	}
	
	public void setFid(int fid) {
		this.fid = fid;
	}
	
	public int getFid() {
		return fid;
	}
	
	@Override
	public String getOne() {
		// TODO Auto-generated method stub
		return "  "+filename;
	}

	@Override
	public String getTwo() {
		// TODO Auto-generated method stub
		return state[stateIn];
	}

	@Override
	public String getThree() {
		// TODO Auto-generated method stub
		return lineEdit+" line";
	}

	@Override
	public String getFour() {
		// TODO Auto-generated method stub
		return fTime;
	}

	@Override
	public String getFive() {
		// TODO Auto-generated method stub
		return len+"K";
	}
	
}
