package com.sishuai.sharer.modules;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 内容提供者，也可以不用管它
 * @author 四帅
 *
 */
public class ClientTreeContentProvider
	implements ITreeContentProvider, IStructuredContentProvider {
	
	@Override
	public Object[] getChildren(Object arg0) {
		if (arg0 instanceof Header) 
			return ClientInfo.getClients().toArray();
		
		ClientInfo clientInfo = (ClientInfo) arg0;
		return clientInfo.getFiles().toArray();
	}

	@Override
	public Object getParent(Object arg0) {
		if (arg0 instanceof ClientInfo) return Header.getHeader();
		return null; //没必要
	}

	@Override
	public boolean hasChildren(Object arg0) {
		//头结点
		if (arg0 instanceof Header)
			if (ClientInfo.getClients().size() > 0) return true;
			else return false;
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
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object arg0) {
		return new Object[] {Header.getHeader()};
	}
	
}
