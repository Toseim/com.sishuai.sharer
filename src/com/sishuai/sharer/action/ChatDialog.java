package com.sishuai.sharer.action;


import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.sishuai.sharer.Activator;
import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ImageMgr;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.util.Logging;
import com.sishuai.sharer.views.ClientView;
/**
 * 用户之间的聊天窗口
 * @author 四帅
 *
 */
public class ChatDialog extends Action{
	private ClientView view;
	private Text dialogText;
	private Text text_2;
	private Font font;
	private Group group_1;
	private Group group_3;
	private ClientInfo clientInfo;
	private static final int height = 500;
	private static final int width = 600;
	
	public ChatDialog(ClientView clientView) {
		super("打开对话框");
		this.view = clientView;
	}
	public Text getDialog() {
		return dialogText;
	}
	
	public void run() {
		Logging.getLogger().setFileName("ChatDialog");
		clientInfo = (ClientInfo) view.getSelectedItem();
		if (clientInfo.isDialogOpened()) {
			Logging.warning("与"+clientInfo.getName()+"的对话窗口已经打开");
			return;
		}
		if (clientInfo.getChatDialog() == null) {
			Logging.info("为"+clientInfo.getName()+"设置对话窗口");
			clientInfo.setChatDialog(this);
		}
		Logging.info(clientInfo.getName()+"的窗口打开中");
		clientInfo.setDialogOpened(true);
		
		Display display = Display.getDefault();
		Shell shell = new Shell(display,SWT.MIN);
		final GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 5;
		shell.setLayout(gridLayout);
		shell.setText(clientInfo.getName());
		shell.setBounds((Activator.width-width)/2, (Activator.height-height)/2, width, height);
		shell.setSize(width, height);
		shell.setVisible(true);
		shell.open();
		
		font = new Font(display, "微软雅黑", 11, SWT.NONE);
		// 第一行
		group_1 = new Group(shell, SWT.NONE);
		group_1.setText("Chatting Area");

		final GridData gridData_1 = new GridData(SWT.FILL, SWT.FILL, true,false, 5, 1);
		gridData_1.heightHint = 300;
		int group_height = gridData_1.heightHint;

		group_1.setLayoutData(gridData_1);

		dialogText = new Text(group_1, SWT.MULTI | SWT.WRAP| SWT.V_SCROLL);
		dialogText.setBounds(10, 22, 555, group_height - 10);
		dialogText.setFont(font);
		dialogText.setText(clientInfo.getTempString());
		dialogText.setEditable(false);
		// 如何让他填满group_1
		
		
		// 第二行
		Image image1 = ImageMgr.getInstance().getImage(Activator.getImageDescriptor(ImageMgr.IMAGE_FACE));
		Image image2 = ImageMgr.getInstance().getImage(Activator.getImageDescriptor(ImageMgr.IMAGE_SHOOT));

		final Composite composite_2 = new Composite(shell, SWT.NONE);

		final Label line1 = new Label(composite_2, SWT.SEPARATOR| SWT.HORIZONTAL);
		line1.setBounds(0, 0, 600, 1);

		final ToolBar toolBar = new ToolBar(composite_2, SWT.FLAT | SWT.WRAP);
		toolBar.setBounds(0, 0, 600, 35);

		final ToolItem toolItem_1 = new ToolItem(toolBar, SWT.PUSH);
		toolItem_1.setImage(image1);

		final ToolItem toolItem_2 = new ToolItem(toolBar, SWT.PUSH);
		toolItem_2.setImage(image2);

		final Label line2 = new Label(composite_2, SWT.SEPARATOR| SWT.HORIZONTAL);
		line2.setBounds(0, 35, 600, 1);

		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 5, 1));
		//final GridData gridData_2 = new GridData(SWT.FILL, SWT.CENTER, true,false, 5, 1);

		//第三组
		group_3 = new Group(shell, SWT.NONE);
		group_3.setText("Inputting area");

		text_2 = new Text(group_3, SWT.NONE);
		text_2.setText("");
		text_2.setFont(font);
		text_2.setBounds(10, 23, 458, 42);
		text_2.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent event) {
				if (text_2.getText().length() == 0) return; 
				dialogText.append(NetworkMgr.getMgr().getName()+": \n"+text_2.getText()+"\n");
				Logging.info("传送text_2消息到"+clientInfo.getName());
				send(text_2.getText());
				text_2.setText("");
			}
		});
	
		final GridData gridData_3 = new GridData(SWT.FILL,SWT.FILL,true,true,5,1);
		group_3.setLayoutData(gridData_3);
		
		final Button sendButton = new Button(group_3, SWT.NONE);
			
		sendButton.setLayoutData(gridData_3);
		sendButton.setText("发送");
		//sendButton.setFont(font);
		sendButton.setBounds(482,23,80,42);
		
		sendButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if (text_2.getText().length() == 0) return;
				
				dialogText.append(NetworkMgr.getMgr().getName()+": \n"+text_2.getText()+"\n");
				Logging.info("传送text_2消息到"+clientInfo.getName());
				send(text_2.getText());
				text_2.setText("");
			}
		});
		
		shell.layout();
		
		while(!shell.isDisposed()){
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		Logging.info("内存释放中...");
		
		shell.dispose();
		Logging.info("与"+clientInfo.getName()+"的对话窗口已关闭");
		clientInfo.setDialogOpened(false);
	}
	
	public void send(String string) {
		try {
			DataOutputStream dos = clientInfo.getDataOutputStream();
			dos.writeUTF(NetworkMgr.getMgr().getName()+": \n"+string+"\n");
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//用户已经失去连接
			Logging.warning("消息传输失败，对方已关闭端口...");
			clientInfo.disconnect();
			MessageDialog.openError(view.getSite().getShell(), "消息传输失败", "对方已关闭端口...");
		}
	}
}
