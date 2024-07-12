/**
 * 
 */
package com.hm.libcore.rmi;

/**
 * Title: RmiError.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014年10月28日 下午5:57:11
 * @version 1.0
 */
public enum RmiError {
	RMI_ERROR("RMI调用出错","-1000");
	
	private String name;
	
	private String code;

	private RmiError(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
	
	
}
