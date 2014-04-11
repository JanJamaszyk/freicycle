package com.smsReceiver.smsreceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class SMSService extends Service {

	private final IBinder myBinder = new MyLocalBinder();
	private SMSReceiver mSMSreceiver;
	private IntentFilter mIntentFilter;
	private UpdateDatabase updater;
	SharedPreferences sharedPref;
	
	@Override
	public void onCreate()
	{
		super.onCreate();

		updater = new UpdateDatabase();
		
		//SMS event receiver
		mSMSreceiver = new SMSReceiver(updater);
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(mSMSreceiver, mIntentFilter);
		
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		
		String bikeKey = sharedPref.getString(getString(R.string.bike_key), "");
		String networkKey = sharedPref.getString(getString(R.string.network_key), "");
		String domainName = sharedPref.getString(getString(R.string.domain_name), "");
		
		Log.d("bikeKey", bikeKey);
		
		this.setBikeKey(bikeKey);
		this.setNetworkKey(networkKey);
		this.setDomainName(domainName);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		// Unregister the SMS receiver
		unregisterReceiver(mSMSreceiver);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}
	
	public void setBikeKey(String key) {
		this.mSMSreceiver.setBikeKey(key);
	}
	
	public void setDomainName(String name) {
		this.updater.setDomainName(name);
	}
	
	public void setNetworkKey(String key) {
		this.updater.setNetworkKey(key);
	}
	
	public class MyLocalBinder extends Binder {
        SMSService getService() {
            return SMSService.this;
        }
	}
	
}