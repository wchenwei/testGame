package com.hm.model.activity.kfactivity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "KfKingServerGroup")
public class KfKingServerGroup extends BaseKfServerGroup{
	private Map<Integer,List<Long>> playerIds = Maps.newHashMap();
	
	public KfKingServerGroup(List<Integer> serverIds,String url) {
		super(serverIds, url);
	}
	
	public void putPlayerIds(int serverId,Collection<Long> playerIds) {
		this.playerIds.put(serverId, Lists.newArrayList(playerIds));
	}
	
	public static void main(String[] args) {
		new KfKingServerGroup(Lists.newArrayList(6,7), "127.0.0.1").saveDB();
	}
}
