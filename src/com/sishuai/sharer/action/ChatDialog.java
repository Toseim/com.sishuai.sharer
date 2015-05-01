package com.sishuai.sharer.action;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.sishuai.sharer.Activator;
import com.sishuai.sharer.modules.ClientInfo;
import com.sishuai.sharer.views.ClientView;
/**
 * 用户之间的聊天窗口
 * @author 四帅
 *
 */
public class ChatDialog extends Action{
	private ClientView view;
	private Text dialogText;
	private Group group;
	private ClientInfo clientInfo;
	private static final int size = 600;
	
	public ChatDialog(ClientView clientView) {
		super("打开对话框");
		this.view = clientView;
	}
	public Text getDialog() {
		return dialogText;
	}
	
	public void run() {
		clientInfo = (ClientInfo) view.getSelectedItem();
		clientInfo.setChatDialog(this);
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.MIN);
		shell.setBounds((Activator.width-size)/2, (Activator.height-size)/2, size, size);
		shell.open();
		shell.setText("ID of the other one");//对话框名称
		
		
		//自己创建一个字体color
		Color createdblue = new Color(display,0,0,255);
		//从display中取得一个系统color
		Color sysytemBlack = display.getSystemColor(SWT.COLOR_BLACK);
		
		
		//group对象可以把相关的控件包成一组，在这一组控件外会有边框将他们和其他控件隔离开来
		group = new Group(shell,SWT.NONE);
		group.setText("Chatting Area");
		group.setBounds(10,0,562,330);
		
		//聊天显示框
		dialogText = new Text(group,SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);//多行文本框才能显示换行转义符
		dialogText.setText("");
		dialogText.setBounds(10,22, 542, 297);
		
		//功能按钮图标
		//Image image = new Image(display, DialogBox.class.getResourceAsStream("1.gif"));
		
		//功能选择按钮
		final CoolBar coolBar = new CoolBar(shell, SWT.NONE);
		coolBar.setBounds(8,339,563,30);
		
		final CoolItem coolItem1 = new CoolItem(coolBar, SWT.PUSH);
		final Button button1 = new Button(coolBar, SWT.NONE);
		//button1.setImage(image);
		button1.setSize(27,27);
		coolItem1.setControl(button1);//setControl方法向coolItem中添加一个button
		coolItem1.setSize(coolItem1.computeSize(27,27));
		
		
		final CoolItem coolItem2 = new CoolItem(coolBar, SWT.PUSH);
		final Button button2 = new Button(coolBar, SWT.NONE);
		//button2.setImage(image);
		button2.setSize(27,27);
		coolItem2.setControl(button2);//setControl方法向coolItem中添加一个button
		coolItem2.setSize(coolItem2.computeSize(27,27));
		
		final CoolItem coolItem3 = new CoolItem(coolBar, SWT.PUSH);
		final Button button3 = new Button(coolBar, SWT.NONE);
		//button3.setImage(image);
		button3.setSize(27,27);
		coolItem3.setControl(button3);//setControl方法向coolItem中添加一个button
		coolItem3.setSize(coolItem3.computeSize(27,27));
		
		final CoolItem coolItem4 = new CoolItem(coolBar, SWT.PUSH);
		final Button button4 = new Button(coolBar, SWT.NONE);
		//button4.setImage(image);
		button4.setSize(27,27);
		coolItem4.setControl(button4);//setControl方法向coolItem中添加一个button
		coolItem4.setSize(coolItem4.computeSize(27,27));
		
		
		//聊天输入框-wrap式样会在文字超过控件宽度时自动换行 此时如果有H_SCROLL式样会被忽略
		Text text = new Text(shell, SWT.V_SCROLL | SWT.BORDER | SWT.WRAP | SWT.H_SCROLL);
		text.setBounds(10,380,560,110);
		text.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == 13) {
					text.setText("");
				}
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.keyCode == 13) {
					String sendMessage = "Message:\n"+ text.getText();
					dialogText.append("   "+sendMessage+"\n");//该方法不会覆盖掉原来的字符 与setText不同
					text.setText("");
					System.out.println("Enter key is pressed");
				}
			}
		});
		//发送按钮
		final Button sendButton = new Button(shell, SWT.PUSH);
		sendButton.setText("发送");
		sendButton.setBounds(420,500,120,36);
		
		
		//监听发送按钮
		sendButton.addSelectionListener(new SelectionAdapter() {//这是一个空实现 可以重载开发者感兴趣的方法
			
			//方法名称是固定的
			public void widgetSelected(SelectionEvent e){
				//对按下Button做出反应
				dialogText.setForeground(createdblue);//设置前景色 即字体颜色
				String sendMessage1 = "   Message:\n";
				String sendMessage2 = text.getText();
				dialogText.append(sendMessage1);//该方法不会覆盖掉原来的字符 与setText不同
				dialogText.setForeground(sysytemBlack);
				dialogText.append(sendMessage2+"\n");
				
				text.setText("");
				
				System.out.println("Send button  Selected");
			}
		});
	}
}
