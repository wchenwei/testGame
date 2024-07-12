package com.hm.model.activity.kfactivity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Document(collection = "KfScuffleServerGroup")
public class KfScuffleServerGroup extends BaseKfServerGroup{
	private Map<Integer,List<Long>> playerIds = Maps.newHashMap();
	private Map<Integer,Long> initRes =Maps.newConcurrentMap();
	
	public KfScuffleServerGroup(List<Integer> serverIds,String url) {
		super(serverIds, url);
	}
	
	public void putPlayerIds(int serverId,Collection<Long> playerIds) {
		this.playerIds.put(serverId, Lists.newArrayList(playerIds));
	}

	public void initRes() {
		int minServerId=0;
		int diff = 0;
		int minNum = 10000;
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		long res = commValueConfig.getCommValue(CommonValueType.KFScuffle_Res_Init);
		for(Map.Entry<Integer, List<Long>> entry: playerIds.entrySet()) {
			int serverId = entry.getKey();
			int num = entry.getValue().size();
			if(num<minNum) {
				minServerId = serverId;
				minNum = num;
			}
			diff = Math.abs(num-diff);
			initRes.put(serverId, res);
		}
		//给人数少的server重新初始化资源
		initRes.put(minServerId, res+diff*res/playerIds.get(minServerId).size());
		
	}
	
}
