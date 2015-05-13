package com.sishuai.sharer.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sishuai.sharer.Activator;

/**
 * log everything
 * @author 四帅
 * clear
 */
public class Logging {
	private static Logging logger;
	private static final File logFile = 
			Activator.getDefault().getStateLocation().append("sharer.log").toFile();
	private BufferedOutputStream bos;
    private String fileName;
    
    public String wrap() {
        long nowTime = System.currentTimeMillis();
        return Utils.simpleDateFormat.format(nowTime)+" ["+fileName+"]";
    }
    public void setFileName(String filename) {
        this.fileName = filename;
        try {
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public static Logging getLogger() {
		if (logger == null) {
			logger = new Logging();
			try {
				logger.bos = new BufferedOutputStream(new FileOutputStream(logFile));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return logger;
	}
	public void Output(String string) {
        System.out.println(string);
        byte[] content = (string+"\n").getBytes();
        try {
            bos.write(content, 0, content.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public static void info(String info) {
        String subInfo = logger.wrap() + " INFO: " + info;
        logger.Output(subInfo);
	}
    public static void warning(String warning) {
        String subWarning = logger.wrap() + " WARNING: " + warning;
        logger.Output(subWarning);
    }
    public static void fatal(String fatal) {
        String subFatal = logger.wrap() + " FATAL: " + fatal;
        logger.Output(subFatal);
    }
    public void dispose() {
    	if (bos != null)
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
}
