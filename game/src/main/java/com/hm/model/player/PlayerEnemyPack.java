package com.hm.model.player;

import com.hm.model.cityworld.EnemyPack;

/**
 * @Description: 玩家敌方信息
 * @author siyunlong  
 * @date 2018年12月20日 上午9:58:08 
 * @version V1.0
 */
public class PlayerEnemyPack extends PlayerDataContext {
	private EnemyPack enemyPack = new EnemyPack();
	
	public void addEnemy(int guildId,int effectMinute) {
		if(guildId < 0) {
			return;//个人无法对中立宣战
		}
		this.enemyPack.addEnemy(guildId,effectMinute);
		SetChanged();
	}
	public EnemyPack getEnemyPack() {
		checkEnemy();
		return enemyPack;
	}
	
	public boolean isEnemy(int guildId) {
		checkEnemy();
		return this.enemyPack.isEnemy(guildId);
	}
	
	public void checkEnemy() {
		if(enemyPack.checkEnemy()) {
			SetChanged();
		}
	}
	
	public void clear() {
		this.enemyPack.clear();
		SetChanged();
	}
}
