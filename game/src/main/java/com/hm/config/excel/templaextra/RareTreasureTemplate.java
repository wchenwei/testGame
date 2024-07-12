package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionSweeperTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.RandomRatio;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.Map;

@FileConfig("mission_sweeper")
public class RareTreasureTemplate extends MissionSweeperTemplate{
	private List<Items> rewards =Lists.newArrayList();
	private RandomRatio mapRatio;
	private Map<Integer,Integer> sweepMap = Maps.newConcurrentMap();
	
	public void init(){
		this.rewards = ItemUtils.str2ItemList(this.getReward(), ",", ":");
		this.mapRatio = new RandomRatio(this.getMap());
		String[] strs = getOnekey_reward().split(",");
		for(String s:strs){
			int type = Integer.parseInt(s.split(":")[0]);
			int num = Integer.parseInt(s.split(":")[1]);
			this.sweepMap.put(type, num);
		}
	}
	
	public List<Items> getRewards(){
		return rewards;
	}
	
	public int randomMapId(){
		return mapRatio.randomRatio();
	}
	
	public Map<Integer,Integer> getSweepMap(){
		return sweepMap;
	}
	
}
