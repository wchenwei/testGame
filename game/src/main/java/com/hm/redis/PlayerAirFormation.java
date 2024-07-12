package com.hm.redis;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.redis.type.RedisTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAirFormation {
	private long playerId;
	private Map<Integer,Formation> airFormations = Maps.newConcurrentMap();
	
	public PlayerAirFormation(long playerId) {
		super();
		this.playerId = playerId;
	}

	public void addFormation(int id, Formation formation) {
		this.airFormations.put(id, formation);
	}

	public void disbandAirFromation(int id) {
		this.airFormations.remove(id);
	}
	public Formation getFormation(int id) {
		return airFormations.get(id);
	}
	
	public boolean isEmpty() {
		return airFormations.isEmpty();
	}
	
	public void updateRedis() {
		if(isEmpty()) {
			RedisTypeEnum.PlayerAircraftFormation.del(playerId);
		}else{
			RedisTypeEnum.PlayerAircraftFormation.put(playerId, GSONUtils.ToJSONString(this));
		}
	}
	
	public static PlayerAirFormation getOrCreate(long playerId) {
		String json = RedisTypeEnum.PlayerAircraftFormation.get(String.valueOf(playerId));
		if(StrUtil.isEmpty(json)) {
			return new PlayerAirFormation(playerId);
		}
		return GSONUtils.FromJSONString(json, PlayerAirFormation.class);
	}
	
	public static PlayerAirFormation getPlayerFormation(long playerId) {
		return GSONUtils.FromJSONString(RedisTypeEnum.PlayerAircraftFormation.get(String.valueOf(playerId)), PlayerAirFormation.class);
	}

	public void checkUpdateAirFormation(List<String> aircraftList) {
		if(aircraftList.isEmpty()) {
			RedisTypeEnum.PlayerAircraftFormation.del(playerId);
			return;
		}
		List<Integer> keys = Lists.newArrayList(airFormations.keySet());
		for(int id:keys) {
			Formation formation = getFormation(id);
			if(formation==null) {
				disbandAirFromation(id);
				continue;
			}
			List<String> list = formation.getAirs().stream().filter(t ->aircraftList.contains(t)).collect(Collectors.toList());
			if(list.isEmpty()) {
				disbandAirFromation(id);
			}else {
				addFormation(id, new Formation(list));
			}
		}
		//更新redis
		updateRedis();
		
	}
	
}
