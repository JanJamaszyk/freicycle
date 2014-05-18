package com.smsReceiver.smsreceiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

import com.smsutils.SMSSender;
import com.smsutils.Sender;

public class GPSTrackerProcessor implements DeviceProcessor {

	private String bikeKey;
	
	public boolean outOfRange(float latitude, float longitude) {
		return !((latitude > 51.0768337) &&
				(latitude < 53.7286946) &&
				(longitude > 7.6229597) &&
				(longitude < 13.7725857));
	}
	
	public void setBikeKey(String key) {
		this.bikeKey = key;
	}
	
	@Override
	public String processMessage(String number, String rawMessage)
			throws NoDeviceMessageException {
		
		String trackerMessage="";
		String result="";
		String[] parts = rawMessage.split("\n");
		if(parts.length!=6)
			throw new NoDeviceMessageException();
		for(int i=0; i<parts.length-1; i++) {
			int index = parts[i].indexOf(":");
			if(index<=0)
				throw new NoDeviceMessageException();
			parts[i] = parts[i].substring(index+1);
		}
		
		/*
		String[] timeStampParts = parts[3].split(" ");
		if(timeStampParts.length!=2)
			throw new NoDeviceMessageException();
		String[] dateParts = timeStampParts[0].split("/");
		if(dateParts.length!=3)
			throw new NoDeviceMessageException();
		String[] timePart = timeStampParts[1].split(":");
		if(timePart.length!=2)
			throw new NoDeviceMessageException();

		//TIME in UTC+8
		Time time = new Time();
		time.set(0, Integer.parseInt(timePart[1]), 
				Integer.parseInt(timePart[0]), 
				Integer.parseInt(dateParts[2]), 
				Integer.parseInt(dateParts[1]),
				Integer.parseInt(dateParts[0])+2000);
		time.normalize(true);
		 */
		SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd hh:mm", Locale.US); // Date Format
		formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));

		Date date;
		try {
			date = formatter.parse(parts[3]);
		} catch (ParseException e) {
			throw new NoDeviceMessageException();
		}

		
		if(outOfRange(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]))) {
			Sender sender = new SMSSender(number);
			sender.send("nofix"+this.bikeKey);
			Log.d("GPSProcessor","Out of Range, nofix send!");
			trackerMessage = "range";
		}
		
		
		result = parts[0]+":"+parts[1] + ":"; // Latitude, Longitude
		//result += time.toMillis(true)+":"; // Time
		result += date.getTime() + ":";
		result += Float.parseFloat(parts[4].replaceAll("[^0-9]", ""))/100+":2" + ":"; //Battery Level
		result += trackerMessage;
		
		return result;
	}
	
	

}
