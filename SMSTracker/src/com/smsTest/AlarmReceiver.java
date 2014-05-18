package com.smsTest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
//		Log.d("receiver","alarm received!");
		LocationManagerHandler handler;
		try {
			handler = LocationManagerHandler.getInstance();
			handler.registerListener();
		} catch (NotYetCreatedException e) {
			Toast.makeText(context, "Alarm receiver error!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

}
