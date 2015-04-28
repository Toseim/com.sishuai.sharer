package com.sishuai.sharer.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;

import com.sishuai.sharer.modules.ContentManager;

public class RefreshTree implements IViewActionDelegate{
	private IWorkbenchPart target;
	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		System.out.println("button pushed");
		ContentManager.getManager().pushCache();
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(IViewPart arg0) {
		// TODO Auto-generated method stub
		this.target = arg0;
	}

}
