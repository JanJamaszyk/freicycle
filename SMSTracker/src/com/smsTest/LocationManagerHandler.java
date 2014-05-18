package com.smsTest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

public class LocationManagerHandler {

    private static LocationManagerHandler instance = null;
	
	LocationManager locManager;
	LocationListener locListener;
	AlarmManager alarmManager;
	Context context;
	
	String provider;
	int delay;

	public static LocationManagerHandler getInstance() throws NotYetCreatedException {
		if(instance==null)
			throw new NotYetCreatedException();
		return instance;
	}
	
	public static LocationManagerHandler getInstance(Context context, LocationListener listener, int delay) {
		if(instance==null) {
			instance = new LocationManagerHandler(context, listener, delay);
		}
		return instance;
	}
	
	private LocationManagerHandler(Context context, LocationListener listener, int delay) {
		this.context = context;
		this.locListener = listener;
		this.locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		
		this.delay = delay*1000;
		
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = locManager.getBestProvider(criteria, true);
		Log.d("provider", provider);
		
		this.registerListener();
	}
	
	public Location getLastKnownLocation() {
		return this.locManager.getLastKnownLocation(provider);
	}

	public void registerListener() {
		this.locManager.requestLocationUpdates(provider,0 , 20, locListener);
	}
	
	public void unregisterListener() {
		this.locManager.removeUpdates(locListener);
	}
	
	public void scheduleListener() {
		this.unregisterListener();
//		Log.d("scheduler","scheduled!");

        Intent intentAlarm = new Intent(this.context, AlarmReceiver.class);
        alarmManager.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+delay, PendingIntent.getBroadcast(this.context,1,  intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
		
	}
	
}
