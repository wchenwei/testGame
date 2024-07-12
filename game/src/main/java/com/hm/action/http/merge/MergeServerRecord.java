package com.hm.action.http.merge;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.enums.PlayerTitleType;
import com.hm.libcore.mongodb.MongoUtils;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class MergeServerRecord {
	@Id
	private int serverId;
	//当前服务器的大总统和
	private Map<Integer, Long> titleData = Maps.newHashMap();
	private Date openDate;
	
	private int worldLv;//世界等级
	private long worldBuildExp;//世界建筑经验
	private int worldBossLv;
	private int baseNpcLv;//初始npc等级
	private boolean isReward;//是否已经补偿过了
	public List<Integer> functions;//功能列表

    public Set<Long> getTitlePlayers() {
		Set<Long> ids = Sets.newHashSet();
		for (Map.Entry<Integer, Long> entry : titleData.entrySet()) {
			if(entry.getKey() == PlayerTitleType.COMMANDER.getType() || 
					entry.getKey() == PlayerTitleType.PRESIDENT.getType()) {
				ids.add(entry.getValue());
			}
		}
		return ids;
	}
	
	public long getTitlePlayerId(PlayerTitleType playerTitleType) {
		return this.titleData.getOrDefault(playerTitleType.getType(), 0L);
	}
	
	public void saveDB() {
		MongoUtils.getLoginMongodDB().save(this);
	}
	
	public long getOpenLong() {
		if(openDate == null) {
			return System.currentTimeMillis();
		}
		return openDate.getTime();
	}
}
