package com.hm.action.kf.pk;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ServerLevelReward {
	private int serverId;
	private Map<Integer,List<Long>> typeMap = Maps.newConcurrentMap();
	
	public ServerLevelReward(int serverId) {
		super();
		this.serverId = serverId;
	}

	public void addPlayer(long id,int type) {
		List<Long> ids = this.typeMap.get(type);
		if(ids == null) {
			ids = Lists.newArrayList();
			this.typeMap.put(type, ids);
		}
		ids.add(id);
	}
}
