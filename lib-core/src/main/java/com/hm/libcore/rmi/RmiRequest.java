/**
 * 
 */
package com.hm.libcore.rmi;

import java.io.Serializable;
import java.util.Map;

/**
 * Title: RmiRequest.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 叶亮
 * @date 2014年10月28日 下午4:52:24
 * @version 1.0
 */
public class RmiRequest implements Serializable{
	private static final long serialVersionUID = -8714124287860851966L;
	private String action;
	private String method;
	private int serverId;
	private Map<String,Object> params;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	public int getServerId() {
		return serverId;
	}
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}
	
	
}
