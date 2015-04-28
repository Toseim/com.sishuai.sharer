package com.sishuai.sharer.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class ContentManager implements IPropertyChangeListener{
	public static ContentManager contentmanager;
	private ClientTreeContentProvider ctcp;
	private static HashMap<ItemInfo, ClientInfo> clientCached;
	
	
	public static ContentManager getManager() {
		if (contentmanager == null) {
			contentmanager = new ContentManager();
			clientCached = new HashMap<ItemInfo, ClientInfo>();
		}
		return contentmanager;
	}
	
	public void addToCache(ItemInfo item, ClientInfo clientInfo) {
		clientCached.put(item, clientInfo);
	}
	
	public void pushCache() {
		for (Entry<ItemInfo, ClientInfo>entry:clientCached.entrySet())
			addItem(entry.getKey(), entry.getValue());
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
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		//test
		return;
	}

}
