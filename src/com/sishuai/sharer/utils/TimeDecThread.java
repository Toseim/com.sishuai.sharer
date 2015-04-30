package com.sishuai.sharer.utils;

public class TimeDecThread implements Runnable {
	private int timeLimit;
	private boolean isCancel = false;
	
	
	public TimeDecThread(int timeLimit) {
		this.timeLimit = timeLimit;
	}
	
	public synchronized void cancel() {
		isCancel = true;
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(timeLimit * 1000);
			if (!isCancel) 
				throw new TimeOutException("TIME OUT!");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
