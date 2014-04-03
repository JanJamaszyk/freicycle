package com.smsTest;

import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;

import com.smsutils.MD5;

public class UpdatePositionTask extends TimerTask implements LocationListener {

	Context context;
	Sender sender;
	double latitude=0;
	double longitude=0;
	String message="";
	Time time = new Time();
	IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	Intent batteryStatus;
	LocationManager locManager;

	TelephonyManager telManager;
	String secret;
	
	public UpdatePositionTask(Context context, String number, String secret) {
		this.context=context;
		this.secret = secret;
		this.sender = new SMSSender(number);
		this.batteryStatus = context.registerReceiver(null, ifilter);
		this.telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

		this.locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);         
		String provider = locManager.getBestProvider(new Criteria(), true);
		
		this.locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60*1000,10.0f, this);
		Location location = locManager.getLastKnownLocation(provider);
		
		if(location!=null)
			this.onLocationChanged(location);
		
	}
	
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	public void setNumber(String number) {
		if(this.sender.getClass() == SMSSender.class)
			((SMSSender)this.sender).setNumber(number);
		else
			Log.e("setNumber", "Only defined for SMS Sender!");
	}

	@Override
	public void run() {
		
		int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		int health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
		
		int batteryPct = (int)(100 * (level / (float)scale));
		time.setToNow();
		String msg = latitude+":"+longitude+":"+ time.toMillis(false) +":" + batteryPct +":"+health+":"+this.message;
		msg += ":" + MD5.hash(msg+this.secret);
		//Log.d("lŠnge", ""+msg.length());
		this.sender.send(msg);
		this.message="";
	}
	
	public void onLocationChanged(Location location) {
		latitude = location.getLatitude();
		longitude = location.getLongitude();
		if(outOfRange(location)) {
			this.message="range";
			locManager.removeUpdates(this);
		}
		this.run();
	}

	public boolean outOfRange(Location location) {
	    return telManager.getSimCountryIso()=="de";
	    /*
		return ((location.getLatitude() > 51.0768337) &&
				(location.getLatitude() < 53.7286946) &&
				(location.getLongitude() > 7.6229597) &&
				(location.getLongitude() < 13.7725857));
		*/
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}
