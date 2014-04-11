package com.smsReceiver.smsreceiver;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.smsutils.MD5;
import com.smsutils.UpdateFailedException;

public class ReceiveSMS implements Runnable {

	Context context;
	UpdateDatabase updater;
	String bikeKey = "";

	public ReceiveSMS(Context context, String domainName, String bikeKey, String networkKey) {
		this.context = context;
		this.bikeKey = bikeKey;

		updater= new UpdateDatabase(domainName,"", networkKey);

		//TEST

		/*
		Time time = new Time();
		time.setToNow();
		updater.update("+4912345", "12345:23456:"+time.toMillis(false)+":45678:0:hallo");
		 */
	}

	public void setBikeKey(String bikeKey) {
		this.bikeKey = bikeKey;
	}

	public void setNetworkKey(String networkKey) {
		updater.setNetworkKey(networkKey);
	}

	public void setDomainName(String domainName) {
		updater.setDomainName(domainName);
	}

	@Override
	public void run() {
		Uri uriSms = Uri.parse("content://sms/inbox");
		Cursor c = context.getContentResolver().query(uriSms,
				new String[] { "_id", "thread_id", "address",
				"person", "date", "body" }, null, null, null);

		if (c != null && c.moveToFirst()) {
			Log.d("count",""+c.getCount());
			do {
				long id = c.getLong(0);
				String address = c.getString(2);
				String body = c.getString(5);
				int hashIndex = body.lastIndexOf(":");

				if(hashIndex>0) {
					String hash = body.substring(hashIndex+1);
					String message = body.substring(0, hashIndex);
					String referenceHash = MD5.hash(message+this.bikeKey);
					try {
						if( hash.equals(referenceHash) ) {
							updater.update(address, message);
							Log.d("forwarded", address);
						} else {
							Log.d("key", this.bikeKey);
							Log.d("hash","discarded!" );
							Log.d("hash", hash);
							Log.d("reference Hash", referenceHash);
							Log.d("message",message);
						}
						try {
							context.getContentResolver().delete(
									Uri.parse("content://sms/" + id), null, null);
						} catch (Exception e) {
							Log.e("delete","Could not delete SMS from inbox: " + e.getMessage());
						}

					} catch (UpdateFailedException e) {
						Log.e("update",e.toString());
					}
				}


			} while (c.moveToNext());
		}
		c.close();
	}

}
