package com.smsReceiver.smsreceiver.test;

import junit.framework.TestCase;

import com.smsReceiver.smsreceiver.NoDeviceMessageException;
import com.smsReceiver.smsreceiver.PhoneProcessor;

public class PhoneProcessorTest extends TestCase {

	public PhoneProcessorTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testProcessMessageGood() throws NoDeviceMessageException {
		PhoneProcessor phoneProcessor = new PhoneProcessor();
		phoneProcessor.setBikeKey("b!k3KeY");

		String answer = phoneProcessor.processMessage("52.380656:9.745458:1400403040577:0.32:2:no_lock:d0d643cfadf60c4a6f6199c83777d9c5");
		assertEquals("Processed String","52.380656:9.745458:1400403040577:0.32:2:no_lock", answer);
	}

}
