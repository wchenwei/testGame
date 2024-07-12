package com.hm.model.tank;

import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.BuildConfig;
import com.hm.config.excel.templaextra.TankStrengthBreakTemplate;

import java.util.Map;

public class TankStrength {
	//喂鱼的情况 key->培养的唯一id value->培养次数
	private Map<Integer,Integer> strengthMaps = Maps.newConcurrentMap();
	//各个等级的突破等级
	private int[] breachLvs = new int[] {1,1,1,1,1};
	private int[] breachProgress = new int[]{0,0,0,0,0};
	
	/**
	 * 获取该等级的突破等级
	 * @param lv
	 * @return
	 */
	public int getBreachLv(int lv) {
		return breachLvs[lv-1];
	}

	public int getStrengthNum(int id) {
		return strengthMaps.getOrDefault(id, 0);
	}

	public void strength(int id) {
		this.strengthMaps.put(id, getStrengthNum(id)+1);
	}
	/**
	 * 突破
	 * @param id
	 * @return false-只增加突破进度，true-突破成功
	 */
	public boolean breach(int id) {
		int oldLv = this.breachLvs[id-1];
		//增加突破进度
		this.breachProgress[id-1] = this.breachProgress[id-1]+1;
		BuildConfig buildConfig = SpringUtil.getBean(BuildConfig.class);
		TankStrengthBreakTemplate template = buildConfig.getBreakTemplate(id, oldLv);
		if(template==null){
			return false;
		}
		if(this.breachProgress[id-1]>=template.getUpgrade_num()){//突破进度满了则增加突破等级
			this.breachLvs[id-1]=this.breachLvs[id-1]+1;
			//同时进度清0
			this.breachProgress[id-1] = 0;
			return true;
		}
		return false;
	}

	public Map<Integer, Integer> getStrengthMaps() {
		return strengthMaps;
	}
	
	public int getStrengthCount() {
		return strengthMaps.values().stream().mapToInt(t->t).sum();
	}
	

}
