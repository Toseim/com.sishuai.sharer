package com.sishuai.sharer.modules;

import com.sishuai.sharer.modules.interfaces.ItemInfo;

/**
 * 树结构的根节点
 * @author 四帅
 *
 */
public class Header implements ItemInfo{
	//树的头结点，因为树不能添加初始节点
	//可以用来提供信息
	private static Header header;
	
	public static Header getHeader() {
		if (header == null) header = new Header();
		return header;
	}
	
	@Override
	public String getOne() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTwo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getThree() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFive() {
		// TODO Auto-generated method stub
		return null;
	}

}
