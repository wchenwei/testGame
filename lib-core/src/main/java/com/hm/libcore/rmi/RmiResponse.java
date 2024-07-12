/**
 * 
 */
package com.hm.libcore.rmi;

import java.io.Serializable;

/**
 * Title: RmiResponse.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014年10月28日 下午4:54:18
 * @version 1.0
 */
public class RmiResponse implements Serializable{
	private static final long serialVersionUID = 6531654855580450239L;
	private byte code;//0错误信息 1正确返回
	private String result;//此属性存储返回值json对象字符串,当code=0时，此字段返回是错误码，code=1时。此字段可以返回任意数据，包括json
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
