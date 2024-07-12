package com.hm.action.captive;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.action.captive.log.CaptiveMeLog;
import com.hm.action.captive.log.CaptiveOtherLog;
import com.hm.action.captive.log.CaptiveTechLog;
import com.hm.action.item.ItemBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CaptiveConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.templaextra.CampPrisonerResearcherTemplateImpl;
import com.hm.config.excel.templaextra.CampPrisonerTemplateImpl;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.item.Items;
import com.hm.model.player.*;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.ObservableEnum;
import com.hm.util.RandomUtils;
import com.hm.war.sg.FightTroopType;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.WarResult;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.NpcTroop;
import com.hm.war.sg.troop.PlayerTroop;
import com.hm.war.sg.troop.TankArmy;
import com.hm.war.sg.unit.Unit;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: tank俘虏
 * @author siyunlong  
 * @date 2020年7月1日 下午5:38:12 
 * @version V1.0
 */
@Slf4j
@Biz
public class CaptiveBiz {

	@Resource
	private CaptiveConfig captiveConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CommValueConfig commValueConfig;
	/**
	 * 判断玩家坦克 俘虏
	 * @param player
	 * @param warResult
	 */
	public void doPlayerTankCaptive(Player player,WarResult warResult) {
		if(player.playerCaptive().getLv() <= 0) {
			return;
		}
		UnitGroup loseGroup = warResult.getLoseUnitGroup();
		AbstractFightTroop loseTroop = loseGroup.getFightTroop();
		if(loseTroop == null || !isCamptiveRate(player,loseTroop.getFightTroopType())) {
			return;//判断概率
		}
		CaptiveSlot luckCaptiveSlot = player.playerCaptive().findIdleCaptiveSlot(calMaxCaptiveCount(player));
		if(luckCaptiveSlot == null) {
			return;//没有俘虏位置了  
		}
		if(loseTroop instanceof NpcTroop) {
			//npc直接俘虏了
			doCaptiveNpcTank(player,luckCaptiveSlot, loseGroup,warResult);
			return;
		}
		if(loseTroop instanceof PlayerTroop) {
			PlayerTroop losePlayerTroop = (PlayerTroop)loseTroop;
			Player losePlayer = losePlayerTroop.getPlayer();
			if(losePlayer == null || !losePlayer.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankCaptive)) {
				return;
			}
			//判断还有可以俘虏的tank
			List<Integer> beCaptiveList = losePlayer.playerCaptive().getBeCaptiveTank();
            int captivePlayerMax = commValueConfig.getCommValue(CommonValueType.CaptivePlayerMax);
            if(beCaptiveList.size() >= captivePlayerMax) {
				return;
			}
			//判断此部队是否已经有被俘虏的tank了
			boolean haveTroopTank = losePlayerTroop.getTankList().stream().anyMatch(e -> beCaptiveList.contains(e.getId()));
			if(haveTroopTank) {
				return;
			}
			doCaptivePlayerTank(player, losePlayer, luckCaptiveSlot, loseGroup,warResult);
		}
		
	}
	/**
	 * 处理俘虏对方tank
	 * @param player
	 * @param luckCaptiveSlot
	 * @param loseGroup
	 */
	public void doCaptiveNpcTank(Player player,CaptiveSlot luckCaptiveSlot,UnitGroup loseGroup,WarResult warResult) {
		Unit unit = RandomUtils.randomEle(loseGroup.getUnits());
		long endTime = System.currentTimeMillis() + commValueConfig.getCommValue(CommonValueType.CaptiveTankTime) * GameConstants.MINUTE;
		CaptiveTankInfo tankInfo = new CaptiveTankInfo(loseGroup, unit, endTime);
		luckCaptiveSlot.loadCaptiveTankInfo(tankInfo);
		//添加俘虏日志
		player.playerCaptive().addLog(new CaptiveOtherLog(tankInfo.getName(), tankInfo.getTankId()));
		doPlayerAutoTech(player,luckCaptiveSlot);//判断是否自动研究
		//==========================================
		warResult.getWinUnitGroup().setCaptiveTankId(unit.getId());
		
		player.notifyObservers(ObservableEnum.TankCaptive, tankInfo);
	}
	
	/**
	 * 处理玩家被俘虏
	 * @param player
	 * @param losePlayer
	 * @param luckCaptiveSlot
	 * @param loseGroup
	 */
	public void doCaptivePlayerTank(Player player,Player losePlayer,CaptiveSlot luckCaptiveSlot,UnitGroup loseGroup,WarResult warResult) {
		Unit unit = RandomUtils.randomEle(loseGroup.getUnits());
		int tankId = unit.getSetting().getTankId();
		long endTime = System.currentTimeMillis()+commValueConfig.getCommValue(CommonValueType.CaptiveTankTime) * GameConstants.MINUTE;
		CaptiveTankInfo tankInfo = new CaptiveTankInfo(loseGroup, unit, endTime);
		luckCaptiveSlot.loadCaptiveTankInfo(tankInfo);
		//添加俘虏日志
		player.playerCaptive().addLog(new CaptiveOtherLog(tankInfo.getName(), tankInfo.getTankId()));
		doPlayerAutoTech(player, luckCaptiveSlot);//判断是否自动研究
		//===========================================
		losePlayer.playerCaptive().addBeCaptiveTank(new BeCaptiveTankInfo(player, tankId, endTime));
		//添加被俘虏日志
		losePlayer.playerCaptive().addLog(new CaptiveMeLog(player.getName(), tankId));
		losePlayer.sendUserUpdateMsg();
		//==========================================
		warResult.getWinUnitGroup().setCaptiveTankId(unit.getId());
		
		player.notifyObservers(ObservableEnum.TankCaptive, tankInfo);
	}
	
	/**
	 * 赎回坦克
	 * @param player
	 * @param
	 */
	public void redeemTank(Player player,BeCaptiveTankInfo captiveTankInfo) {
		int tankId = captiveTankInfo.getTankId();
		long oppoPlayerId = captiveTankInfo.getPlayerId();
		//删除坦克
		player.playerCaptive().removeBeCaptiveTank(tankId);
		//=====================删除对方数据==============================
		Player robPlayer = PlayerUtils.getPlayer(oppoPlayerId);
		if(robPlayer == null) {
			return;
		}
		robPlayer.playerCaptive().doRedeemTank(tankId, player.getId());
		if(robPlayer.playerCaptive().Changed()) {
			robPlayer.sendUserUpdateMsg();
		}
	}

	/**
	 * 释放坦克
	 * @param player
	 * @param
	 */
	public void freedTank(Player player,CaptiveSlot captiveSlot) {
		CaptiveTankInfo tankInfo = captiveSlot.getTankInfo();
		//删除俘虏的坦克
		captiveSlot.doFreedTank();
		player.playerCaptive().SetChanged();
		//=====================删除对方数据==============================
		Player bePlayer = PlayerUtils.getPlayer(tankInfo.getPlayerId());
		if(bePlayer == null) {
			return;
		}
		bePlayer.playerCaptive().removeBeCaptiveTank(tankInfo.getTankId());
		if(bePlayer.playerCaptive().Changed()) {
			bePlayer.sendUserUpdateMsg();
			//更新被俘玩家部队信息
			bePlayer.sendWorldTroopMessage();
		}
	}

	/**
	 * 判断俘虏概率
	 * @param player
	 * @return
	 */
	public boolean isCamptiveRate(Player player,FightTroopType type) {
		int lv = player.playerCaptive().getLv();
		CampPrisonerTemplateImpl prisonerTemplate = captiveConfig.getCampPrisonerTemplate(lv);
		double rate = 0;
		if (prisonerTemplate != null){
			rate = type == FightTroopType.NPC ? prisonerTemplate.getChance_catch_npc() : prisonerTemplate.getChance_catch_player();
		}
		return RandomUtils.randomIsRate(rate);
	}
	/**
	 * 计算槽位最大可俘虏tank数量
	 * @param player
	 * @return
	 */
	public int calMaxCaptiveCount(Player player) {
		int lv = player.playerCaptive().getLv();
		CampPrisonerTemplateImpl prisonerTemplate = captiveConfig.getCampPrisonerTemplate(lv);
		return prisonerTemplate != null ? prisonerTemplate.getCatch_limit() : 0;
	}
	
	/**
	 * 处理俘虏自动研究
	 * @param player
	 */
	public void doPlayerAutoTech(Player player, CaptiveSlot captiveSlot) {
		if (player.playerCaptive().isAutoTech()){
			doCaptiveTech(player, captiveSlot);
		}
	}

	/**
	 * 处理俘虏研究
	 */
	public List<Items> doCaptiveTech(Player player, CaptiveSlot captiveSlot){
		List<Items> rewardItems = Lists.newArrayList();
		//检查是否可以研究
		if (captiveSlot.checkCanTech()){
			//检查研究员
			checkResearcherEndTime(player);
			//计算研究奖励
			rewardItems = calPlayerTechReward(player, captiveSlot);
			itemBiz.addItem(player, rewardItems, LogType.CaptiveResearcherReward);
			CaptiveTankInfo tankInfo = captiveSlot.getTankInfo();
			player.playerCaptive().addLog(new CaptiveTechLog(tankInfo.getTankId(),tankInfo.getName(),rewardItems));
			captiveSlot.doTech();
			player.playerCaptive().SetChanged();
		}
		return rewardItems;
	}

	public boolean isCanAutoTech(Player player){
		int lv = player.playerCaptive().getLv();
		CampPrisonerTemplateImpl prisonerTemplate = captiveConfig.getCampPrisonerTemplate(lv);
		return prisonerTemplate != null ? prisonerTemplate.getResearch_auto()==1 : false;
	}

	private List<Items> calPlayerTechReward(Player player, CaptiveSlot captiveSlot) {
		List<Items> rewardItems = Lists.newArrayList();
		//研究员
		int researcherId = player.playerCaptive().getResearcherId();
		CampPrisonerResearcherTemplateImpl researcherTemplate = captiveConfig.getCampPrisonerResearcherTemplate(researcherId);
		if (researcherTemplate == null){
			return rewardItems;
		}
		//战俘营
		CampPrisonerTemplateImpl prisonerTemplate = captiveConfig.getCampPrisonerTemplate(player.playerCaptive().getLv());
		if (prisonerTemplate == null){
			return rewardItems;
		}
		rewardItems.addAll(prisonerTemplate.getExtraRewardItem());
		rewardItems.addAll(researcherTemplate.getExtraDropItems());
		CaptiveTankInfo tankInfo = captiveSlot.getTankInfo();
		if (tankInfo != null){
			int tankId = tankInfo.getTankId();
			TankSetting tankSetting = tankConfig.getTankSetting(tankId);
			int paperId = tankSetting.getPaper_id();
			rewardItems.add(new Items(paperId, 1, ItemType.PAPER.getType()));
		}
		return rewardItems;
	}

	/**
	 * 检查研究员是否到期  若到期则更换为默认研究员
	 * @param player
	 */
	public void checkResearcherEndTime(Player player){
		PlayerCaptive playerCaptive = player.playerCaptive();
		if (!playerCaptive.checkHaveResearcher(playerCaptive.getResearcherId())){
			int defResearcher = captiveConfig.getDefResearcher();
			playerCaptive.changeResearcher(defResearcher);
		}
	}

	/**
	 * 检查我俘虏的坦克是否到期
	 * @param player
	 */
	public void checkCaptiveTankEndTime(Player player){
		Arrays.stream(player.playerCaptive().getSlots()).filter(Objects::nonNull)
				.filter(e -> e.getTankInfo() != null && !e.getTankInfo().isFitTime())
				.forEach(captiveSlot -> {
			freedTank(player, captiveSlot);
		});
	}

	/**
	 * 检查我的被俘虏的坦克是否到期
	 * @param player
	 */
	public void checkBeCaptiveTankEndTime(Player player){
		player.playerCaptive().getBeCloseMap().values().stream().filter(e -> !e.isFitTime()).forEach(beCaptiveTankInfo -> {
			redeemTank(player, beCaptiveTankInfo);
		});
	}


	/**
	 * 判断部队中是否有被俘虏坦克
	 * @param player
	 * @param worldTroop
	 * @return
	 */
	public boolean haveCaptiveTank(Player player,WorldTroop worldTroop) {
		List<Integer> beCaptiveTankIds = player.playerCaptive().getBeCaptiveTank();
		if(CollUtil.isEmpty(beCaptiveTankIds)) {
			return false;
		}
		return worldTroop.getTroopArmy().getTankList().stream().anyMatch(e -> beCaptiveTankIds.contains(e.getId()));
	}
	
	/**
	 * 编队时判断是否存在有被俘虏坦克
	 * @param player
	 * @param worldTroop
	 * @param newArmyList
	 * @return
	 */
	public boolean isCanCaptiveChangeTank(Player player,WorldTroop worldTroop,List<TankArmy> newArmyList) {
		List<Integer> beCaptiveTankIds = player.playerCaptive().getBeCaptiveTank();
		if(CollUtil.isEmpty(beCaptiveTankIds)) {
			return false;
		}
		List<Integer> newTankIds = newArmyList.stream().map(e -> e.getId()).collect(Collectors.toList());
		return worldTroop.getTroopArmy().getTankList().stream().filter(e -> !newTankIds.contains(e.getId()))
				.anyMatch(e -> beCaptiveTankIds.contains(e.getId()));
	}

}
