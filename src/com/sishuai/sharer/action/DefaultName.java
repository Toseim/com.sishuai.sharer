package com.sishuai.sharer.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.eclipse.jface.action.Action;
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
 * 获得用户的设置的名字
 * @author 四帅
 * clear!
 */
public class DefaultName extends Action{
	
	public static boolean state = false;
	private static final Pattern namePattern = Pattern.compile("^[\u4e00-\u9fa5 _a-zA-Z]+");
	private Text text;
	private File file = Activator.getDefault().getStateLocation().append("default.ini").toFile();
	private String name = null;
	private final int height = 138;
	private final int width = 418;
	private static DefaultName instance;
	
	public static DefaultName getInstance() {
		if (instance == null)
			instance = new DefaultName();
		return instance;
	}
	
	public DefaultName() {
		super("change name");
	}
	
	public String fileInput() {
		Logging.getLogger().setFileName("DefaultName");
		BufferedReader br = null;
		try {
			if (file.exists()) {
				Logging.info("Reading the preset name ");
				br = new BufferedReader(new FileReader(file));
				name = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Logging.warning("Failed to read for the configuration file is missing");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logging.fatal("Failed to read from a configuration file");
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if (name != null && name.length() == 0) name = null;
		return name;
	}
	
	public void saveName() {
		Logging.info("Saveing the user name to a file.");
		BufferedWriter bw = null;
		if (!file.exists())
			try {
				Logging.info("New the user's file  " + file.toString());
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
			Logging.fatal("Failed to write , user name cannot be saved");
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
	
	public String viewInput() {
		state = true;
		Display display = Display.getDefault();
		Shell shell = new Shell(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.ON_TOP);
		shell.setText("Welcome to use our plugin ! ");
		shell.setBounds((Activator.width-width)/2, (Activator.height-height)/2+100, width, height);
		
		Label lblNewLabel = new Label(shell, SWT.WRAP | SWT.CENTER);
		lblNewLabel.setBounds(10, 0, 383, 38);
		lblNewLabel.setText("Set your name to make yourself distinguished.\r\nYou can change it afterwards through settings.");
		
		Button btnCheckButton = new Button(shell, SWT.CHECK);
		btnCheckButton.setToolTipText("When you use this plug-in again,this name is default.");
		btnCheckButton.setSelection(true);
		btnCheckButton.setBounds(10, 77, 98, 17);
		btnCheckButton.setText("set it to default");
		
		text = new Text(shell, SWT.BORDER);
		text.setToolTipText("You can set it freely,just let others recognize you.");
		text.setBounds(60, 39, 281, 23);
		text.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				name = text.getText();
				Logging.info("Getting the name of user "+ name);
				if (btnCheckButton.getSelection()) saveName();
				shell.dispose();
			}
		});
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.setBounds(296, 68, 80, 27);
		btnNewButton.setText("OK");
		btnNewButton.setEnabled(false);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				name = text.getText();
				Logging.info("Getting the name of user "+ name);
				if (btnCheckButton.getSelection()) saveName();
				shell.dispose();
			}
		});
		
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				// TODO Auto-generated method stub
				btnNewButton.setEnabled(false);
				if (namePattern.matcher(text.getText()).matches()) {
					btnNewButton.setEnabled(true);
				}
			}
		});
		shell.open();
		
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
		shell.dispose();
		state = false;
		return name;
	}
	
	public void run() {
		NetworkMgr.getMgr().setName(viewInput());
	}
}
