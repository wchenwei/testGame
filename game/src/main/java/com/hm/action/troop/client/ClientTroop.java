package com.hm.action.troop.client;

import com.hm.libcore.msg.JsonMsg;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.model.player.Player;
import com.hm.util.ArmyUtils;
import com.hm.util.StringUtil;
import com.hm.war.sg.troop.TankArmy;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 客户端上传后构建部队配置
 * @author siyunlong  
 * @date 2020年12月22日 下午5:17:53 
 * @version V1.0
 */
@Data
public class ClientTroop {
	private List<TankArmy> armyList;
	private int aircraftId;
	
	public ClientTroop setArmyList(List<TankArmy> armyList) {
		this.armyList = armyList;
		return this;
	}
	public ClientTroop setAircraftId(int airplanId) {
		this.aircraftId = airplanId;
		return this;
	}
	
	public static ClientTroop build() {
		return new ClientTroop();
	}
	
	//满血
	public static ClientTroop build(String troopInfo) {
		ClientTroop clientTroop = new ClientTroop();
		String[] arrays = troopInfo.split("#");
		clientTroop.setArmyList(ArmyUtils.createTankArmys(arrays[0]));
		if(arrays.length > 1) {
			clientTroop.setAircraftId(Integer.parseInt(arrays[1]));
		}
		return clientTroop;
	}
	
	public static ClientTroop build(List<TankArmy> tanks,int aircraftId) {
		return new ClientTroop().setAircraftId(aircraftId).setArmyList(tanks);
	}
	public static ClientTroop buildFull(Player player,JsonMsg msg) {
		return buildFull(player, "troops", msg);
	}
	//附带血量
	public static ClientTroop build(Player player,String troopKey,JsonMsg msg) {
		return ClientTroop.build()
				.setArmyList(TroopBiz.createTankArmys(msg.getString(troopKey), player, false))
				.setAircraftId(msg.getInt("airplanId"));
	}
	public static ClientTroop buildFull(Player player,String troopKey,JsonMsg msg) {
		return ClientTroop.build()
				.setArmyList(TroopBiz.createTankArmys(msg.getString(troopKey), player, true))
				.setAircraftId(msg.getInt("airplanId"));
	}
	
	public static ClientTroop build(List<TankArmy> tanks) {
		return ClientTroop.build().setArmyList(tanks);
	}
	/**
	 * 
	 * @param player
	 * @param armys 格式 index:tankId,index:tankId,index:tankId#airId
	 * @return
	 */
	public static ClientTroop buildFull(Player player, String armys) {
		return ClientTroop.build()
				.setArmyList(TroopBiz.createTankArmys(armys.split("#")[0], player, true))
				.setAircraftId(StringUtil.strToIntId(armys.split("#")[1]));
	}

	public String toTroopStr() {
		String tankInfo = armyList.stream().map(e -> e.getIndex()+":"+e.getId()).collect(Collectors.joining(","));
		if(aircraftId > 0) {
			tankInfo = tankInfo +"#"+this.aircraftId;
		}
		return tankInfo;
	}
}
