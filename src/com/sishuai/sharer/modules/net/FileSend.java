package com.sishuai.sharer.modules.net;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.sishuai.sharer.modules.ClientInfo;

public class FileSend {
	private ClientInfo clientInfo;
	private String filePath;
	
	public FileSend(ClientInfo clientInfo, String filePath) {
		this.clientInfo = clientInfo;
		this.filePath = filePath;
	}
	public void send() {
		DataOutputStream dos = clientInfo.getDataOutputStream();
		try {
			FileInputStream fis = new FileInputStream(new File(filePath));
			int count = 0;
			byte[] buf = new byte[1024];
			while ((fis.read(buf, 0, buf.length) != -1)) {
				count ++;
				dos.write(buf, 0, buf.length);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			Logging.fatal("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			Logging.fatal();
		}
	}
}
