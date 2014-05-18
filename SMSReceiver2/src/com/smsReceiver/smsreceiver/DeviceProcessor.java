package com.smsReceiver.smsreceiver;

public interface DeviceProcessor {
	String processMessage(String rawMessage) throws NoDeviceMessageException;
}
