package com.hm.model.guild;

import com.hm.model.cityworld.EnemyPack;

/**
 * @Description: 部落宣战信息
 * @author siyunlong  
 * @date 2018年12月20日 上午10:07:18 
 * @version V1.0
 */
public class GuildEnemyPack extends GuildComponent{
	private EnemyPack enemyPack = new EnemyPack();
	
	public void addEnemy(int guildId,int effectMinute) {
		this.enemyPack.addEnemy(guildId,effectMinute);
		SetChanged();
	}
	public boolean isEnemy(int guildId) {
		checkEnemy();
		return this.enemyPack.isEnemy(guildId);
	}
	
	public EnemyPack getEnemyPack() {
		checkEnemy();
		return enemyPack;
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
