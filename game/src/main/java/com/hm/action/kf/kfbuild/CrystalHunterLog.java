package com.hm.action.kf.kfbuild;

import com.hm.libcore.util.date.DateUtil;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.util.RandomUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 水晶猎人日志
 * @author siyunlong  
 * @date 2020年8月11日 下午8:18:04 
 * @version V1.0
 */
@Data
@NoArgsConstructor
public class CrystalHunterLog {
	private String id;
	private long playerId;
	private String name;
	private long time;
	private long val;
	private int typeId;
	private int mark;//是否被标记
	
	public CrystalHunterLog(long playerId,int typeId,long val) {
		this.id = playerId+"_"+DateUtil.today()+"_"+RandomUtils.randomInt(100);
		this.val = val;
		this.playerId = playerId;
		PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(playerId);
		this.name = playerRedisData.getName();
		this.typeId = typeId;
		this.time = System.currentTimeMillis();
		this.mark = 1;
	}
	
	
	public void saveDB() {
		HunterRedisUtils.saveDB(this);
	}
	
	public static void main(String[] args) {
		new CrystalHunterLog(600062, 1, 10000);
		new CrystalHunterLog(600061, 1, 10000);
		new CrystalHunterLog(600060, 1, 10000);
		new CrystalHunterLog(600059, 1, 10000);
	}
	
}
