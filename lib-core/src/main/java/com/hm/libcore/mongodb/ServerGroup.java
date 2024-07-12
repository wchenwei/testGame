package com.hm.libcore.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class ServerGroup {
	@Id
	private int id;
	
	//============跨服竞技战==============
	private String serverip;
	private String servertcp;
	private String serverhttp;
	//============跨服领地战==============
	@Field("servermsgSec")
	private String manorUrl;
	private int state;
	
	public String getServerurl() {
		return serverip+":"+servertcp;
	}
	
	public String getHttpServerurl() {
		return serverip+":"+serverhttp;
	}

	public int getId() {
		return id;
	}
	

	public String getManorUrl() {
		return manorUrl;
	}

	public boolean isClose() {
		return this.state == 0;
	}
	
}
