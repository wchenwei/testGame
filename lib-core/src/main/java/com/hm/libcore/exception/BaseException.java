package com.hm.libcore.exception;

public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 701744682420624838L;
	
	public BaseException(String msg){
		super(msg);
	}
	
	public BaseException(String msg,Throwable t){
		super(msg,t);
	}

}
