package com.smsutils;

public class SMSSenderOnlyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1794459179845703200L;
	
	public SMSSenderOnlyException() {
		super();
	}
	
	public SMSSenderOnlyException(Exception e) {
		super(e);
	}

}
