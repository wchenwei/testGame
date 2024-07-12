package com.hm.model.cityworld;

import org.springframework.data.annotation.Transient;

/**
 * 
 * @Description: 城市NPC
 * @author siyunlong  
 * @date 2018年11月3日 上午2:51:06 
 * @version V1.0
 */
public class CityNpc extends CityComponent{
	private int npcIndex;//当前生成npc的索引
	@Transient
	private long nextCreateNpcTime;//最后一次生成npc的时间
	
	public boolean isCanCreateNpc() {
		return System.currentTimeMillis() > nextCreateNpcTime;
	}
	public void setNextCreateNpcTime(long nextCreateNpcTime) {
		this.nextCreateNpcTime = nextCreateNpcTime;
	}
	public void setNpcIndex(int npcIndex) {
		this.npcIndex = npcIndex;
		SetChanged();
	}
	public int getNpcIndex() {
		return npcIndex;
	}
	
}
