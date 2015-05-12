package com.sishuai.sharer.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.sishuai.sharer.Activator;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.util.Logging;

/**
 * @author 四帅
 * 此菜单选项适用于局域网广播禁用的情况，提供手动添加ip地址的功能
 */
public class OthLink extends Action {
	private static OthLink othLink;
	private String objectIP;
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
	
	public void run() {
		Logging.getLogger().setFileName("OthLink");
		Logging.info("打开用户自定义网络输入框");
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.ON_TOP);
		//居中显示
		shell.setBounds((Activator.width-width)/2, (Activator.height-height)/2, width, height);
		shell.setLayout(null);
		
		Text text = new Text(shell, SWT.BORDER);
		text.setBounds(80, 19, 215, 24);
		
		Label lblNewLabel = new Label(shell, SWT.WRAP | SWT.SHADOW_IN | SWT.CENTER);
		lblNewLabel.setBounds(10, 21, 70, 24);
		lblNewLabel.setText("IP地址");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(117, 55, 80, 27);
		btnNewButton.setText("确定");
		btnNewButton.setEnabled(false);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btnNewButton.isEnabled()) {
					if (NetworkMgr.getState()) {
						MessageDialog.openWarning(shell, "连接受堵", "网络正在被占用，请稍后再试");
						return;
					}
					objectIP = text.getText();
					Logging.info("获得用户输入的IP:"+objectIP);
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
				Logging.info("自定义输入IP地址已取消");
				shell.setVisible(false);
				shell.dispose();
				NetworkMgr.setState(false);
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
					if (NetworkMgr.getState()) {
						MessageDialog.openWarning(shell, "连接受堵", "网络正在被占用，请稍后再试");
						return;
					}
					objectIP = text.getText();
					Logging.info("获得用户输入的IP:"+objectIP);
					shell.setVisible(false);
					shell.dispose();
					next();
				}
			}
		});
		
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		Logging.info("自定义网络窗口已关闭");
		shell.dispose();
	}
	
	public void next() {
		if (objectIP == null)
			return;
		NetworkMgr.getMgr().attempLink(objectIP);
	}
}
