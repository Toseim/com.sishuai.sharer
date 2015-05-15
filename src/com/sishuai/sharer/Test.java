package com.sishuai.sharer;




import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class Test {
	
	public static void main(String[] args) {
		try {
			File in = new File("/home/tose/Downloads/test/A.java");
			File out = new File("/home/tose/Downloads/test/B.java");
			Scanner scanner = new Scanner(System.in);
			int choose = scanner.nextInt();
			if (choose == 1) {
				ServerSocket ss = new ServerSocket(9990);
				Socket socket = ss.accept();
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(in));
				long fileLen = in.length();
				dos.writeLong(fileLen);
				dos.flush();
				byte[] buf = new byte[(int)fileLen];
				bis.read(buf, 0, buf.length);
				for (int i = 0; i < buf.length; i++) {
					System.out.print(buf[i]+" ");
				}
				dos.write(buf, 0, buf.length);
			} else {
				Socket socket = new Socket("127.0.0.1", 9990);
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));
				long fileLen = dis.readLong();
				System.out.println(fileLen);
				byte[] buf = new byte[(int)fileLen];
				dis.read(buf, 0, buf.length);
				for (int i = 0; i < buf.length; i++) {
					System.out.print(buf[i]+" ");
				}
				bos.write(buf, 0, buf.length);
				bos.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
/*	public static void main(String[] args) {
		File in = new File("/home/tose/Downloads/test/A.java");
		File out = new File("/home/tose/Downloads/test/B.java");
		
		try {
			BufferedInputStream dis = new BufferedInputStream(new FileInputStream(in));
			long fileLen = in.length();
			System.out.println(fileLen);
			byte[] buf = new byte[(int)fileLen];
			dis.read(buf, 0, buf.length);
			for (int i = 0; i < buf.length; i++) {
				System.out.print(buf[i]+" ");
			}
			
			BufferedOutputStream dos = new BufferedOutputStream(new FileOutputStream(out));
			dos.write(buf, 0, buf.length);
			dos.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
}
