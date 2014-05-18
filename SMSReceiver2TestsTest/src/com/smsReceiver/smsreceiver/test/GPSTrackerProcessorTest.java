package com.smsReceiver.smsreceiver.test;

import junit.framework.TestCase;

import com.smsReceiver.smsreceiver.GPSTrackerProcessor;
import com.smsReceiver.smsreceiver.NoDeviceMessageException;

public class GPSTrackerProcessorTest extends TestCase {

	public GPSTrackerProcessorTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testProcessMessage() throws NoDeviceMessageException {
		GPSTrackerProcessor gpsProcessor = new GPSTrackerProcessor();

		String answer = gpsProcessor.processMessage(null, "lat:52.397735\n"
				+ "long:9.673347\n"
				+ "speed:0.00\n"
				+ "T:14/05/18 17:28\n"
				+ "bat:100%\n"
				+ "http://maps.google.com/maps?f=q&q=52.397735,9.673347&z=16");

		assertEquals("Processed String", "52.397735:9.673347:1400405280000:1.0:2:",answer );

		answer = gpsProcessor.processMessage(null, "lat:52.397735\n"
				+ "long:9.673347\n"
				+ "speed:0.00\n"
				+ "T:14/05/18 0:28\n"
				+ "bat:100%\n"
				+ "http://maps.google.com/maps?f=q&q=52.397735,9.673347&z=16");

		assertEquals("Processed String", "52.397735:9.673347:1400344080000:1.0:2:",answer );
	}

}
