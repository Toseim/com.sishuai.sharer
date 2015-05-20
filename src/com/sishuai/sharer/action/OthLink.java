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
import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.util.Logging;

/**
 * @author 鍥涘竻
 * 姝よ彍鍗曢�夐」閫傜敤浜庡眬鍩熺綉骞挎挱绂佺敤鐨勬儏鍐碉紝鎻愪緵鎵嬪姩娣诲姞ip鍦板潃鐨勫姛鑳�
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
			othLink = new OthLink("add new link");
		return othLink;
	}
	
	public void run() {
		Logging.getLogger().setFileName("OthLink");
		Logging.info("Open the user network input box");
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.ON_TOP);
		//灞呬腑鏄剧ず
		shell.setBounds((Activator.width-width)/2, (Activator.height-height)/2, width, height);
		shell.setLayout(null);
		
		Text text = new Text(shell, SWT.BORDER);
		text.setBounds(80, 19, 215, 24);
		
		Label lblNewLabel = new Label(shell, SWT.WRAP | SWT.SHADOW_IN | SWT.CENTER);
		lblNewLabel.setBounds(10, 21, 70, 24);
		lblNewLabel.setText("IP address");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(117, 55, 80, 27);
		btnNewButton.setText("Ok");
		btnNewButton.setEnabled(false);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (btnNewButton.isEnabled()) {
					if (NetworkMgr.getState()) {
						MessageDialog.openWarning(shell, "Connection blocking", "The network is being used, please try again later");
						return;
					}
					objectIP = text.getText();
					if (ClientInfo.getIPList().contains(objectIP))
						return;
					Logging.info("Get the user input IP :"+objectIP);
					shell.setVisible(false);
					shell.dispose();
					next();
				}
			}
		});
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.setBounds(215, 55, 80, 27);
		btnNewButton_1.setText("Cancel");
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				Logging.info("Custom input IP address has been canceled");
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
						MessageDialog.openWarning(shell, "Connection blocking", "The network is being used, please try again later");
						return;
					}
					objectIP = text.getText();
					if (ClientInfo.getIPList().contains(objectIP))
						return;
					Logging.info("Get the user input IP:"+objectIP);
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
		Logging.info("The user window has closed network");
		shell.dispose();
	}
	
	public void next() {
		if (objectIP == null)
			return;
		NetworkMgr.getMgr().attempLink(objectIP);
	}
}
