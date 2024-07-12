package com.hm.model.activity;

import com.google.common.collect.Lists;
import com.hm.model.player.BasePlayer;

import java.util.ArrayList;

/**
 * @Description: 通用的id属性列表
 * @author siyunlong  
 * @date 2018年5月14日 下午3:10:08 
 * @version V1.0
 */
public class PlayerActivityIdListValue extends PlayerActivityValue{
	//已经领取的id列表
	private ArrayList<Integer> idList = Lists.newArrayList();
	
	@Override
	public void clearRepeatValue() {
		this.idList.clear();
	}

	@Override
	public boolean checkCanReward(BasePlayer player,int id) {
		return !this.idList.contains(id);
	}

	@Override
	public void setRewardState(BasePlayer player, int id) {
		this.idList.add(id);
		player.getPlayerActivity().SetChanged();
	}

	public ArrayList<Integer> getIdList() {
		return idList;
	}
	

}
