package com.hm.action.guild.vo;

import com.google.common.collect.Lists;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankVo;
import com.hm.model.worldtroop.WorldTroop;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
@NoArgsConstructor
@Data
public class TroopVo {
	private String id;
	private List<TankVo> tankList = Lists.newArrayList();
	private int state;//当前状态
	private int troopPosition;//当前位置
	private int cityId;//当前所在城市
	private LinkedList<Integer> wayList = Lists.newLinkedList();
	private int index;
	private long secondAdd;//每秒回复速率
	
	public TroopVo(Player player,WorldTroop worldTroop) {
		if(worldTroop == null){
			return;
		}
		this.id = worldTroop.getId();
		this.tankList = worldTroop.getTroopArmy().getTankList().stream().map(tankArmy -> {
			Tank tank = player.playerTank().getTank(tankArmy.getId());
			TankVo tankVo = tank.createTankVo();
			tankVo.setHp(tankArmy.getHp());
			return tankVo;
		}).collect(Collectors.toList());
		this.state = worldTroop.getState();
		this.cityId = worldTroop.getCityId();
		this.wayList = Lists.newLinkedList(worldTroop.getTroopWay().getWayList());
		this.index = worldTroop.getTroopInfo().getIndex();
		this.troopPosition = worldTroop.getTroopPosition();
		TroopBiz troopBiz = SpringUtil.getBean(TroopBiz.class);
		this.secondAdd = troopBiz.calTroopRecoveSecondHp(player, worldTroop);
	}
}
