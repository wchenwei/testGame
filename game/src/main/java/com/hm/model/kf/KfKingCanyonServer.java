package com.hm.model.kf;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

/**
 * @Description: 跨服远征数据
 * @author siyunlong  
 * @date 2019年8月1日 下午3:45:41 
 * @version V1.0
 */
@NoArgsConstructor
@Data
public class KfKingCanyonServer{
	@Id
	private int serverId;
	private Set<Long> playerIds = Sets.newHashSet();
	private long maxCombat;
	private long averageCombat;
	private long score;
	
	public void saveDB() {
		MongoUtils.getLoginMongodDB().save(this);
	}
	public KfKingCanyonServer(int serverId, Set<Long> playerIds) {
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
		return this.playerIds.size() >= 10;
	}
	
	public static void main(String[] args) {
		List<KfKingCanyonServer> serverList = MongoUtils.getLoginMongodDB().queryAll(KfKingCanyonServer.class);
		for (KfKingCanyonServer kfKingCanyonServer : serverList) {
			kfKingCanyonServer.calTotalCombat();
		}
	}
}
