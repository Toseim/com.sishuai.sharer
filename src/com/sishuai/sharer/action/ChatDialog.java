package com.sishuai.sharer.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Display;

import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.views.ClientView;
/**
 * 用户之间的聊天窗口
 * @author 四帅
 *
 */
public class ChatDialog extends Action{
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
		ClientInfo clientInfo = (ClientInfo) view.getSelectedItem();
		dis = clientInfo.getDataInputStream();
		dos = clientInfo.getDataOutputStream();
		//Display.getDefault().asyncExec(new RecvThread());
	}
	
	public void run() {
		//界面显示
	}
}
