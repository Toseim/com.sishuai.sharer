package com.sishuai.sharer.views;


import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import com.sishuai.sharer.action.ChatDialog;
import com.sishuai.sharer.action.OthLink;
import com.sishuai.sharer.action.TCPConnect;
import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ClientTableLabelProvider;
import com.sishuai.sharer.modules.ClientTreeContentProvider;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.Header;
import com.sishuai.sharer.modules.interfaces.ItemInfo;
import com.sishuai.sharer.modules.net.NetworkMgr;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */
/**
 * 主视图类了
 * @author 四帅
 *
 */
public class ClientView extends ViewPart {
	//主界面

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.sishuai.sharer.views.ClientView";

	private TreeViewer viewer;
	private TCPConnect tcpConnect;
	private IStatusLineManager statusline;
	private ChatDialog chatDialog;
	private OthLink othLink;

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public ClientView() {
	}
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		final Tree tree = viewer.getTree();
		tree.setHeaderVisible(true);
		TreeColumn treeColumn0 = new TreeColumn(tree, SWT.LEFT);
		treeColumn0.setWidth(10);
		TreeColumn treeColumn1 = new TreeColumn(tree, SWT.LEFT);
		//treeColumn1.setText("Moniker");
		treeColumn1.setWidth(150);
		
		TreeColumn treeColumn2 = new TreeColumn(tree, SWT.LEFT);
		//treeColumn2.setText("msg和状态");
		treeColumn2.setWidth(80);
		
		TreeColumn treeColumn3 = new TreeColumn(tree, SWT.LEFT);
		//treeColumn3.setText("file消息和编辑行数");
		treeColumn3.setWidth(80);
		
		TreeColumn treeColumn4 = new TreeColumn(tree, SWT.LEFT);
		//分页里面有新的消息
		//treeColumn4.setText("ip和update时间");
		treeColumn4.setWidth(180);
		
		TreeColumn treeColumn5 = new TreeColumn(tree, SWT.RIGHT);
		//treeColumn5.setText("交互文件数和文件大小");
		treeColumn5.setWidth(30);
		
		//默认使用udp广播
		NetworkMgr.getMulticastServer().run();
		
		ClientTreeContentProvider ctcp = new ClientTreeContentProvider();
		viewer.setContentProvider(ctcp);
		viewer.setLabelProvider(new ClientTableLabelProvider());
		
		viewer.setInput(ContentManager.getManager());
		ContentManager.getManager().setContentProvider(ctcp);
		
		viewer.setExpandedState(Header.getHeader(), true);
		
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				
				ISelection selection = viewer.getSelection();
				ItemInfo object = ((ItemInfo)((IStructuredSelection) selection).getFirstElement());
				if (object instanceof ClientInfo && ((ClientInfo)object).isConnected())
					;
			}
		});
		
		 viewer.addSelectionChangedListener(new ISelectionChangedListener()
         {
             public void selectionChanged(SelectionChangedEvent event)
             {
                 IStructuredSelection selection =
                     (IStructuredSelection) event.getSelection();
                 statusline = getViewSite().getActionBars().getStatusLineManager();
         		 //eclipse 下面的状态栏，哈哈，我们征用你了
                 Object obj = selection.getFirstElement();
                 if(obj == null || !(obj instanceof ClientInfo)) {
                	 tcpConnect.setEnabled(false);
                	 return;
                 }
                 tcpConnect.setEnabled(true);
             }
         });
		
		
		//拖拽操作
		//viewer.addDropSupport(operations, transferTypes, listener);
		
		createAction();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();

        //共享视图查看器中的内容
        getViewSite().setSelectionProvider(viewer);

	}
	
	private void createAction() {
		// TODO Auto-generated method stub
		TCPConnect.getTcpConnect().setView(this);
		tcpConnect = TCPConnect.getTcpConnect();
		
		OthLink.getOthLink().setView(this);
		othLink = OthLink.getOthLink();
		
		ChatDialog.getDialog().setView(this);
		chatDialog = ChatDialog.getDialog();
		
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ClientView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		//manager.add(new Separator());
	}
	
	public ItemInfo getSelectedItem()
    {
        IStructuredSelection selection = 
            (IStructuredSelection)viewer.getSelection();
        return ((ItemInfo)selection.getFirstElement());
    }


	private void fillContextMenu(IMenuManager manager) {
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(tcpConnect);
		manager.add(othLink);
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
	}


	private void hookDoubleClickAction() {
	}
	
	
	public void showMessage(String notification) {
		statusline.setMessage(notification);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}