package com.hm.action.overallWar.vo;

import com.google.common.collect.Maps;
import com.hm.enums.OverallWarTroopType;
import com.hm.model.player.Player;
import com.hm.model.player.SimplePlayerVo;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankVo;
import com.hm.war.sg.troop.TankArmy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OverallWarOpponentVo extends SimplePlayerVo {
	private String npcId;
	private Map<Integer,List<TankVo>> troops = Maps.newConcurrentMap();
	
	public OverallWarOpponentVo(Player player) {
		this.load(player);
		loadTroops(player);
	}
	public void loadTroops(Player player){
		Arrays.stream(OverallWarTroopType.values()).forEach(t ->{
			List<TankArmy> tanks = player.playerOverallWar().getTroop(t.getType());
			List<TankVo> tankVos = tanks.stream().map(tankArmy -> {
				Tank tank = player.playerTank().getTank(tankArmy.getId());
				return tank.createTankVo();
			}).collect(Collectors.toList());
			troops.put(t.getType(), tankVos);
		});
	}
	public OverallWarOpponentVo(String npcId){
		this.npcId = npcId;
	}
	public String getNpcId() {
		return npcId;
	}
	
}
