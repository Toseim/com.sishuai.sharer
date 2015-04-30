package com.sishuai.sharer.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;

public class OpenView implements IWorkbenchWindowActionDelegate {
	//用来打开视图的action
	private IWorkbenchWindow window;
	
	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		if (window == null) return;
		IWorkbenchPage page = window.getActivePage();
		if (page == null) return;
		
		try {
			page.showView("com.sishuai.sharer.views.ClientView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			MessageDialog.openError(window.getShell(), "Error", "There is Something Wrong!");
		}
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		System.out.println("iadfjaidfj");
	}

	@Override
	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub
		this.window = arg0;
	}

}
