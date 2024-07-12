package com.hm.timerjob;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.cityworld.biz.WorldBiz;
import com.hm.action.cityworld.biz.WorldDeclareWarBiz;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.config.GameConstants;
import com.hm.db.PlayerUtils;
import com.hm.enums.TankAttrType;
import com.hm.enums.TroopState;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.worldtroop.TroopWay;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.troop.TroopItemContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.util.MathUtils;
import com.hm.war.sg.troop.TankArmy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class WorldTroopJob {
	@Resource
    private TroopBiz troopBiz;
	@Resource
    private WorldBiz worldBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private WorldDeclareWarBiz worldDeclareWarBiz;


//	@Scheduled(cron="0/1 * *  * * ? ")  
//	public void doWorldTroopTimer() {
//		try {
//			if(!ServerStateCache.serverIsRun()) {
//				return;
//			}
//			GameServerManager.getInstance().getServerIdList().forEach(serverId -> {
//				TroopItemContainer troopContainer = TroopServerContainer.of(serverId);
//				for (WorldTroop worldTroop : troopContainer.getPlayerTroopMap().values()) {
//					doJobWorldTroop(worldTroop);
//				}
//			});
//			
//		} catch (Exception e) {
//			log.error("世界部队定时器出错", e);
//		}
//	}
	
	public void doJob(int serverId) {
		boolean guildWarIsOpen = GuildWarUtils.isOpenWar(serverId);
		TroopItemContainer troopContainer = TroopServerContainer.of(serverId);
		for (WorldTroop worldTroop : troopContainer.getPlayerTroopMap().values()) {
			try {
				doJobWorldTroop(worldTroop,guildWarIsOpen);
			} catch (Exception e) {
				log.error(worldTroop.getServerId()+"_"+worldTroop.getId()+"部队检查出错", e);
			}
		}
	}
	
	private void doJobWorldTroop(WorldTroop worldTroop,boolean guildWarIsOpen) {
		int troopState = worldTroop.getState();
		if(troopState == TroopState.None.getType()) {
			//检查是否解散部队
			if(checkDisandWorldTroop(worldTroop) || checkTroopToCityFight(worldTroop)) {
				return;//删除部队了
			}
			doCheckWorldTroopNone(worldTroop);
			return;
		}else if(troopState == TroopState.Death.getType()) {
			doWorldTroopDeath(worldTroop);
			return;
		}else if(guildWarIsOpen && troopState == TroopState.Move.getType()) {
			doWorldTroopMove(worldTroop);
			return;
		}
	}
	private void doWorldTroopMove(WorldTroop worldTroop) {
		try {
			worldTroop.lockTroop();
			TroopWay troopWay = worldTroop.getTroopWay();
			if(!troopWay.checkTroopMove()) {//还不能移动到下个城市
				return;
			}
			//到当前目标点 
			LinkedList<Integer> wayList = worldTroop.getTroopWay().getWayList();
			Player player = PlayerUtils.getPlayer(worldTroop);
			if(wayList.isEmpty()) {//处理异常!!
				//所有都满血了
				worldTroop.changeState(TroopState.None);
				//去该去的地方
				int curCityId = worldTroop.getCityId();
				if(curCityId > 0) {
					worldBiz.troopEnterCity(player, worldTroop, curCityId);
				}else{
					troopBiz.addWorldTroopToWorldAndUpdate(player, worldTroop);
				}
				return;
			}
			//部队移动时间
			int troopMoveSpeed = troopBiz.calPlayerWorldMoveSpeed(player);
			troopMoveSpeed = Math.max(troopMoveSpeed, 2);//最低2秒
			troopWay.setTroopSpeed(troopMoveSpeed);
			//更新城与城之前的移动时间
			troopWay.updateNextArriveSecond(troopMoveSpeed);
			int nextCityId = wayList.get(0);
			//通知部队变化
			worldTroop.setCityId(nextCityId);
			int curCityId = worldTroop.getCityId();
			int endTarget = wayList.getLast();//最后一个点
			if(curCityId != worldTroop.getTroopWay().getStartCityId()) {
				//进入B城
				WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(nextCityId);
				if(troopBiz.isEnterCityFightForMove(player,worldTroop, worldCity)
						|| wayList.size() <= 1 || nextCityId == endTarget) {//此城有战斗
//					//清除路径,进入此城
//					worldTroop.getTroopWay().clear();//清除路径信息
					//进入此城
					worldBiz.troopEnterCity(player, worldTroop, nextCityId);
					return;
				}
			}
			troopBiz.sendWorldTroopUpdate(player, worldTroop);
			if(CollUtil.isNotEmpty(wayList)) {
				wayList.removeFirst();
				worldTroop.getTroopWay().setStartCityId(nextCityId);
			}
			//广播移动
			if(CollUtil.isNotEmpty(wayList)) {
				troopBiz.worldTroopMoveBroad(worldTroop,worldTroop.getCityId(),wayList.get(0));
			}
			worldTroop.saveDB();
		} catch (Exception e) {
			log.error(worldTroop.getServerId()+"部队出错:"+worldTroop.getId(), e);
		} finally {
			worldTroop.unlockTroop();
		}
		
	}
	
	private void doWorldTroopDeath(WorldTroop worldTroop) {
		if(System.currentTimeMillis() < worldTroop.getTroopArmy().getLastRecoverTime()) {
			return;
		}
		doWorldTroopRecover(worldTroop);//处理回血
	}
	
	private void doCheckWorldTroopNone(WorldTroop worldTroop) {
		if(System.currentTimeMillis() < worldTroop.getTroopArmy().getLastRecoverTime()) {
			return;
		}
		worldTroop.getTroopArmy().updateLastRecoverTime();
		//是否有不满血坦克
		boolean haveNotFullTank = worldTroop.getTroopArmy().getTankList().stream().anyMatch(e -> !e.isFullHp());
		if(!haveNotFullTank) {
			return;//都满血了
		}
		doWorldTroopRecover(worldTroop);//处理回血
	}
	
	/**
	 * 部队回血
	 * @param worldTroop
	 */
	private void doWorldTroopRecover(WorldTroop worldTroop) {
		//正在回血的坦克Id
		Player player = PlayerUtils.getPlayer(worldTroop);
		long secondAdd = troopBiz.calTroopRecoveSecondHp(player, worldTroop);

		long addHp = secondAdd*GameConstants.Recover_HP_Interval;
		long newAddHp = addHp;
		for (int i = 0; i < 5; i++) {
			long recoverHp = doTroopRecover(player, worldTroop, addHp,secondAdd);
			if(recoverHp <= 0) {
				break;
			}
			addHp -= recoverHp;
			if(addHp <= 0) {
				break;
			}
		}
		//重新判断剩余回血的时间和回血间隔进行比较
		long lastRecoverHp = getTroopLastHp(player, worldTroop);
		if(newAddHp > lastRecoverHp) {
			//下次回血,10秒回的血超了
			worldTroop.getTroopArmy().updateLastRecoverTime(lastRecoverHp/secondAdd);
		}else{
			worldTroop.getTroopArmy().updateLastRecoverTime();
		}
		worldTroop.saveDB();
		player.sendUserUpdateMsg(true);
	}
	
	public long getTroopLastHp(Player player,WorldTroop worldTroop) {
		List<TankArmy> noFullTanks = worldTroop.getTroopArmy().getTankMap().values()
				.stream().filter(e -> !e.isFullHp()).collect(Collectors.toList());
		return troopBiz.calTroopRecoveHp(player, noFullTanks);
	}
	
	public long doTroopRecover(Player player,WorldTroop worldTroop,long addHp,long secondAdd) {
		int recoverTankId = worldTroop.getTroopArmy().getRecoverId();
		if(recoverTankId <= 0) {
			//找到血量最高
			recoverTankId = troopBiz.choiceRecoverTankId(player, worldTroop);
			worldTroop.getTroopArmy().setRecoverId(recoverTankId);
		}
		TankArmy tankArmy = worldTroop.getTroopArmy().getTankArmy(recoverTankId);
		if(tankArmy == null || tankArmy.isFullHp() 
				|| player.playerCaptive().getBeCaptiveTank(recoverTankId) != null) {//说明换坦克了
			recoverTankId = troopBiz.choiceRecoverTankId(player, worldTroop);
			worldTroop.getTroopArmy().setRecoverId(recoverTankId);
			tankArmy = worldTroop.getTroopArmy().getTankArmy(recoverTankId);
		}
		if(recoverTankId <= 0) {
			if(worldTroop.getState() != TroopState.None.getType()) {
				//所有都满血了
				worldTroop.changeState(TroopState.None);
				//去该去的地方
				troopBiz.addWorldTroopToWorldAndUpdate(player, worldTroop);
			}
			return 0;
		}
		//回血后检查
		Tank tank = player.playerTank().getTank(tankArmy.getId());
		if(tank == null) {
			return 0;
		}
		long maxHp = (long)tank.getTotalAttr(TankAttrType.HP);
		long curTankAddHp = Math.min(maxHp - tankArmy.getHp(), addHp);
		if(curTankAddHp > 0) {
			long spendOil = (long)(Math.min(1, MathUtils.div((double)curTankAddHp, (double)maxHp))*tank.getOil());
			//检查消耗
			if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Oil, spendOil, null)) {
				return 0;
			}
			//回血
			tankArmy.setHp(tankArmy.getHp()+curTankAddHp);
		}
		
		if(tankArmy.getHp() >= maxHp) {
			tankArmy.setFullHp();//这个回满了
			recoverTankId = troopBiz.choiceRecoverTankId(player, worldTroop);
			worldTroop.getTroopArmy().setRecoverId(recoverTankId);
			if(recoverTankId == -1 && worldTroop.getState() != TroopState.None.getType()) {
				boolean isRevive = worldTroop.getState() == TroopState.Death.getType();
				//所有都满血了
				worldTroop.changeState(TroopState.None);
				//去该去的地方
				troopBiz.addWorldTroopToWorldAndUpdate(player, worldTroop);
				if(isRevive) {
					player.notifyObservers(ObservableEnum.TroopRevive, worldTroop,0L);
				}
			}
			troopBiz.sendWorldTroopUpdate(player, worldTroop);
			if(recoverTankId == -1) {
				return 0;//全部回满了
			}
		}else{
			//发送回血消息
			sendWorldTroopRecoverHpMsg(player, worldTroop, tankArmy,secondAdd);
		}
		return curTankAddHp;
	}
	
	
	//回血消息
	private void sendWorldTroopRecoverHpMsg(Player player,WorldTroop worldTroop,TankArmy tankArmy,long secondAdd) {
		JsonMsg msg = JsonMsg.create(MessageComm.S2C_WorldTroopRecoverHp);
		msg.addProperty("troopId", worldTroop.getId());
		msg.addProperty("tankId", tankArmy.getId());
		msg.addProperty("tankHp", tankArmy.getHp());
		msg.addProperty("secondAdd", secondAdd);
		player.sendMsg(msg);
	}
	
	/**
	 * 10天部隊无变化-解散部队
	 * @param worldTroop
	 * @return
	 */
	private boolean checkDisandWorldTroop(WorldTroop worldTroop) {
		if(worldTroop.getLastChangeTime() + 7*GameConstants.DAY > System.currentTimeMillis()) {
			return false;
		}
		Player player = PlayerUtils.getPlayer(worldTroop);
		if(player != null) {
			troopBiz.removeWorldTroop(worldTroop,player);//删除部队
		}
		return true;
	}
	
	private boolean checkTroopToCityFight(WorldTroop worldTroop) {
		if(worldTroop.isSky()) {
			return false;
		}
		WorldCity worldCity = WorldServerContainer.of(worldTroop).getWorldCity(worldTroop.getCityId());
		if(worldCity != null) {
			Player player = PlayerUtils.getPlayer(worldTroop);
			if(worldDeclareWarBiz.isEnemyGuild(player, worldCity)) {
				return worldBiz.troopEnterCity(player, worldTroop, worldCity.getId());
			}
		}
		return false;
	}
}
