package com.hm.model.cityworld.troop;

import com.google.common.collect.Lists;
import com.hm.action.cityworld.vo.FightTroopVo;
import com.hm.action.cityworld.vo.SMovePlayerVo;
import com.hm.enums.CityTroopType;
import com.hm.enums.TroopState;
import com.hm.model.cityworld.IWorldCity;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.json.TroopJsonType;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.war.sg.troop.TankArmy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class ClonePlayerTroop extends BaseCityFightTroop{
	private long playerId;
	private int serverId;
	//复制的部队
	private ArrayList<TankArmy> armyList = Lists.newArrayList();
	//部队状态
	private int state = TroopState.None.getType();
	private int formationId;//航母部队id
	
	public ClonePlayerTroop(WorldTroop worldTroop) {
		super(CityTroopType.ClonePlayer,"clone_"+worldTroop.getId()+"_"+PrimaryKeyWeb.getPrimaryKey("clone", worldTroop.getServerId()));
		this.armyList = Lists.newArrayList(worldTroop.getTroopArmy().getCloneTankList());
		this.armyList.forEach(e -> e.setFullHp());
		this.playerId = worldTroop.getPlayerId();
		this.serverId = worldTroop.getServerId();
		this.formationId = worldTroop.getTroopInfo().getFormationId();
		setJt(TroopJsonType.ClonePlayer);
	}
	
	@Override
	public void changeState(TroopState state) {
		this.state = state.getType();
	}

	@Override
	public int getCityTroopState(WorldCity worldCity) {
		return this.state;
	}

	@Override
	public FightTroopVo createFightTroopVo(IWorldCity worldCity) {
		FightTroopVo vo = new FightTroopVo();
		vo.id = getId();
		vo.state = this.state;
		PlayerRedisData player = RedisUtil.getPlayerRedisData(this.playerId);
		vo.load(player);
		vo.loadGuild(this.serverId);
		vo.name = player.getName()+"-镜像";
		vo.titleId = 0;
		vo.serverId = this.serverId;
		return vo;
	}

	@Override
	public SMovePlayerVo createSMovePlayerVo(IWorldCity worldCity) {
		SMovePlayerVo vo = new SMovePlayerVo(this.playerId);
		vo.name = vo.getName()+"-镜像";
		return vo;
	}
}
