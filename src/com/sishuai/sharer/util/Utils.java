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
	public static Random random = new Random();
	public static SimpleDateFormat simpleDateFormat = 
			new SimpleDateFormat("[yyyy-MM-d HH:mm:ss]");
	public static DecimalFormat decimalFormat = new DecimalFormat("#.#");
}