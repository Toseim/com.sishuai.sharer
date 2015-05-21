package com.sishuai.sharer.modules.net.msg;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.sishuai.sharer.modules.interfaces.Msg;
import com.sishuai.sharer.modules.net.NetworkMgr;
import com.sishuai.sharer.util.Logging;

public class RefuseMsg implements Msg{
	private static final int msgType = MSG_REFUSE;
	private String IP;
	
	public RefuseMsg() {}
	
	public RefuseMsg(String IP) {
		// TODO Auto-generated constructor stub
		this.IP = IP;
	}
	
	@Override
	public void send(Object other, int port) {
		// TODO Auto-generated method stub
		Logging.getLogger().setFileName("RefuseMsg");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(msgType);
			byte[] buf = baos.toByteArray();
			
			DatagramPacket dp = new DatagramPacket(buf, 0, buf.length, 
					new InetSocketAddress(IP, NetworkMgr.getMgr().getUDPport()));
			
			NetworkMgr.getMgr().createTempSend(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (dos != null)
				try {
					dos.close();
				} catch (IOException e) {
					 
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	@Override
	public void parse(DataInputStream dis) {
		// TODO Auto-generated method stube
		new Thread(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							Logging.info("收到对方拒绝的信息");
							NetworkMgr.getMgr().getTempSocket().close();
							Logging.info("关闭开放的临时端口");
							MessageDialog.openInformation(new Shell(Display.getDefault()), 
									"connection aborted", "the other side refuse your connection request!");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			}
		}).start();
	}

}
