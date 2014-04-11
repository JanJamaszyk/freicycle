package com.smsReceiver.smsreceiver;

import com.smsutils.UpdateFailedException;

public class DatabaseTest implements Runnable {
	
	private int counter=0;
	private String name;
	UpdateDatabase updater;
	
	public DatabaseTest(String name) {
		this.name = name;
		this.updater = new UpdateDatabase("freicycle.herokuapp.com/posin", "", "fre!cYcl3");
	}

	@Override
	public void run() {
		try {
			this.updater.update(name, "0.0:0.0:" + (System.currentTimeMillis() + 60*60*1000) + ":1:2:Test "+counter);
			counter++;
		} catch (UpdateFailedException e) {
			e.printStackTrace();
		}
	}
	
	

}
