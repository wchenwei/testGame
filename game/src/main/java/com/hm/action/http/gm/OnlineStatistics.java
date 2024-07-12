package com.hm.action.http.gm;

import com.google.common.collect.Lists;
import com.hm.libcore.db.mongo.DBEntity;

import java.util.List;

public class OnlineStatistics extends DBEntity<String>{
	private List<OnlineNumInfo> onlineInfo=Lists.newArrayList();

	public OnlineStatistics() {
		super();
	}
	public OnlineStatistics(String id) {
		super();
		this.setId(id);
	}
	
	public List<OnlineNumInfo> getOnlineInfo() {
		return onlineInfo;
	}
	public void addOnlineInfo(OnlineNumInfo onlineNumInfo){
		this.onlineInfo.add(onlineNumInfo);
	}
	

}
