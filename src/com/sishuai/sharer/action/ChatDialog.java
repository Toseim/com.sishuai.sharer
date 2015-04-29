package com.sishuai.sharer.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.sishuai.sharer.views.ClientView;

public class ChatDialog {
	private DataInputStream dis;
	private DataOutputStream dos;
	private ClientView view;
	private static ChatDialog chatDialog;
	
	public void setView(ClientView view) {
		this.view = view;
	}
	
	public static ChatDialog getDialog() {
		if (chatDialog == null)
			chatDialog = new ChatDialog();
		return chatDialog;
	}
	
	public void NetInit() {
		
	}
	
}
