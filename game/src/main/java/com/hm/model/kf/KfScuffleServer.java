package com.hm.model.kf;

import com.google.common.collect.Lists;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * @Description: 跨服乱斗服务器数据
 * @author xjt  
 * @date 2020年11月10日16:02:44
 * @version V1.0
 */
@NoArgsConstructor
@Data
public class KfScuffleServer{
	@Id
	private int serverId;
	private List<Long> playerIds = Lists.newArrayList();
	private long maxCombat;
	private long averageCombat;
	private long score;
	private int openDay;
	
	public void saveDB() {
		MongoUtils.getLoginMongodDB().save(this);
	}
	public KfScuffleServer(int serverId, List<Long> playerIds) {
		super();
		this.serverId = serverId;
		this.playerIds = playerIds;
	}
	
	public void calTotalCombat() {
		List<PlayerRedisData> redisPlayerList = RedisUtil.getListPlayer(Lists.newArrayList(playerIds));
		this.maxCombat = redisPlayerList.stream().mapToLong(e -> e.getCombat()).max().orElse(0);
		this.averageCombat = (long)redisPlayerList.stream().mapToLong(e -> e.getCombat()).average().orElse(0);
		this.score = (long)(this.maxCombat*0.7+this.averageCombat*0.3);
	}
	
	public boolean isCanStart() {
		return true;
	}
	public int getPlayerNum() {
		return this.playerIds.size();
	}
	public static void main(String[] args) {
		List<KfScuffleServer> serverList = MongoUtils.getLoginMongodDB().queryAll(KfScuffleServer.class);
		for (KfScuffleServer kfKingCanyonServer : serverList) {
			kfKingCanyonServer.calTotalCombat();
		}
	}
}
