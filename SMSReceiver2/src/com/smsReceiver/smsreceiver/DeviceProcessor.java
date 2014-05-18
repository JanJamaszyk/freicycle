package com.smsReceiver.smsreceiver;

public interface DeviceProcessor {
	String processMessage(String number, String rawMessage) throws NoDeviceMessageException;
	public void setBikeKey(String key);
}
