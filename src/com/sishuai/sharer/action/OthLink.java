package com.sishuai.sharer.action;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.sishuai.sharer.Activator;
import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ContentManager;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.views.ClientView;

/**
 * @author 四帅
 * 此菜单选项适用于局域网广播禁用的情况，提供手动添加ip地址的功能
 */
public class OthLink extends Action {
	private static OthLink othLink;
	private static ServerSocket serverSocket;
	private ClientView view;
	private String objectIP;
	private boolean state = false;
	private static final int height = 127;
	private static final int width = 333;
	
	public OthLink(String text) {
		super(text);
	}
	public static OthLink getOthLink() {
		if (othLink == null)
			othLink = new OthLink("添加新网络");
		return othLink;
	}
	public void setView(ClientView clientView) {
		this.view = clientView;
	}
	
	public void run() {
		if (state) {
			MessageDialog.openError(view.getSite().getShell(), "Warning", "你已经打开这个窗口了");
			return;
		}
		state = true;
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.DIALOG_TRIM);
		//居中显示
		shell.setBounds((Activator.width-width)/2, (Activator.height-height)/2, width, height);
		shell.setLayout(null);
		
		shell.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent arg0) {
				// TODO Auto-generated method stub
				shell.dispose();
				state = false;
			}
		});
		Text text = new Text(shell, SWT.BORDER);
		text.setBounds(80, 19, 215, 24);
		
		Label lblNewLabel = new Label(shell, SWT.WRAP | SWT.SHADOW_IN | SWT.CENTER);
		//lblNewLabel.setFont(SWTResourceManager.getFont("Microsoft JhengHei UI", 14, SWT.NORMAL));
		lblNewLabel.setBounds(10, 19, 74, 24);
		lblNewLabel.setText("IP地址");
		
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(117, 55, 80, 27);
		btnNewButton.setText("确定");
		btnNewButton.setEnabled(false);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btnNewButton.isEnabled()) {
					objectIP = text.getText();
					shell.setVisible(false);
					shell.dispose();
					next();
				}
			}
		});
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setBounds(215, 55, 80, 27);
		btnNewButton_1.setText("取消");
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				shell.setVisible(false);
				state = false;
			}
		});
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				// TODO Auto-generated method stub
				if (NetworkMgr.pattern1.matcher(text.getText()).matches() ||
						NetworkMgr.pattern2.matcher(text.getText()).matches() ||
						NetworkMgr.pattern3.matcher(text.getText()).matches()) {
					btnNewButton.setEnabled(true);
				} else {
					btnNewButton.setEnabled(false);
				}
			}
		});
		
		text.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				if (btnNewButton.isEnabled()) {
					objectIP = text.getText();
					System.out.println(text.getText());
					shell.setVisible(false);
					shell.dispose();
					next();
				}
			}
		});
		
		shell.open();
	}
	
	public boolean getState() {
		return state;
	}
	
	public void next() {
		if (objectIP == null)
			return;

		serverSocket = NetworkMgr.getManager().getServersocket();
		NetworkMgr.getManager().attempLink(objectIP);
		view.showMessage("等待对面的用户想到一块去");
		ConnectionThread ct = new ConnectionThread();
		new Thread(ct).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(30000);
					ct.socket.close();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		state = false;
	}
	
	class ConnectionThread implements Runnable {
		volatile ServerSocket socket = OthLink.serverSocket;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				//处理超时的操作
				Socket socket = serverSocket.accept();
		
				//连接后取消计时
				ClientInfo clientInfo = new ClientInfo(objectIP, "null"); //名字的获取方式暂定
				clientInfo.setSocket(socket);
				clientInfo.setConnected(true);
				
				//试一下
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						//加入树结构
						ContentManager.getManager().addItem(clientInfo, null);
						// TODO Auto-generated method stub
						view.showMessage("We connect!");
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						MessageDialog.openError(view.getSite().getShell(), "TIME OUT!", "连接超时，对方未响应");
						view.showMessage("");
					}
				});
			}
		}
	}
}
