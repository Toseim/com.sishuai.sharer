package com.sishuai.sharer.modules;

import java.util.ArrayList;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.sishuai.sharer.modules.interfaces.ItemInfo;
import com.sishuai.sharer.views.ClientView;

/**
 * 显示界面中数据的修改要用到，目前可提供用户的添加和删除
 * @author 四帅
 *
 */
public class ContentManager {
	//用来管理数据
	public static ContentManager contentmanager;
	private ClientTreeContentProvider ctcp;
	private TreeViewer viewer;
	
	
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
	
	public void setTreeViewer(TreeViewer viewer) {
		this.viewer = viewer;
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
	
	public void updateItems() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						viewer.refresh();
					}
				});
			}
		}).start();
	}
}
