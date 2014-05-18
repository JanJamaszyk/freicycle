package com.smsutils;

import android.telephony.SmsManager;

public class SMSSender implements Sender {
	
	private String number;
	
	public SMSSender(String number) {
		this.number = number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Override
	public void send(String message) {
		  try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(number, null, message, null, null);
		  } catch (Exception e) {
			e.printStackTrace();
		  }
	}

}
