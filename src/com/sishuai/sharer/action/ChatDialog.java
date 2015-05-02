package com.sishuai.sharer.action;

import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.sishuai.sharer.Activator;
import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.modules.ImageMgr;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.views.ClientView;
/**
 * 用户之间的聊天窗口
 * @author 四帅
 *
 */
public class ChatDialog extends Action{
	private ClientView view;
	private Text dialogText;
	private Text text;
	private Group group1;
	private Group group2;
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
		clientInfo = (ClientInfo) view.getSelectedItem();
		if (clientInfo.isDialogOpened()) {
			return;
		}
		clientInfo.setChatDialog(this);
		Display display = Display.getDefault();
		Shell shell = new Shell(display,SWT.MIN);
		shell.setBounds((Activator.width-width)/2, (Activator.height-height)/2, width, height);
		shell.open();
		shell.setText(clientInfo.getName());
		
		Color createdblue = new Color(display,0,0,255);

		group1 = new Group(shell,SWT.NONE);
		group1.setText("Chatting Area");
		group1.setBounds(10,0,572,330);
		
		dialogText = new Text(group1,SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		dialogText.setText("");
		dialogText.setEditable(false);
		dialogText.setBounds(10,22, 555, 297);
		
		final Label line1 = new Label(shell,SWT.SEPARATOR|SWT.HORIZONTAL);
		line1.setBounds(0,333,600,10);
		
		final Label line2 = new Label(shell,SWT.SEPARATOR|SWT.HORIZONTAL);
		line2.setBounds(0,370,600,10);

		group2 = new Group(shell, SWT.NONE);
		group2.setText("Input area");
		group2.setBounds(10,375,572,70);
				
		Image image1 = ImageMgr.getInstance().getImage(Activator.getImageDescriptor(ImageMgr.IMAGE_FACE));
		Image image2 = ImageMgr.getInstance().getImage(Activator.getImageDescriptor(ImageMgr.IMAGE_SHOOT));
		Image image3 = ImageMgr.getInstance().getImage(Activator.getImageDescriptor(ImageMgr.IMAGE_BLANK));

		final CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		coolBar.setBounds(8,339,563,30);
		
		final CoolItem coolItem1 = new CoolItem(coolBar, SWT.PUSH);
		final Button button1 = new Button(coolBar, SWT.NONE);
		button1.setImage(image1);
		button1.setSize(28,28);
		coolItem1.setControl(button1);
		coolItem1.setSize(coolItem1.computeSize(28,28));
		
		final CoolItem coolItem2 = new CoolItem(coolBar, SWT.PUSH);
		final Button button2 = new Button(coolBar, SWT.NONE);
		button2.setImage(image2);
		button2.setSize(28,28);
		coolItem2.setControl(button2);
		coolItem2.setSize(coolItem2.computeSize(27,27));
		
		final CoolItem coolItem3 = new CoolItem(coolBar, SWT.PUSH);
		final Button button3 = new Button(coolBar, SWT.NONE);
		button3.setImage(image3);
		button3.setSize(27,27);
		coolItem3.setControl(button3);
		coolItem3.setSize(coolItem3.computeSize(27,27));
		
		text = new Text(group2, SWT.NONE);
		text.setText("");
		text.setBounds(10,22,490,37);
		text.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent event) {
				dialogText.append(clientInfo.getName()+": /n"+text.getText()+"\n");
				text.setText("");
				send(text.getText());
			}
		});
	
		final Button sendButton = new Button(group2, SWT.PUSH);
		sendButton.setText("发送");
		sendButton.setBounds(510,20,50,40);
		
		
		sendButton.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e){
				dialogText.setForeground(createdblue);
				dialogText.append(clientInfo.getName()+": /n"+text.getText()+"\n");
				text.setText("");
				
				send(text.getText());
			}
		});
		
		while(!shell.isDisposed()){
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		createdblue.dispose();
		shell.dispose();
	}
	
	public void send(String string) {
		try {
			DataOutputStream dos = clientInfo.getDataOutputStream();
			dos.writeUTF(string);
			dos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//用户已经失去连接
			NetworkMgr.getMgr().disconnect(clientInfo);
		}
	}
	
}
