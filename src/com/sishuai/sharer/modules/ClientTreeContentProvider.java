package com.sishuai.sharer.modules;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;


public class ClientTreeContentProvider
	implements ITreeContentProvider, IStructuredContentProvider {
	private TreeViewer viewer;
	private ContentManager manager;
	
	@Override
	public Object[] getChildren(Object arg0) {
		if (arg0 instanceof Header) 
			return ClientInfo.getClients().toArray();
		
		ClientInfo clientInfo = (ClientInfo) arg0;
		return clientInfo.getFiles().toArray();
	}

	@Override
	public Object getParent(Object arg0) {
		return null;
	}

	@Override
	public boolean hasChildren(Object arg0) {
		//头结点
		if (arg0 instanceof Header && ClientInfo.getClients().size() > 0) return true;
		//先保留，以后如果传输目录的话，估计要改改
		if (arg0 instanceof FileInfo) return false;
		//文件为空
		if (((ClientInfo)arg0).getFiles().size() ==0)
			return false;
		return true;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	public void itemsChanged(ItemInfo itemInfo, ItemInfo parent, int type) {
		// TODO �Զ����ɷ������
		viewer.getTree().setRedraw(false);
		try{
			switch (type) {
			case 0:
				viewer.add(parent, itemInfo);
				break;
			case 1:
				viewer.remove(itemInfo);
				break;
			default:
				break;
			}
		}
		finally{
			viewer.getTree().setRedraw(true);
		}
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer)viewer;
		manager = (ContentManager)newInput;
		if (manager != null)
		manager.setContentProvider(this);
	}

	@Override
	public Object[] getElements(Object arg0) {
		return new Object[] {Header.getHeader()};
	}
	
}
