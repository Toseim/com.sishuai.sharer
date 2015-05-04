package com.sishuai.sharer.views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.sishuai.sharer.Activator;

public class DefaultName {
	private Text text;
	private File file = Activator.getDefault().getStateLocation().append("default.ini").toFile();
	private String name = null;
	private final int height = 138;
	private final int width = 418;
	
	public String getName() {
		BufferedReader br = null;
		try {
			if (file.exists()) {
				br = new BufferedReader(new FileReader(file));
				name = br.readLine();
				System.out.println("name  found" + "\t" + name);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if (name != null && name.length()>0) return name;
		System.out.println("show view");
		return showView();
	}
	
	public void saveName() {
		BufferedWriter bw = null;
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(name);
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public String showView() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.RESIZE);
		shell.setText("欢迎使用我们的插件");
		shell.setBounds((Activator.width-width)/2, (Activator.height-height)/2+100, width, height);
		
		Label lblNewLabel = new Label(shell, SWT.WRAP | SWT.CENTER);
		lblNewLabel.setBounds(10, 0, 383, 38);
		lblNewLabel.setText("使用本插件，你需要设置你的昵称，用来在网络上相互识别。\r\n之后你也可以通过配置修改你的设置");
		
		Button btnCheckButton = new Button(shell, SWT.CHECK);
		btnCheckButton.setToolTipText("以后你再次使用本插件，就默认使用这个昵称");
		btnCheckButton.setSelection(true);
		btnCheckButton.setBounds(10, 77, 98, 17);
		btnCheckButton.setText("设置成默认");
		
		text = new Text(shell, SWT.BORDER);
		text.setToolTipText("你可以随意设置，让别人知道你就好了");
		text.setBounds(60, 39, 281, 23);
		text.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				if (text.getText().length() > 0)
					name = text.getText();
				if (btnCheckButton.getSelection()) saveName();
					shell.dispose();
			}
		});
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(296, 68, 80, 27);
		btnNewButton.setText("确认");
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (text.getText().length() > 0)
					name = text.getText();
				if (btnCheckButton.getSelection()) saveName();
				shell.dispose();
			}
		});
		shell.open();
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		shell.dispose();
		if (name != null) return name.trim();
		else return null;
	}
}
