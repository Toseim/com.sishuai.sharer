package com.sishuai.sharer.views;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

import com.sishuai.sharer.Activator;
import com.sishuai.sharer.action.ChatDialog;
import com.sishuai.sharer.action.DefaultName;
import com.sishuai.sharer.action.OpenView;
import com.sishuai.sharer.action.OthLink;
import com.sishuai.sharer.action.RegetIP;
import com.sishuai.sharer.action.TCPConnect;
import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ClientTableLabelProvider;
import com.sishuai.sharer.modules.ClientTreeContentProvider;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.FileInfo;
import com.sishuai.sharer.modules.Header;
import com.sishuai.sharer.modules.ImageMgr;
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

	private TreeViewer viewer;
	private TCPConnect tcpConnect;
	private IStatusLineManager statusline;
	private ChatDialog chatDialog;
	private OthLink othLink;
	private DefaultName changeName;
	private MulticastServer multicastServer;
	private RegetIP regetIP; 

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
		treeColumn5.setWidth(50);

		statusline = getViewSite().getActionBars()
				.getStatusLineManager();
		
		NetworkMgr.getMgr().setView(this);
		
		Logging.getLogger().setFileName("ClientView");
		Logging.info("Loading the contents ");
		viewer.setContentProvider(new ClientTreeContentProvider());
		viewer.setLabelProvider(new ClientTableLabelProvider());
		viewer.setInput(ContentManager.getMgr());
		ContentManager.getMgr().setTreeViewer(viewer);
		ContentManager.getMgr().setView(this);
		
		// 默认不展开根节点（为了获取用户的第一次双击)
		viewer.setExpandedState(Header.getHeader(), false);
		
		Logging.getLogger().setFileName("ClientView");
		
		createAction();
		addDoubleClickMonitor();
		hookContextMenu();
		contributeToActionBars();
		getName();
	}
	
	public void getName() {
		String name;
		if ((name = DefaultName.getInstance().fileInput()) == null) {
			viewer.addOpenListener(new IOpenListener() {
				@Override
				public void open(OpenEvent event) {
					// TODO Auto-generated method stub
					if (DefaultName.state) return;
					String nameInput;
					if ((nameInput = DefaultName.getInstance().viewInput()) == null)
						return;
					NetworkMgr.getMgr().setName(nameInput);
					addSelectionMonitor();
					multicastServer.setEnabled(true);
					changeName.setEnabled(true);
					othLink.setEnabled(true);
					NetworkMgr.getMgr().getDatagramSocket();
					viewer.removeOpenListener(this);
				}
			});
		} else {
			NetworkMgr.getMgr().setName(name);
			addSelectionMonitor();
			changeName.setEnabled(true);
			multicastServer.setEnabled(true);
			othLink.setEnabled(true);
			NetworkMgr.getMgr().getDatagramSocket();
		}
	}
	
	public void addDoubleClickMonitor() {
		Logging.info("Loading double click operation");
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object obj = selection.getFirstElement();
				if (obj instanceof FileInfo) {
					FileInfo fileInfo = (FileInfo) obj;
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("Share");
					IFile file = project.getFile(new Path("/src/LCCD/"+fileInfo.getFilename()));
					try {
						IDE.openEditor(page, file, "org.eclipse.jdt.ui.CompilationUnitEditor");
					} catch (PartInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void addSelectionMonitor() {
		Logging.getLogger().setFileName("ClientView");
		Logging.info("Loading selection operation..");
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
				if (obj instanceof ClientInfo && !((ClientInfo) obj).isConnected())
					tcpConnect.setEnabled(true);
				
				//建立新网络
				othLink.setEnabled(true);
				
				if (obj instanceof Header)
					statusline.setMessage(NetworkMgr.getMgr().getIPs().toString());
			}
		});
	}

	private void createAction() {
		// TODO Auto-generated method stub
		Logging.info("Loading operation context, drop-down bar..");
		tcpConnect = TCPConnect.getTcpConnect();
		tcpConnect.setView(this);

		othLink = OthLink.getOthLink();
		changeName = new DefaultName();
		regetIP = new RegetIP(this);

		chatDialog = new ChatDialog(this);
		multicastServer = MulticastServer.getMulticastServer();
		dropSupport();
		setDefaultState();
	}
	
	private void setDefaultState() {
		chatDialog.setEnabled(false);
		othLink.setEnabled(false);
		multicastServer.setEnabled(false);
		tcpConnect.setEnabled(false);
		changeName.setEnabled(false);
	}
	
	public void dropSupport() {
		//drop 的支持
		Logging.info("Adding drag and drop support..");
		int ops = DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget dropTarget = new DropTarget(viewer.getTree(), ops);
		final FileTransfer fileTransfer = FileTransfer.getInstance();
		Transfer[] transfers = new Transfer[] {fileTransfer};
		dropTarget.setTransfer(transfers);
		dropTarget.addDropListener(new DropTargetAdapter() {
			private ArrayList<File> fileList = new ArrayList<File>();
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
					String[] files = (String[]) event.data;
					boolean flag = true;
					for (int j = 0; j < files.length; j++) {
						fileList.clear();
						changeFile(files[j]);
						for(int i = 0 ; i < fileList.size(); i++) {
							if (event.item != null && event.item.getData() instanceof ClientInfo) {
								if (!flag) return;
								flag = ((ClientInfo)event.item.getData()).sendFile(fileList.get(i).getAbsolutePath());
							}
						}
					}
				}
			}
			
			private void changeFile(String files) {
				File temp = new File(files);
				if(temp.isFile()&&temp.getAbsolutePath().endsWith(".java")) {
					fileList.add(temp);
				}else if(temp.isDirectory()){
					File[] floder = temp.listFiles();
					for(int i = 0 ; i <floder.length;i++) {
						changeFile(floder[i].getAbsolutePath());
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
		manager.add(regetIP);
		manager.add(multicastServer);
		manager.add(new Separator());
		manager.add(changeName);
	}

	public ItemInfo getSelectedItem() {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		return ((ItemInfo) selection.getFirstElement());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(chatDialog);
		manager.add(new Separator());
		manager.add(tcpConnect);
		manager.add(othLink);
	}

	private void fillLocalToolBar(IToolBarManager manager) {
	}

	public void showMessage(String notification) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						statusline.setMessage(notification);
					}
				});
			}
		}).start();
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		OpenView.isOpen = false;
		ImageMgr.getInstance().dispose();
		NetworkMgr.dispose();
		Logging.dispose();
		Activator.pluginDispose();
	}
}
