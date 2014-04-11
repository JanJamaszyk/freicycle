package com.smsReceiver.smsreceiver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SmsReceiverActivity extends Activity {

	private final ScheduledExecutorService scheduler =
			Executors.newScheduledThreadPool(1);
	ReceiveSMS receiver;
	EditText bikeKeyField;
	EditText networkKeyField;
	EditText domainNameField;
	Button changeKeyButton;
	SharedPreferences sharedPref;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		bikeKeyField = (EditText)findViewById(R.id.bikeKey);
		networkKeyField = (EditText)findViewById(R.id.networkKey);
		domainNameField = (EditText)findViewById(R.id.domainName);
		changeKeyButton = (Button)findViewById(R.id.changeButton);
		sharedPref = getPreferences(Context.MODE_PRIVATE);

		changeKeyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				SmsReceiverActivity.this.updateKeys();
			}
		});

		String bikeKey = sharedPref.getString(getString(R.string.bike_key), bikeKeyField.getText().toString());
		String networkKey = sharedPref.getString(getString(R.string.network_key), networkKeyField.getText().toString());
		String domainName = sharedPref.getString(getString(R.string.domain_name), domainNameField.getText().toString());
		bikeKeyField.setText(bikeKey);
		networkKeyField.setText(networkKey);
		domainNameField.setText(domainName);

		//receiver = new ReceiveSMS(this, domainName,bikeKey, networkKey);
		
		//receiver.run();
		//scheduler.scheduleWithFixedDelay(receiver, 0, 1000, TimeUnit.MILLISECONDS);
		scheduler.scheduleWithFixedDelay(new DatabaseTest("Test1"), 0, 1000, TimeUnit.MILLISECONDS);
	}

	public void updateKeys() {
		SharedPreferences.Editor editor = sharedPref.edit();

		String bikeKey = bikeKeyField.getText().toString();
		String networkKey = networkKeyField.getText().toString();
		String domainName= domainNameField.getText().toString();

		editor.putString(getString(R.string.bike_key), bikeKey );
		editor.putString(getString(R.string.network_key), networkKey);
		editor.putString(getString(R.string.domain_name), domainName);

		editor.commit();

		this.receiver.setBikeKey(bikeKey);
		this.receiver.setNetworkKey(networkKey);
		this.receiver.setDomainName(domainName);
	}

}
