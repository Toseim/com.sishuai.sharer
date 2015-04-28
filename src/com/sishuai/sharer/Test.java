package com.sishuai.sharer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

interface T {
	public static int TT() {
		System.out.println("TT");
		return 0;
	}
}
class A implements T {
	public static int TT() {
		System.out.println("AA");
		return 1;
	}
}

public class Test {
	public static void main(String[] args) throws UnknownHostException {
		A.TT();
		T.TT();
	}

}
