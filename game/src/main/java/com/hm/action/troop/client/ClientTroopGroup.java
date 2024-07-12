package com.hm.action.troop.client;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 客户端上传后构建部队配置组
 * @author siyunlong  
 * @date 2020年12月22日 下午5:17:53 
 * @version V1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientTroopGroup {
	private List<ClientTroop> troopList = Lists.newArrayList();
	
	public static ClientTroopGroup buildFull(Player player,JsonMsg msg) {
		return buildFull(player, "troops", msg);
	}
	
	
	public static ClientTroopGroup buildFull(Player player,String troopKey,JsonMsg msg) {
		//index:tankId,index:tankId#1;index:tankId,index:tankId#1;...
		String troops = msg.getString(troopKey);
		List<ClientTroop> troopList = Arrays.stream(troops.split(";"))
				.map(t -> ClientTroop.build(t))
				.collect(Collectors.toList());
		return ClientTroopGroup.build(troopList);
	}
	
	public static ClientTroopGroup build(String armyGroup) {
		//index:tankId,index:tankId#1;index:tankId,index:tankId#1;...
		List<ClientTroop> troopList = Arrays.stream(armyGroup.split(";"))
				.map(t -> ClientTroop.build(t))
				.collect(Collectors.toList());
		return ClientTroopGroup.build(troopList);
	}
	
	private static ClientTroopGroup build(List<ClientTroop> troopList) {
		return new ClientTroopGroup(troopList);
	}
}
