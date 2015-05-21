package com.sishuai.sharer.modules;

import com.sishuai.sharer.modules.interfaces.ItemInfo;
import com.sishuai.sharer.modules.net.NetworkMgr;

/**
 * 树结构的根节点
 * @author 四帅
 *
 */
public class Header implements ItemInfo{
	//可以用来提供信息
	private static Header header;
	
	public static Header getHeader() {
		if (header == null) header = new Header();
		return header;
	}

	@Override
	public String getOne() {
		// TODO Auto-generated method stub
		String string = "";
		if (NetworkMgr.getMgr().getIP() != null)
			string = NetworkMgr.getMgr().getIP();
		return string;
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
