package com.hm.servercontainer.mission;

import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.model.player.Player;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@RedisMapperType(type = MapperType.STRING_HASH)
public class MissionRecord extends BaseEntityMapper<String> {
	private MissionTopRecord firstRecord;
	private MissionTopRecord minCombatRecord;
	
	public MissionRecord(String id,int serverId) {
		setId(id);
		setServerId(serverId);
	}

	public void checkTopRecord(Player player,long combat,String tanks) {
		boolean isChange = false;
		if(firstRecord == null) {
			this.firstRecord = new MissionTopRecord(player,combat, tanks);
			isChange = true;
		}
		if(this.minCombatRecord == null || combat < this.minCombatRecord.getCombat()) {
			this.minCombatRecord = new MissionTopRecord(player,combat, tanks);
			isChange = true;
		}
		if(isChange) {
			saveDB();
		}
	}

	public MissionTopRecord getFirstRecord() {
		return firstRecord;
	}

	public MissionTopRecord getMinCombatRecord() {
		return minCombatRecord;
	}
}
