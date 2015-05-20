package com.sishuai.sharer.action;

import org.eclipse.jface.action.Action;

import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.views.ClientView;

public class RegetIP extends Action {
	private ClientView view;

	public RegetIP(ClientView view) {
		// TODO Auto-generated constructor stub
		super("Reget IP");
		this.view = view;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		NetworkMgr.getMgr().IPclear();
		NetworkMgr.getMgr().getIP();
		view.showMessage(NetworkMgr.getMgr().getIPs().toString());
		ContentManager.getMgr().updateItems();
	}
}
