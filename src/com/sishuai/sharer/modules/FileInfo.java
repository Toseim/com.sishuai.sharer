package com.sishuai.sharer.modules;

import java.util.Date;

import org.eclipse.core.runtime.IAdaptable;

public class FileInfo implements IAdaptable, ItemInfo{
	//1
	private String filename;
	//2文件状态标识
	private int stateIn = 2;
	public static final String[] state = {"commited", "updated", "new","done"};
	//3编辑的行数
	private int lineEdit;
	//4
	private String fTime;
	//5
	private int len;
	
	public FileInfo(String filename, int len) {
		this.filename = filename;
		this.len = len;
		this.fTime = new Date().toString();
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
	public void setTimes(String times) {
		this.fTime = times;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}

	public int getLineEdit() {
		return lineEdit;
	}
	public void setLineEdit(int lineEdit) {
		this.lineEdit = lineEdit;
	}

	@Override
	public Object getAdapter(Class arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void showDetail() {
		
	}

	@Override
	public String getOne() {
		// TODO Auto-generated method stub
		return filename;
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
		return len+"";
	}
	
}
