package com.hm.model.serverpublic.serverpower;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.serverData.vo.PunishPlayerVo;
import com.hm.action.serverData.vo.PunishVo;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.redis.util.RedisUtil;
import com.hm.util.ServerUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Punish {
	//key:制裁玩家id  value:制裁截止时间
	private Map<Integer,Long> punishs = Maps.newConcurrentMap();

	public void addPunish(int id) {
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		this.punishs.put(id, System.currentTimeMillis()+commValueConfig.getCommValue(CommonValueType.PresidentPower_Punish_Time)*GameConstants.DAY);
	}

	public void clearPunish() {
		this.punishs.clear();
	}

	public boolean isBePunished(long playerId) {
		return punishs.getOrDefault(playerId, 0l)>System.currentTimeMillis();
	}

	public PunishVo createVo() {
		List<PunishPlayerVo> vos = Lists.newArrayList();
		for(Map.Entry<Integer, Long> entry:punishs.entrySet()){
			PunishPlayerVo vo = new PunishPlayerVo();
			vo.load(RedisUtil.getPlayerRedisData(entry.getKey()));
			vo.loadGuild(ServerUtils.getServerId(vo.getPlayerId()));
			vo.setEndTime(entry.getValue());
			vos.add(vo);
		}
		return new PunishVo(vos);
	}

}
