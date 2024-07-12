package com.hm.servercontainer.worldbuild;

import com.google.common.collect.Lists;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.Player;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
@RedisMapperType(type = MapperType.STRING_HASH)
public class PlayerWorldBuild extends BaseEntityMapper<Long> {
	//每只部队所在的阵营信息
	private ConcurrentHashMap<String,WorldBuildTroop> troopCampMap = new ConcurrentHashMap<>();
	private long[] campScores = new long[3];
	
	public void addCampScores(int campId,int score) {
		this.campScores[campId-1] += score;
	}
	public long getCampScore(int campId) {
		return this.campScores[campId-1];
	}
	
	public PlayerWorldBuild(Player player) {
		setId(player.getId());
		setServerId(player.getServerId());
	}
	
	public void addTroop(List<String> ids,int campId, int cityId,long conuteSecond) {
		for (String id : ids) {
			this.troopCampMap.put(id, new WorldBuildTroop(campId, cityId,id,conuteSecond));
		}
	}
	
	public void saveDB() {
		RedisMapperUtil.update(this);
	}
	
	public boolean isContainTroop(String troopId) {
		return this.troopCampMap.containsKey(troopId);
	}
	public WorldBuildTroop getWorldBuildTroop(String troopId) {
		return this.troopCampMap.get(troopId);
	}

	public ConcurrentHashMap<String, WorldBuildTroop> getTroopCampMap() {
		return troopCampMap;
	}
	public List<WorldBuildTroop> getWorldBuildTroopList() {
		return Lists.newArrayList(this.troopCampMap.values());
	}
	public WorldBuildTroop removeBuildTroop(String troopId) {
		return this.troopCampMap.remove(troopId);
	}
	public List<WorldBuildTroop> removeBuildTroops(List<String> troopIds) {
		List<WorldBuildTroop> removeList = Lists.newArrayList();
		for (String id : troopIds) {
			WorldBuildTroop worldBuildTroop = removeBuildTroop(id);
			if(worldBuildTroop != null) {
				removeList.add(worldBuildTroop);
			}
		}
		return removeList;
	}
	
	public List<WorldBuildTroop> removeBuildTroops(int campId) {
		List<WorldBuildTroop> removeList = Lists.newArrayList();
		for (WorldBuildTroop worldBuildTroop : Lists.newArrayList(troopCampMap.values())) {
			if(worldBuildTroop.getCampId() == campId) {
				removeList.add(removeBuildTroop(worldBuildTroop.getId()));
			}
		}
		return removeList;
	}
	
	public void clearTroop() {
		this.troopCampMap.clear();
	}
}
