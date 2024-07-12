package com.hm.action.troop.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.action.bag.BagBiz;
import com.hm.action.citywarskill.biz.CityWarSkillBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.troop.client.ClientTroop;
import com.hm.action.troop.vo.FullTroopResult;
import com.hm.annotation.Broadcast;
import com.hm.config.GameConstants;
import com.hm.config.excel.ItemConfig;
import com.hm.enums.LogType;
import com.hm.enums.TankAttrType;
import com.hm.enums.TroopPosition;
import com.hm.libcore.rpc.IGameRpc;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.sysConstant.ItemConstant;
import com.hm.util.MathUtils;
import com.hm.war.sg.troop.TankArmy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WorldTroopBiz extends NormalBroadcastAdapter {
	@Resource
	private CityWarSkillBiz cityWarSkillBiz;
	@Resource
	private TankBiz tankBiz;
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private TroopBiz troopBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private BagBiz bagBiz;

	@Broadcast(ObservableEnum.PlayerLoginSuc)
	public void doPlayerLogin(ObservableEnum observableEnum, Player player, Object... argv) {
		//超过7天没有登录检查部队
		if(System.currentTimeMillis() - player.playerBaseInfo().getLastLoginDate().getTime() > 7* GameConstants.DAY) {
			doPlayerCreate(player);
		}
	}

	public void doPlayerCreate(Player player) {
		List<WorldTroop> troopList =  TroopServerContainer.of(player).getWorldTroopByPlayer(player);
		if(CollUtil.isNotEmpty(troopList)) {
			return;
		}
		List<Tank> tankList = tankBiz.getTankListTopCombat(player,1);
		if(CollUtil.isEmpty(tankList)) {
			return;
		}
		//一辆车到4位置
		List<TankArmy> tankArmyList = Lists.newArrayList();
		Tank tank = tankList.get(0);
		tankArmyList.add(new TankArmy(4,tank.getId(),player));
		ClientTroop clientTroop = ClientTroop.build(tankArmyList,0);
		createPlayerTroop(player,clientTroop);
	}

	//创建部队
	public void createPlayerTroop(Player player, ClientTroop clientTroop) {
		WorldTroop worldTroop = new WorldTroop();
		worldTroop.createLoadPlayer(player);
		worldTroop.loadClientTroop(clientTroop);
		TroopServerContainer.of(player).addWorldTroop(worldTroop);
		player.playerTroops().addTroopId(worldTroop.getId());

		player.notifyObservers(ObservableEnum.TroopChange);
	}

	
	//是否可以操作部队（不判断权限，只判断部队状态是否允许操作）
	public boolean isCanOperationTroop(WorldTroop worldTroop){
		if(worldTroop.getTroopPosition()!=TroopPosition.None.getType()){
			//不处于玩家掌控
			return false;
		}
		return true;
	}
	public boolean isCanOperationTroop(int serverId,String troopId){
		WorldTroop worldTroop = TroopServerContainer.of(serverId).getWorldTroop(troopId);
		if(worldTroop==null){
			return false;
		}
		return isCanOperationTroop(worldTroop);
	}
	
	/**
	 * 部队是否在采集中
	 * @param worldTroop
	 * @return
	 */
	public boolean isTroopIsBusy(WorldTroop worldTroop) {
//		return WorldBuildTroopServerContainer.of(worldTroop).troopIsmMining(worldTroop);
		return false;
	}
	
	
	/**
	 * 处理撤退增加部队血量
	 * @param player
	 * @param worldTroop
	 */
	public void addWorldTroopHpForRetreat(Player player,WorldTroop worldTroop) {
		int addHp = cityWarSkillBiz.getSkillAddHp(player);
		addWorldTroopHpForEveryTank(player, worldTroop, addHp);
	}
	/**
	 * 对部队的每个坦克进行血量恢复
	 * @param addHp
	 */
	public void addWorldTroopHpForEveryTank(Player player,WorldTroop worldTroop,int addHp) {
		if(addHp <= 0) {
			return;
		}
		boolean isChange = false;
		for (TankArmy tankArmy : worldTroop.getTroopArmy().getTankList()) {
			if(tankArmy.isFullHp()) {
				continue;
			}
			Tank tank = player.playerTank().getTank(tankArmy.getId());
			if(tank == null) {
				continue;
			}
			long maxHp = (long)tank.getTotalAttr(TankAttrType.HP);
			tankArmy.setHp(tankArmy.getHp()+addHp);
			if(tankArmy.getHp() >= maxHp) {
				tankArmy.setFullHp();
			}
			isChange = true;
		}
		if(isChange) {
			worldTroop.saveDB();
		}
	}

	public FullTroopResult checkSpendHandFullTroopOrKF(Player player,long totalSecond) {
		if(player.isKFPlayer()) {
			IGameRpc gameRpc = player.playerTemp().getKfpServerUrl().getGameRpc();
			return new FullTroopResult(gameRpc.handFullTankHp(player.getId(),totalSecond));
		}else{
			return checkSpendHandFullTroop(player,totalSecond);
		}
	}

	/**
	 * @author siyunlong
	 * @version V1.0
	 * @Description: 计算部队回满血的消耗
	 * @date 2024/6/12 10:22
	 */
	public FullTroopResult checkSpendHandFullTroop(Player player,long totalSecond) {
		FullTroopResult result = new FullTroopResult();

		totalSecond -= 2;//固定再减去2秒用于处理客户端和服务器差异
		totalSecond = Math.max(1, totalSecond);
		//当前背包内的锤子数量
		long itemNum = player.playerBag().getItemCount(ItemConstant.TroopRepairHammer);
		//每个锤子抵消的秒数
		int oneItemSecond = itemConfig.getItemTemplateById(ItemConstant.TroopRepairHammer).getValue();
		//实际消耗道具数量
		long spendItemNum = Math.min(itemNum, troopBiz.getTroopFullHpNeedItemNum(totalSecond, oneItemSecond));
		//需要用金币抵消的秒数
		long lastGoldSecond = Math.max(totalSecond-spendItemNum*oneItemSecond, 0);
		long spendGold = (long)Math.ceil(2*MathUtils.div(lastGoldSecond, 60));

		//扣除满血所需金币
		if(spendGold > 0 && !playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, spendGold, LogType.HandTroopFullHp)) {
			return result;
		}
		if(spendItemNum > 0) {
			bagBiz.spendItem(player, ItemConstant.TroopRepairHammer, spendItemNum, LogType.HandTroopFullHp);
		}
		result.setSuc(true);
		result.setSpendGold(spendGold);
		result.setSpendItemNum(spendItemNum);

		return result;
	}
	
	
}
