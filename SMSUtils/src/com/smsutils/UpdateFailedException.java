package com.smsutils;

public class UpdateFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6076406417602478651L;
	
	public UpdateFailedException(Exception exception) {
		super(exception);
	}

}
