package com.hm.model.supplytroop;

import com.google.common.collect.Lists;
import com.hm.action.troop.client.ClientTroop;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import com.hm.model.player.Player;
import com.hm.model.player.SupplyItem;
import com.hm.war.sg.troop.TankArmy;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 玩家补给部队
 * @author siyunlong  
 * @date 2019年1月24日 上午11:22:33 
 * @version V1.0
 */
@Getter
@Setter
@RedisMapperType(type = MapperType.STRING_HASH)
public class SupplyTroop extends BaseEntityMapper<String> {
	private long playerId;
	//开始时间
	private long endTime;
	//出站的坦克列表
	private ConcurrentHashMap<Integer,TankArmy> tankMap = new ConcurrentHashMap<>();
	//
	private int formationId;
	//补给
	private SupplyItem supplyItem;
	//是否被掠夺过
	private boolean isRob;
	
	public void createLoadPlayer(Player player,SupplyItem supplyItem,long endTime) {
		setServerId(player.getServerId());
		setId(supplyItem.getId());
		this.playerId = player.getId();
		this.endTime = endTime;
		this.supplyItem = supplyItem;
	}
	private int getWaySize() {
		return supplyItem.getWaySize();
	}
	
	public void changeTankList(List<TankArmy> tankList) {
		this.tankMap = new ConcurrentHashMap<>(tankList.stream().collect(Collectors.toMap(TankArmy::getId, Function.identity())));
	}
	
	public boolean isContainTankId(int tankId) {
		return tankMap.containsKey(tankId);
	}
	public List<TankArmy> getTankList() {
		return Lists.newArrayList(tankMap.values());
	}
	
	public void saveDB() {
		RedisMapperUtil.update(this);
	}
	public void loadDefTroop(ClientTroop clientTroop) {
		changeTankList(clientTroop.getArmyList());
		setFormationId(clientTroop.getAircraftId());
	}
}
