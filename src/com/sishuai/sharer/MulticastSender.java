package com.sishuai.sharer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author 四帅
 * 用来单机测试的类，可以随便改
 */
public class MulticastSender {
	public static void main(String[] args) throws Exception {
		Display display = Display.getDefault();
		Shell shell = new org.eclipse.swt.widgets.Shell(display);
		shell.open();
		MessageBox messageBox = new MessageBox(shell, SWT.CANCEL | SWT.OK);
		messageBox.setMessage("aidjfiasjdfhaisdjfihi");
		messageBox.setText("asuidf");
		messageBox.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}
