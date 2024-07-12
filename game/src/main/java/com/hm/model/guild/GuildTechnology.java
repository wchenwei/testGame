package com.hm.model.guild;

import com.hm.libcore.msg.JsonMsg;
import com.hm.config.GameConstants;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class GuildTechnology extends GuildComponent {
	//奖励产出时间，用于（204-每隔n小时2级城市双倍产出）
	private long rewardDate = 0;
	// 科技（科技信息，等级）
	private ConcurrentHashMap<Integer, Integer> technologys=new ConcurrentHashMap<Integer, Integer>();
	//
	private long resetMsec = 0;
	//每个科技的使用次数
	private ConcurrentHashMap<Integer, Integer> techUseMap=new ConcurrentHashMap<Integer, Integer>();
	
	public ConcurrentHashMap<Integer, Integer> getTechnologys() {
		return technologys;
	}
	public Integer getTecLvById(int id){
		return technologys.getOrDefault(id, 0);
	}
	public long getResetMsec() {
		return resetMsec;
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("guildTechnology", this);
	}
	public boolean have(int techId) {
		return technologys.contains(techId);
	}
	
	public void updateTecLv(int techId, int lv) {
		this.technologys.put(techId, lv);
		SetChanged();
	}
	//重置科技
	public void cleanTec() {
		this.technologys.clear();
		resetDate();
		SetChanged();
	}
	public void resetDate() {
		this.resetMsec = System.currentTimeMillis();
		SetChanged();
	}
	//是否已经过了重置时间
	public boolean isOutRestTime(int resetTime) {
		return 0==resetMsec || System.currentTimeMillis() - resetMsec > resetTime*GameConstants.MINUTE;
	}
	
	public void doDayReset() {
		this.techUseMap.clear();
		SetChanged();
	}
	public void resetRewardDate() {
		this.rewardDate = new Date().getTime();
		SetChanged();
	}
	
	public long getRewardDate() {
		return rewardDate;
	}
	public int getTechUseTimes(int techId) {
		return this.techUseMap.getOrDefault(techId, 0);
	}
	public void addTechUserTimes(int techId) {
		this.techUseMap.put(techId, getTechUseTimes(techId)+1);
		SetChanged();
	}
}





