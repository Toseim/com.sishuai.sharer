package com.sishuai.sharer.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * 这个类放一些编写时的工具
 * @author 四帅
 * clear
 */
public class Utils {
	public static Random random;
	public static SimpleDateFormat simpleDateFormat;
	public static DecimalFormat decimalFormat;
	public static String os;
	
	public static SimpleDateFormat getSimpleDataFormat() {
		if (simpleDateFormat == null) 
			simpleDateFormat = new SimpleDateFormat("[yyyy-MM-d HH:mm:ss]");
		return simpleDateFormat;
	}
	
	public static DecimalFormat getDecimalFormat() {
		if (decimalFormat == null)
			decimalFormat = new DecimalFormat("0.0");
		return decimalFormat;
	}
	
	public static Random getRandom() {
		if (random == null)
			random = new Random();
		return random;
	}
	
	public static String getos() {
		if (os == null)
			os = System.getProperty("os.name");
		return os;
	}
}