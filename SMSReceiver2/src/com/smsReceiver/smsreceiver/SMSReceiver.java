package com.smsReceiver.smsreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.smsutils.UpdateFailedException;

public class SMSReceiver extends BroadcastReceiver {

	private UpdateDatabase updater;
	private DeviceReceiver receiver;

	public SMSReceiver(UpdateDatabase updater, DeviceReceiver receiver) {
		super();
		this.updater = updater;
		this.receiver = receiver;
		Log.d("service","created!");
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

				Log.d("message",body);

				try{
					String message = this.receiver.getMessage(body);
					updater.update(address, message);
					Log.d("forwarded", address);
					this.abortBroadcast();

				} catch (NoDeviceMessageException e) {

				} catch (UpdateFailedException e) {
					Log.e("update",e.toString());
				}


			}

		}
	}
}