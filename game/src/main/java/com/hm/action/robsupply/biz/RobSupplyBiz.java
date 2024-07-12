package com.hm.action.robsupply.biz;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.SupplyConfig;
import com.hm.enums.NpcConfType;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.SupplyTroopType;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerRobSupply;
import com.hm.model.player.SupplyItem;
import com.hm.model.supplytroop.SupplyContants;
import com.hm.model.supplytroop.SupplyTroop;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.supplyTroop.SupplyTroopServerContainer;
import com.hm.war.sg.troop.TankArmy;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Biz
public class RobSupplyBiz implements IObserver{

	@Resource
	private SupplyConfig supplyConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private NpcConfig npcConfig;
	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		if ((int)argv[0] == PlayerFunctionType.RobSupply.getType()) {
			dayReset(player);
        }
	}
	
	public ArrayList<TankArmy> createTankArmys(String armys,Player player) {
		ArrayList<TankArmy> tankList = Lists.newArrayList();
		for (String str : armys.split(",")) {
			TankArmy tankArmy = new TankArmy(Integer.parseInt(str.split(":")[0]), Integer.parseInt(str.split(":")[1]));
//			long tankHp = player.playerRobSupply().getTankHp(tankArmy.getId());
//			if(tankHp >= 0) {
//				tankArmy.setHp(tankHp);
//			}else{
//			}
			tankArmy.setFullHp();
			tankList.add(tankArmy);
		}
		return tankList;
	}
	
	public void dayReset(Player player) {
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.RobSupply)) {
			return;
		}
		PlayerRobSupply playerRobSupply= player.playerRobSupply();
		//重置坦克血量
		playerRobSupply.dayReset();
		//刷新护送列表 暂时屏蔽
//		refershSupplyList(player);
		//刷新偵察列表
//		refreshSupplyEnemyList(player);
	}
	
	/**
	 * 护送登录检查
	 * @param player
	 */
	public void doPlayerLogin(Player player) {
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.RobSupply)) {
			return;
		}
		PlayerRobSupply playerRobSupply= player.playerRobSupply();
		for (SupplyItem supplyItem : playerRobSupply.getSupplyList()) {
			if(supplyItem.isOver()) {
				supplyItem.changeSupplyType(SupplyTroopType.Over);
				player.playerRobSupply().SetChanged();
			}
		}
		//检查部队
		Set<Integer> tankIds = SupplyTroopServerContainer.of(player).getSupplyTroopByPlayer(player)
		.stream().flatMap(e -> e.getTankList().stream()).map(e -> e.getId()).collect(Collectors.toSet());
		Set<Integer> disTankIds = Sets.newHashSet(playerRobSupply.getDispatchTanks());
		if(tankIds.size() != disTankIds.size()) {
			playerRobSupply.reloadDispatchTanks(tankIds);
		}
	}
	
	/**
	 * 刷新护送列表
	 * @param player
	 */
	public void refershSupplyList(Player player) {
		PlayerRobSupply playerRobSupply= player.playerRobSupply();
		//删除所有空闲的
		dayResetSupplyItem(player);
		//生成新的订单
		int MaxSupplyNum = 5;
		List<SupplyItem> newList = supplyConfig.randomSupplyItem(player, MaxSupplyNum-playerRobSupply.getSupplyList().size());
		playerRobSupply.addSupplyItem(newList);
	}
	
	private void dayResetSupplyItem(Player player) {
		List<SupplyItem> supplyList = player.playerRobSupply().getSupplyList();
		for (int i = supplyList.size()-1; i >= 0; i--) {
			SupplyItem item = supplyList.get(i);
			if(item.getType() == SupplyTroopType.None.getType()) {
				supplyList.remove(i);
			}
		}
	}
	
	/**
	 * 检查坦克状态是否可以创建部队
	 * @param player
	 * @param troopId
	 * @param tankArmys
	 * @return
	 */
	public boolean checkTankArmyState(Player player,String troopId,List<TankArmy> tankArmys) {
		List<String> playerTroopIds = Lists.newArrayList(player.playerRobSupply().getTroopIdList());
		if(StrUtil.isNotEmpty(troopId) && !playerTroopIds.remove(troopId)) {
			return false;
		}
		//当前我的部队
		List<SupplyTroop> worldTroops = SupplyTroopServerContainer.of(player).getSupplyTroops(playerTroopIds);
		for (TankArmy tankArmy : tankArmys) {
			if(tankArmy.getHp() == 0) {
				return false;
			}
			//当前部队都不包含此坦克
			if(worldTroops.stream().anyMatch(e -> e.isContainTankId(tankArmy.getId()))) {
				return false;
			}
		}
		return true;
	}
	
	public List<SupplyTroopVo> createSupplyEnemyList(Player player) {
		PlayerRobSupply playerRobSupply= player.playerRobSupply();
		List<String> enemyIdList = playerRobSupply.getEnemyList();
		List<SupplyTroopVo> voList = Lists.newArrayList();
		for (String id : enemyIdList) {
			if(isNpc(id)) {
				NpcRobSupply npcRobSupply = playerRobSupply.getNpcRobSupply(id);
				if(npcRobSupply != null) {
					voList.add(new SupplyTroopVo(npcRobSupply));
				}
			}else{
				SupplyTroop supplyTroop = SupplyTroopServerContainer.of(player).getSupplyTroop(id);
				if(supplyTroop != null && !supplyTroop.isRob()) {
					voList.add(new SupplyTroopVo(supplyTroop));
				}
			}
		}
		return voList;
	}
	
	/**
	 * 偵察刷新
	 * @param player
	 */
	public void refreshSupplyEnemyList(Player player) {
		PlayerRobSupply playerRobSupply= player.playerRobSupply();
		//获取我的敌人部队列表
		List<SupplyTroop> enemyList = SupplyTroopServerContainer.of(player).randomSupplyTroop(player, SupplyContants.SupplyEnemyNum);
		List<String> enemyIdList = enemyList.stream().map(e -> e.getId()).collect(Collectors.toList());
		int npcSize = SupplyContants.SupplyEnemyNum - enemyIdList.size();
		if(npcSize > 0) {
			Map<String,NpcRobSupply> npcMap = createNpcSupply(player, npcSize);
			playerRobSupply.setNpcMap(npcMap);
			enemyIdList.addAll(npcMap.values().stream().map(e -> e.getId()).collect(Collectors.toList()));
		}
        playerRobSupply.setEnemyList(enemyIdList);
	}
	
	/**
	 * 随机npc
	 * @param player
	 * @return
	 */
	public Map<String,NpcRobSupply> createNpcSupply(Player player,int size) {
		Map<String,NpcRobSupply> npcMap = Maps.newHashMap();
		List<Integer> npcIds = npcConfig.randomNpcList(NpcConfType.SupplyNpc, size);
		List<SupplyItem> itemList = supplyConfig.randomSupplyNpcItem(player, size);
		for (int i = 0; i < npcIds.size(); i++) {
			String id = "npc"+i;
			npcMap.put(id, new NpcRobSupply(id,npcIds.get(i),itemList.get(i)));
		}
		return npcMap;
	}
	
	public boolean isNpc(String id) {
		return id.startsWith("npc");
	}
}
