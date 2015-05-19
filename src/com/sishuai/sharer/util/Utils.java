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
	private static Random random;
	private static SimpleDateFormat simpleDateFormat;
	private static DecimalFormat onePointFormat;
	private static String separator;
	public static DecimalFormat fourFormat;
	
	public static SimpleDateFormat getSimpleDataFormat() {
		if (simpleDateFormat == null) 
			simpleDateFormat = new SimpleDateFormat("[yyyy-MM-d HH:mm:ss]");
		return simpleDateFormat;
	}
	
	public static String getSeparator() {
		if (separator == null)
			separator = System.getProperty("file.separator");
		return separator;
	}
	
	public static DecimalFormat getOnePointFormat() {
		if (onePointFormat == null)
			onePointFormat = new DecimalFormat("0.0");
		return onePointFormat;
	}
	
	public static Random getRandom() {
		if (random == null)
			random = new Random();
		return random;
	}
	
	public static DecimalFormat getFourFormat() {
		if (fourFormat == null)
			fourFormat = new DecimalFormat("0000");
		return fourFormat;
	}
}