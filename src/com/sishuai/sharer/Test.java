package com.sishuai.sharer;

import com.sishuai.sharer.util.Utils;

public class Test {
	
	public static void main(String[] args) {
		System.out.println(f());
	}
	
	public static int f() {
		try {
			int i = 1/0;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return -1;
		} finally {
			System.out.println("finally");
			
		}
		return 0;
	}
	
}