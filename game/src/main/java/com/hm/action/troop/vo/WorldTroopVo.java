package com.hm.action.troop.vo;

import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.war.sg.troop.TankArmy;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;

@Data
public class WorldTroopVo {
	private String id;
	private ArrayList<TankArmy> armyList;
	private int state;//当前状态
	private int troopPosition;//当前位置
	private int cityId;//当前所在城市
	private LinkedList<Integer> wayList = Lists.newLinkedList();
	private int index;
	private long secondAdd;//每秒回复速率
	private int worldType;
	private long advanceSkillTime;
	private int formationId;
	
	public WorldTroopVo(Player player,WorldTroop worldTroop) {
		this.id = worldTroop.getId();
		this.armyList = worldTroop.getTroopArmy().getTankList();
		this.state = worldTroop.getState();
		this.cityId = worldTroop.getCityId();
		this.wayList = Lists.newLinkedList(worldTroop.getTroopWay().getWayList());
		this.index = worldTroop.getTroopInfo().getIndex();
		this.troopPosition = worldTroop.getTroopPosition();
		TroopBiz troopBiz = SpringUtil.getBean(TroopBiz.class);
		this.secondAdd = troopBiz.calTroopRecoveSecondHp(player, worldTroop);
		this.worldType = worldTroop.getWorldType();
		this.advanceSkillTime = worldTroop.getTroopTemp().getAdvanceSkillTime();
		this.formationId = worldTroop.getTroopInfo().getFormationId();
	}

	public WorldTroopVo() {
		super();
	}

}
