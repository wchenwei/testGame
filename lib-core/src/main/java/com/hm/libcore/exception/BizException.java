package com.hm.libcore.exception;

/**
 * Title: 自定义异常,所有业务层抛出的异常均为此异常
 * Description:HttpServerHandler
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014-7-31
 * @version 1.0
 */
public class BizException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2666591556730358207L;
	
	private String msg;
	

	public BizException(String msg){
		this.msg = msg;
	}
	
	

	@Override
	public String getMessage() {
		return this.msg;
	}
	



	/**
	 * 提高性能 不调用堆栈
	 */
	@Override
	public  Throwable fillInStackTrace() {
		return this;
	}
	
	

}
