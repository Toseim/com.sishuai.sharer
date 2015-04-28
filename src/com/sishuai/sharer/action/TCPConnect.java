package com.sishuai.sharer.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.sishuai.sharer.modules.ClientInfo;

public class TCPConnect implements IObjectActionDelegate{
	private IWorkbenchPart target;
	private ClientInfo clientInfo;
	
	
	public TCPConnect(ClientInfo clientInfo) {
		super();
		this.clientInfo = clientInfo;
	}
	

	@Override
	public void run(IAction arg0) {
		//打开对话框
		
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub
		this.target = arg1;
	}

}
