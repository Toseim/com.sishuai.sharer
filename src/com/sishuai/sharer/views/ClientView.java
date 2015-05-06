package com.sishuai.sharer.views;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import com.sishuai.sharer.modules.net.MulticastServer;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.util.Logging;

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
 * 
 * @author 四帅
 *
 */
public class ClientView extends ViewPart {
	// 主界面

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.sishuai.sharer.views.ClientView";

	private boolean getNameFlag = false;
	
	private TreeViewer viewer;
	private TCPConnect tcpConnect;
	private IStatusLineManager statusline;
	private ChatDialog chatDialog;
	private OthLink othLink;
	private MulticastServer multicastServer;
//	private Transport transport = new Transport();

	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public ClientView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		
		viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		final Tree tree = viewer.getTree();
		tree.setHeaderVisible(true);
		TreeColumn treeColumn0 = new TreeColumn(tree, SWT.LEFT);
		treeColumn0.setWidth(10);
		TreeColumn treeColumn1 = new TreeColumn(tree, SWT.LEFT);
		// treeColumn1.setText("Moniker");
		treeColumn1.setWidth(150);

		TreeColumn treeColumn2 = new TreeColumn(tree, SWT.LEFT);
		// treeColumn2.setText("msg和状态");
		treeColumn2.setWidth(80);

		TreeColumn treeColumn3 = new TreeColumn(tree, SWT.LEFT);
		// treeColumn3.setText("file消息和编辑行数");
		treeColumn3.setWidth(80);

		TreeColumn treeColumn4 = new TreeColumn(tree, SWT.LEFT);
		// 分页里面有新的消息
		// treeColumn4.setText("ip和update时间");
		treeColumn4.setWidth(180);

		TreeColumn treeColumn5 = new TreeColumn(tree, SWT.RIGHT);
		// treeColumn5.setText("交互文件数和文件大小");
		treeColumn5.setWidth(30);

		// eclipse 下面的状态栏，哈哈，我们征用你了
		statusline = getViewSite().getActionBars()
				.getStatusLineManager();
		
		viewer.addOpenListener(new IOpenListener() {
			@Override
			public void open(OpenEvent event) {
				// TODO Auto-generated method stub
				if (getNameFlag) return;
				getNameFlag = true;
				NetworkMgr.getMgr().setName(new DefaultName().getName());
				if (NetworkMgr.getMgr().getName()==null)
					return;
				addSelectionMonitor();
				multicastServer.setEnabled(true);
				viewer.removeOpenListener(this);
				getNameFlag = false;
			}
		});
		Logging.getLogger().setFileName("ClientView");
		Logging.info("内容装填中...");
		viewer.setContentProvider(new ClientTreeContentProvider());
		viewer.setLabelProvider(new ClientTableLabelProvider());
		viewer.setInput(ContentManager.getManager());
		ContentManager.getManager().setTreeViewer(viewer);
		NetworkMgr.getMgr().getDatagramSocket(); //初始化udp隐藏的，始终打开的端口
		//默认不展开根节点（为了获取用户的第一次双击)
		viewer.setExpandedState(Header.getHeader(), false);
		
		createAction();
		hookContextMenu();
		contributeToActionBars();

		// 共享视图查看器中的内容
		getViewSite().setSelectionProvider(viewer);
	}

	public void addSelectionMonitor() {
		Logging.getLogger().setFileName("ClientView");
		Logging.info("加载选择操作。。");
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				
				Object obj = selection.getFirstElement();
				
				//打开对话框
				chatDialog.setEnabled(false);
				if (obj instanceof ClientInfo && ((ClientInfo) obj).isConnected()
						&& !((ClientInfo) obj).isDialogOpened())
					chatDialog.setEnabled(true);
				
				othLink.setEnabled(false);
				tcpConnect.setEnabled(false);
				if (NetworkMgr.getState()) return;
				//加入该网络
				if (obj instanceof ClientInfo
						&& !((ClientInfo) obj).isConnected())
					tcpConnect.setEnabled(true);
				
				//建立新网络
				othLink.setEnabled(true);
			}
		});
	}

	private void createAction() {
		// TODO Auto-generated method stub
		Logging.info("装载上下文，下拉栏操作。。");
		TCPConnect.getTcpConnect().setView(this);
		tcpConnect = TCPConnect.getTcpConnect();

		OthLink.getOthLink().setView(this);
		othLink = OthLink.getOthLink();

		chatDialog = new ChatDialog(this);
		multicastServer = NetworkMgr.getMgr().getMulticastServer();
		
		dropSupport();
		setDefaultState();
	}
	
	private void setDefaultState() {
		chatDialog.setEnabled(false);
		othLink.setEnabled(false);
		multicastServer.setEnabled(false);
		tcpConnect.setEnabled(false);
	}
	
	public void dropSupport() {
		//drop 的支持
		Logging.info("添加拖拽支持。。");
		int ops = DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget dropTarget = new DropTarget(viewer.getTree(), ops);
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] transfers = new Transfer[] {fileTransfer};
		dropTarget.setTransfer(transfers);
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						//if (((ClientInfo)event.item.getData()).isConnected()) 
							event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
			}
			public void drop(DropTargetEvent event) {
				if (fileTransfer.isSupportedType(event.currentDataType)) {
					System.out.println(((ClientInfo)event.item.getData()).getIp());
					System.out.println(event.widget);
					String[] files = (String[]) event.data;
					for (int i = 0; i < files.length; i++) {
						System.out.println(files[i]);
					}
				}
			}
		});
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
		// manager.add(new Separator());
		manager.add(multicastServer);
		manager.add(othLink);
//		manager.add(transport);
	}

	public ItemInfo getSelectedItem() {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		return ((ItemInfo) selection.getFirstElement());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(chatDialog);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		manager.add(tcpConnect);
		manager.add(othLink);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
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
