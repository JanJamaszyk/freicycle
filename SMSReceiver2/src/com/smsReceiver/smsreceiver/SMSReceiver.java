package com.smsReceiver.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.smsutils.MD5;
import com.smsutils.UpdateFailedException;

public class SMSReceiver extends BroadcastReceiver {

	private UpdateDatabase updater;
	private String bikeKey="";

	public SMSReceiver(UpdateDatabase updater) {
		super();
		this.updater = updater;
		Log.d("service","created!");
	}

	public void setBikeKey(String key) {
		this.bikeKey = key;
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		Bundle extras = intent.getExtras();
		
		if ( extras != null )
		{
			Object[] smsextras = (Object[]) extras.get( "pdus" );

			for ( int i = 0; i < smsextras.length; i++ )
			{
				SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

				String body = smsmsg.getMessageBody().toString();
				String address = smsmsg.getOriginatingAddress();

				int hashIndex = body.lastIndexOf(":");

				if(hashIndex>0) {
					String hash = body.substring(hashIndex+1);
					String message = body.substring(0, hashIndex);
					String referenceHash = MD5.hash(message+this.bikeKey);
					try {
						if( hash.equals(referenceHash) ) {
							updater.update(address, message);
							Log.d("forwarded", address);
							this.abortBroadcast();
						} else {
							Log.d("key", this.bikeKey);
							Log.d("hash","discarded!" );
							Log.d("hash", hash);
							Log.d("reference Hash", referenceHash);
							Log.d("message",message);
						}

					} catch (UpdateFailedException e) {
						Log.e("update",e.toString());
					}
				}

			}
		}
	}
}