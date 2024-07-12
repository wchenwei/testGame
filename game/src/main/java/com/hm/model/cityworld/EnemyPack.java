package com.hm.model.cityworld;

import com.hm.config.GameConstants;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 敌人部落列表
 * @author siyunlong  
 * @date 2018年12月20日 上午9:27:33 
 * @version V1.0
 */
public class EnemyPack {
	//key:部落id   value:截止时间
	private ConcurrentHashMap<Integer,Long> enemyMap = new ConcurrentHashMap<>();
	
	public void addEnemy(int guildId,int effectMinute) {
		if(effectMinute <= 0) {
			this.enemyMap.remove(guildId);
			return;
		}
		long endTime = effectMinute*GameConstants.MINUTE+System.currentTimeMillis();
		if(endTime > this.enemyMap.getOrDefault(guildId, 0L)) {
			this.enemyMap.put(guildId, endTime);
		}
		
	}
	public ConcurrentHashMap<Integer, Long> getEnemyMap() {
		return enemyMap;
	}

	public boolean isEnemy(int guildId) {
		return this.enemyMap.containsKey(guildId);
	}
	
	public boolean checkEnemy() {
		boolean isChange = false;
		for (Map.Entry<Integer,Long> entry : enemyMap.entrySet()) {
			if(entry.getValue() < System.currentTimeMillis()) {
				enemyMap.remove(entry.getKey());
				isChange = true;
			}
		}
		return isChange;
	}
	
	public void clear() {
		this.enemyMap.clear();
	}
}
