
package com.hm.action.login.vo;
/**
 * Title: LoadPlayerVO.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年4月24日 上午10:58:44
 * @version 1.0
 */
public class LoadPlayerVO {

	private int state;//状态
	
	private String chatIp;
	
	private String chatPort;
	
	private long createtime;

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getChatIp() {
		return chatIp;
	}

	public void setChatIp(String chatIp) {
		this.chatIp = chatIp;
	}

	public String getChatPort() {
		return chatPort;
	}

	public void setChatPort(String chatPort) {
		this.chatPort = chatPort;
	}

	
	
	
}

