package com.hm.war.sg.unit;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * @Description: 统计引擎
 * @author siyunlong  
 * @date 2018年10月18日 上午11:02:51 
 * @version V1.0
 */
@Getter
public class StatisticsEngine {
	private long atkHurt;//打出去的伤害
	private long bearHurt;//承受的伤害
	//杀死的坦克列表
	private List<Integer> killTank = Lists.newArrayList();
	
	public void addAtkHurt(long hurt) {
		this.atkHurt += hurt;
	}
	public void addBearHurt(long hurt) {
		this.bearHurt += hurt;
	}


	
	public void addKillTank(int tankId) {
		this.killTank.add(tankId);
	}
	public int getKillTankNum() {
		return this.killTank.size();
	}
}
