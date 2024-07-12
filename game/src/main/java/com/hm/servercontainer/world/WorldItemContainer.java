package com.hm.servercontainer.world;

import com.google.common.collect.Maps;
import com.hm.action.mission.biz.MissionRecordBiz;
import com.hm.enums.WorldType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.cityworld.WorldCity;
import com.hm.servercontainer.ItemContainer;
import com.hm.servercontainer.mission.MissionRecord;
import com.hm.servercontainer.record.PveMRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
public class WorldItemContainer extends ItemContainer{
	//世界数组
	private AbstractCityWorld[] worldMap = new AbstractCityWorld[WorldType.WorldList.length];
	//关卡记录
	private Map<String, MissionRecord> missonMap = Maps.newConcurrentMap();
	private Map<String, PveMRecord> pveRecordMap = Maps.newConcurrentMap();
	
	public WorldItemContainer(int serverId) {
		super(serverId);
	}
	
	@Override
	public void initContainer() {
		for (WorldType worldType : WorldType.WorldList) {
			AbstractCityWorld world = worldType.createCityWorld(getServerId());
			world.initWorld();
			this.worldMap[worldType.getType()] = world; 
		}
		initMissionRecord();
		initPveRecord();
	}
	
	public void initMissionRecord() {
		Map<String, MissionRecord> missonMap = Maps.newConcurrentMap();
		for (MissionRecord missionRecord : RedisMapperUtil.queryListAll(getServerId(), MissionRecord.class)) {
			missonMap.put(missionRecord.getId(), missionRecord);
		}
		this.missonMap = missonMap;
	}
	public void initPveRecord() {
		Map<String, PveMRecord> pveRecordMap = Maps.newConcurrentMap();
		for (PveMRecord record : PveMRecord.getPveMRecords(getServerId())) {
			pveRecordMap.put(record.getId(), record);
		}
		this.pveRecordMap = pveRecordMap;

		try {
			if(this.pveRecordMap.isEmpty() && !this.missonMap.isEmpty()) {
				SpringUtil.getBean(MissionRecordBiz.class)
						.missionRecordOldToBattle(getServerId(),this.missonMap,this.pveRecordMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public WorldCity getWorldCity(int id) {
		if(id <= 0) {
			return null;
		}
		return this.worldMap[WorldType.getTypeByCityId(id).getType()].getWorldCity(id);
	}
	
	public List<WorldCity> getWorldCitys(WorldType worldType) {
		return this.worldMap[worldType.getType()].getWorldCitys();
	}

	public Map<Integer,Integer> getWorldCityBelongGuild(WorldType worldType) {
		return this.worldMap[worldType.getType()].getWorldCityBelongGuild();
	}

	
	public MissionRecord getMissionRecord(String id) {
		MissionRecord record = this.missonMap.get(id);
		if(record == null) {
			record = new MissionRecord(id, getServerId());
			this.missonMap.put(id, record);
		}
		return record;
	}

	public PveMRecord getPveMRecord(String id) {
		return this.pveRecordMap.get(id);
	}
	public void addPveMRecord(PveMRecord record) {
		this.pveRecordMap.put(record.getId(),record);
	}
}
