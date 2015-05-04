package com.sishuai.sharer.util;

import java.io.File;

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
	
	
	public static Logging getLogger() {
		if (logger == null)
			logger = new Logging();
		return logger;
	}
	
	
}
