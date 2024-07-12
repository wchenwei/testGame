package com.hm.servercontainer.centreArms;

import com.google.common.collect.Maps;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.CentreArms;
import com.hm.servercontainer.ItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class CentreArmsItemContainer extends ItemContainer{
	private Map<Long, CentreArms> dataMap = Maps.newConcurrentMap();
	
	public CentreArmsItemContainer(int serverId) {
		super(serverId);
	}
	@Override
	public void initContainer() {
		for(CentreArms troop: RedisMapperUtil.queryListAll(getServerId(),CentreArms.class)){
			this.dataMap.put(troop.getId(), troop);
		}
	}
	
	public CentreArms getCentreArms(long playerId) {
		return this.dataMap.get(playerId);
	}
	
	public void addCentreArms(CentreArms centreArms) {
		this.dataMap.put(centreArms.getId(), centreArms);
	}
	public Map<Long, CentreArms> getDataMap() {
		return dataMap;
	}
	
}












