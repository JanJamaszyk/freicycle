package com.smsTest;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

public class LocationManagerHandler implements Runnable {

	LocationManager locManager;
	LocationListener locListener;
	String provider;

	public LocationManagerHandler(Context context, LocationListener listener) {
		this.locManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		this.locListener = listener;
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = locManager.getBestProvider(criteria, true);
		Log.d("provider", provider);
		
		registerListener();
	}
	
	public Location getLastKnownLocation() {
		return this.locManager.getLastKnownLocation(provider);
	}

	public void registerListener() {
		this.locManager.requestLocationUpdates(provider,0,0, locListener);
	}
	
	public void unregisterListener() {
		this.locManager.removeUpdates(locListener);
	}
	
	@Override
	public void run() {
		//Log.d("scheduler","registered!");
		this.registerListener();
	}
	
}
