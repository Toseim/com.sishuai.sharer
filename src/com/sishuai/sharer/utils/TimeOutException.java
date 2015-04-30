package com.sishuai.sharer.utils;

public class TimeOutException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public TimeOutException(String ErrMsg) {
		super(ErrMsg);
	}
}