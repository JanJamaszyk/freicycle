package com.smsReceiver.smsreceiver;

public interface DeviceReceiver {
	String getMessage(String rawMessage) throws NoDeviceMessageException;
}
