package com.smsReceiver.smsreceiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class SMSService extends Service {

	private final IBinder myBinder = new MyLocalBinder();
	private SMSReceiver mSMSreceiver;
	private IntentFilter mIntentFilter;
	private UpdateDatabase updater;
	private PhoneProcessor phoneProcessor;
	SharedPreferences sharedPref;

	PowerManager.WakeLock wakeLock;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SMSReceiver Lock");
		wakeLock.acquire();

		updater = new UpdateDatabase();
		phoneProcessor = new PhoneProcessor();
		
		//SMS event receiver
		mSMSreceiver = new SMSReceiver(updater, phoneProcessor);
		mIntentFilter = new IntentFilter();
		mIntentFilter.setPriority(Integer.MAX_VALUE);
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
		
		Notification notification = new Notification(R.drawable.ic_launcher, getText(R.string.ticker_text),
		        System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, SmsReceiverActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(this, getText(R.string.notification_title),
		        getText(R.string.notification_message), pendingIntent);
		startForeground(0, notification);
	}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();

		// Unregister the SMS receiver
		unregisterReceiver(mSMSreceiver);

		wakeLock.release();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return myBinder;
	}
	
	public void setBikeKey(String key) {
		this.phoneProcessor.setBikeKey(key);
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