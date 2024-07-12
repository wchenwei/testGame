package com.hm.model.player;

import com.hm.enums.HonorType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.timerjob.GuildWarUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 荣誉
 * @author siyunlong  
 * @date 2018年12月10日 下午5:56:09 
 * @version V1.0
 */
@Getter
public class PlayerHonor extends PlayerDataContext {
	private long allTotalHonor;
	
	private long totalHonor;
	//每项获得荣誉数
//	private ConcurrentHashMap<Integer,Long> honorMap = new ConcurrentHashMap<>();
	//已经领取的荣誉id
	private ArrayList<Integer> rewardIndex = new ArrayList<>();
	//今日等级
	private int honorLv;

	public void addHonor(HonorType type,long value) {
		if(value == 0) {
			return;
		}
		this.allTotalHonor += value;
		this.totalHonor += value;
//		this.honorMap.put(type.getType(), honorMap.getOrDefault(type.getType(), 0L)+value);
		SetChanged();
	}
	
	public void setAllTotalHonor(long allTotalHonor) {
		this.allTotalHonor = allTotalHonor;
		SetChanged();
	}



	public void addRewardIndex(List<Integer> indexList) {
		this.rewardIndex.addAll(indexList);
		SetChanged();
	}
	public void addRewardIndex(int index) {
		this.rewardIndex.add(index);
		SetChanged();
	}
	
	public boolean containRewardIndex(int index) {
		return this.rewardIndex.contains(index);
	}

	public void resetDay() {
		this.totalHonor = 0;
//		this.honorMap.clear();
		this.rewardIndex.clear();
		this.honorLv = super.Context().playerMission().getFbId();
		SetChanged();
	}


	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerHonor", this);
	}
}
