package com.hm.model.kf;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.util.date.DateUtil;
import lombok.Data;

import java.util.Date;

/**
 * @Description: 跨服远征数据
 * @author siyunlong  
 * @date 2019年8月1日 下午3:45:41 
 * @version V1.0
 */
@Data
public class KfExpeditionServer extends DBEntity<Integer>{
	private int atkId;//攻击方
	private int score;
	private int type;// 0-无  1-攻击 2-防御
	private long combat;
	private int groupId;
	private int lastId;//上次战斗id

	private long openTime;//第一次开服时间

	public void addScore(int add,int minScore) {
		this.score += add;
		this.score = Math.max(minScore, this.score);
	}

	public int getOpenDay() {
		return (int) DateUtil.betweenDay(new Date(openTime), new Date(), true);
	}

	public boolean isCanJoinKF() {
		return getOpenDay() >= 7;
	}

	public boolean isCombatRandom() {
		return getOpenDay() >= 90;
	}

	public void saveDB() {
		MongoUtils.getLoginMongodDB().save(this);
	}
}
