package com.sishuai.sharer.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sishuai.sharer.Activator;

/**
 * log everything
 * 
 * @author 四帅 clear
 */
public class Logging {
	private static Logging logger;
	private static File logFile = Activator.getDefault()
			.getStateLocation().append("sharer.log").toFile();
	private BufferedOutputStream bos;
	private String fileName;

	public String wrap() {
		long nowTime = System.currentTimeMillis();
		return Utils.getSimpleDataFormat().format(nowTime) + " [" + fileName + "]";
	}

	public void setFileName(String filename) {
		this.fileName = filename;
	}

	public static Logging getLogger() {
		if (logger == null) {
			logger = new Logging();
			try {
				if (logger.bos == null) {
					logger.bos = new BufferedOutputStream(new FileOutputStream(logFile));
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return logger;
	}

	public void Output(String string, boolean isflush) {
		System.out.println(string);
		if (logger == null) getLogger();
		byte[] content = (string + "\n").getBytes();
		try {
			bos.write(content, 0, content.length);
			if (isflush)
				bos.flush();
		} catch (IOException e) {
		}
	}

	public static void info(String info) {
		String subInfo = logger.wrap() + " INFO: " + info;
		logger.Output(subInfo, false);
	}

	public static void warning(String warning) {
		String subWarning = logger.wrap() + " WARNING: " + warning;
		logger.Output(subWarning, true);
	}

	public static void fatal(String fatal) {
		String subFatal = logger.wrap() + " FATAL: " + fatal;
		logger.Output(subFatal, true);
	}

	public static void dispose() {
		if (logger == null) return;
		if (logger.bos != null) {
			try {
				logger.bos.close();
			} catch (Exception e) {
			}
		}		
	}
}
