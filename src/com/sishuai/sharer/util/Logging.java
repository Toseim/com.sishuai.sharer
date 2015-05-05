package com.sishuai.sharer.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import sun.util.logging.resources.logging;

import com.sishuai.sharer.Activator;

/**
 * log everything
 * @author 四帅
 *
 */
public class Logging {
	private static Logging logger;
	private static final File logFile = 
			Activator.getDefault().getStateLocation().append("sharer.log").toFile();
	private BufferedOutputStream bos;
    private String fileName;
    private SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-d HH:mm:ss]");
    
    public String wrap() {
        long nowTime = System.currentTimeMillis();
        return sdf.format(nowTime)+" ["+fileName+"]";
    }
    public void setFileName(String filename) {
        this.fileName = filename;
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
        byte[] content = string.getBytes();
        try {
            bos.write(content, 0, content.length);
            bos.flush();
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
        logger.Output(fatal);
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
