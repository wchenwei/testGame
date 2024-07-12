package com.hm.model.serverpublic;

import lombok.Data;

/**
 * 
 * @Description: 服务器结盟数据
 * @author siyunlong  
 * @date 2019年10月10日 下午2:29:21 
 * @version V1.0
 */
@Data
public class ServerAllianceData extends ServerPublicContext{
	private int campId;
	private int campId2;
	
	public void campAlliance(int campId,int campId2) {
		this.campId = campId;
		this.campId2 = campId2;
		SetChanged();
		save();
	}
	
	public boolean isAllianCamp(int campId) {
		return this.campId == campId || this.campId2 == campId;
	}
	
	public boolean isAllianCamp(int campId,int campId2) {
		return this.campId == campId && this.campId2 == campId2
				|| this.campId == campId2 && this.campId2 == campId;
	}
	
	public int getFriendCamp(int campId) {
		if(this.campId == campId) return this.campId2;
		if(this.campId2 == campId) return this.campId;
		return 0;
	}
	
	public boolean haveAllian() {
		return this.campId > 0 && this.campId2 > 0;
	}
	
	public void clearData() {
		campAlliance(0, 0);
	}

	public boolean isAllianGuild(int guildId, int oppoGuild) {
		return false;
	}
}
