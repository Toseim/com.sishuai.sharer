package com.sishuai.sharer.modules;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.sishuai.sharer.util.Logging;
import com.sishuai.sharer.util.Utils;

/**
 * 显示界面中数据的修改要用到，目前可提供用户的添加和删除
 * @author 四帅
 *
 */
public class ContentManager {
	//用来管理数据
	public static ContentManager contentmanager;
	private TreeViewer viewer;
	private String tmpFolder;
	
	public static ContentManager getMgr() {
		if (contentmanager == null) {
			contentmanager = new ContentManager();
		}
		return contentmanager;
	}
	
	public Object[] getItems() {
		ArrayList<ClientInfo> clientInfos = ClientInfo.getClients();
		return clientInfos.toArray();
	}
	
	public void setTreeViewer(TreeViewer viewer) {
		this.viewer = viewer;
	}
	
	public void updateItems() {
		Logging.getLogger().setFileName("ContentManager");
		Logging.info("Refreshing view element");
		if (viewer.getTree().isDisposed()) return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						viewer.getTree().setRedraw(false);
						viewer.refresh();
						viewer.getTree().setRedraw(true);
					}
				});
			}
		}).start();
	}
	
	public String getTmpFolder() {
		if (tmpFolder == null) {
			tmpFolder = System.getProperty("java.io.tmpdir")+
					Utils.getSeparator()+"shared_files"+Utils.getSeparator();
			File tmp = new File(tmpFolder);
			if (!tmp.exists()) tmp.mkdir();
		}
		return tmpFolder;
	}
}