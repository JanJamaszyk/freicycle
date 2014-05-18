package com.smsReceiver.smsreceiver;

import com.smsutils.MD5;

public class PhoneProcessor implements DeviceProcessor {

	private String bikeKey="";

	public PhoneProcessor() {
	}
	
	public void setBikeKey(String key) {
		this.bikeKey = key;
	}

	@Override
	public String processMessage(String rawMessage) throws NoDeviceMessageException {
		int hashIndex = rawMessage.lastIndexOf(":");

		if(hashIndex<=0) {
			throw new NoDeviceMessageException();
		}
		String hash = rawMessage.substring(hashIndex+1);
		String message = rawMessage.substring(0, hashIndex);
		String referenceHash = MD5.hash(message+this.bikeKey);

		if( !hash.equals(referenceHash) ) {
			throw new NoDeviceMessageException();
		}
		
		return message;
	}

}
