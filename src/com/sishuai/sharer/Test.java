package com.sishuai.sharer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
/**
 * 
 * 用来临时测试的地方
 *
 */
public class Test {
	public static void main(String[] args){
		byte[] buf = null;
		try {
			buf = "abcdefg\n".getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("/home/tose/a.txt")));
			try {
				bos.write(buf, 0, buf.length);
				bos.flush();
				bos.write(buf, 0, buf.length);
 				bos.flush();
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
