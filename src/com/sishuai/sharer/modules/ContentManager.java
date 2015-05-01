package com.sishuai.sharer.modules;

import java.util.ArrayList;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.sishuai.sharer.modules.interfaces.ItemInfo;

/**
 * 显示界面中数据的修改要用到，目前可提供用户的添加和删除
 * @author 四帅
 *
 */
public class ContentManager implements IPropertyChangeListener{
	//用来管理数据
	public static ContentManager contentmanager;
	private ClientTreeContentProvider ctcp;
	
	
	public static ContentManager getManager() {
		if (contentmanager == null) {
			contentmanager = new ContentManager();
		}
		return contentmanager;
	}
	
	public Object[] getItems() {
		ArrayList<ClientInfo> clientInfos = ClientInfo.getClients();
		return clientInfos.toArray();
	}
	
	public void setContentProvider(ClientTreeContentProvider clientTreeContentProvider) {
		this.ctcp = clientTreeContentProvider;
	}
	
	public void addItem(ItemInfo item, ClientInfo clientInfo) {
		if (clientInfo == null) {
			ClientInfo.getClients().add((ClientInfo)item);
			ctcp.itemsChanged(item, Header.getHeader(), 0);
		}
		else {
			clientInfo.getFiles().add((FileInfo)item);
			ctcp.itemsChanged(item, clientInfo, 0);
		}
	}
	
	public void removeItem(ItemInfo item, ClientInfo clientInfo) {
		if (clientInfo == null) {
			ClientInfo.getClients().remove((ClientInfo)item);
			ctcp.itemsChanged(item, Header.getHeader(), 1);
		}
		else {
			clientInfo.getFiles().remove((FileInfo)item);
			ctcp.itemsChanged(item, clientInfo, 1);
		}
	}
	//暂时没想好，要不要把listener加进来处理
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		//test
		return;
	}

}
