package com.smsReceiver.smsreceiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.smsReceiver.smsreceiver.SMSService.MyLocalBinder;

public class SmsReceiverActivity extends Activity {

	EditText bikeKeyField;
	EditText networkKeyField;
	EditText domainNameField;
	ToggleButton forwardingButton;
	Button changeKeyButton;
	SharedPreferences sharedPref;
	
    SMSService smsService;
    boolean isBound = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		
		bikeKeyField = (EditText)findViewById(R.id.bikeKey);
		networkKeyField = (EditText)findViewById(R.id.networkKey);
		domainNameField = (EditText)findViewById(R.id.domainName);
		forwardingButton = (ToggleButton)findViewById(R.id.forwardingButton);
		changeKeyButton = (Button)findViewById(R.id.changeButton);
		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		

		forwardingButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SmsReceiverActivity.this.checkForwarding();
				
			}
		});
		
		changeKeyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SmsReceiverActivity.this.updateKeys();
			}
		});

		String bikeKey = sharedPref.getString(getString(R.string.bike_key), bikeKeyField.getText().toString());
		String networkKey = sharedPref.getString(getString(R.string.network_key), networkKeyField.getText().toString());
		String domainName = sharedPref.getString(getString(R.string.domain_name), domainNameField.getText().toString());
		boolean forwarding = sharedPref.getBoolean(getString(R.string.forwarding), true);
		
		bikeKeyField.setText(bikeKey);
		networkKeyField.setText(networkKey);
		domainNameField.setText(domainName);
		forwardingButton.setChecked(forwarding);

		this.updateKeys();
		this.checkForwarding();
	}

	public void onDestroy() {
		super.onDestroy();
		unbindService(serviceConnection);
	}
	
	public void updateKeys() {

		String bikeKey = bikeKeyField.getText().toString();
		String networkKey = networkKeyField.getText().toString();
		String domainName= domainNameField.getText().toString();
		
		if(isBound) {
			smsService.setBikeKey(bikeKey);
			smsService.setDomainName(domainName);
			smsService.setNetworkKey(networkKey);
		}
		
		SharedPreferences.Editor editor = sharedPref.edit();

		editor.putString(getString(R.string.bike_key), bikeKey );
		editor.putString(getString(R.string.network_key), networkKey);
		editor.putString(getString(R.string.domain_name), domainName);

		editor.commit();
	}
	
	private void checkForwarding() {
		Intent serviceIntent = new Intent(this, SMSService.class);
		boolean forwarding = forwardingButton.isChecked();
		Log.d("forwarding",""+forwarding);
		if(forwarding) {
			startService(serviceIntent);
			bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
		} else {
			unbindService(serviceConnection);
			stopService(serviceIntent);
		}

		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean(getString(R.string.forwarding), forwarding);
		editor.commit();
	}
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

	    public void onServiceConnected(ComponentName className,
	            IBinder service) {
	        MyLocalBinder binder = (MyLocalBinder) service;
	        smsService = binder.getService();
	        isBound = true;
	    }
	    
	    public void onServiceDisconnected(ComponentName arg0) {
	        isBound = false;
	    }
	    
	   };

}
