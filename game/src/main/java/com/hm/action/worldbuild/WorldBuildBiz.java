package com.hm.action.worldbuild;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.WorldBuildConfig;
import com.hm.config.excel.templaextra.WorldBuildLevelTemplate;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.ActivityType;
import com.hm.enums.CommonValueType;
import com.hm.enums.WorldBuildAddType;
import com.hm.message.MessageComm;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerWorldBuildData;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.servercontainer.worldbuild.PlayerWorldBuild;
import com.hm.servercontainer.worldbuild.WorldBuildTroop;
import com.hm.servercontainer.worldbuild.WorldBuildTroopItemContainer;
import com.hm.servercontainer.worldbuild.WorldBuildTroopServerContainer;
import com.hm.timerjob.server.GameTimerManager;
import com.hm.timerjob.server.WorldBuildQuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Biz
public class WorldBuildBiz implements IObserver{

	@Resource
	private WorldBuildConfig worldBuildConfig;
	@Resource
	private CommValueConfig commValueConfig;
	

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum) {
			case HourEvent:
				doHourEvent();
				break;
		}
	}

	public void doHourEvent() {
		if(DateUtil.thisHour(true) == 4) {
			for (WorldBuildTroopItemContainer worldBuildTroopItemContainer : WorldBuildTroopServerContainer.getServerMap().getAllContainer()) {
				if(worldBuildTroopItemContainer != null && worldBuildTroopItemContainer.getPlayerMap().size() > 0) {
					worldBuildTroopItemContainer.clearData();
				}
			}
		}
	}
	
	/**
	 * 检查世界建筑开启
	 * @param expeditionActivity
	 */
	public void checkWorldBuildOpen(KfExpeditionActivity expeditionActivity) {
		if(expeditionActivity.getOpenType() != 2) {
			return;
		}
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(expeditionActivity.getServerId())
				.getServerWorldBuildData();
		if(serverWorldBuildData.isOpen()) {
			return;
		}
		serverWorldBuildData.setLv(1);//开启
		serverWorldBuildData.save();
		broadWorldBuildChange(serverWorldBuildData);
		
		ObserverRouter.getInstance().notifyObservers(ObservableEnum.WorldBuildOpen, null, expeditionActivity.getServerId());
	}
	public void checkWorldBuildOpen(int serverId) {
		KfExpeditionActivity expeditionActivity = (KfExpeditionActivity)ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KfExpeditionActivity);
		if(expeditionActivity != null) {
			checkWorldBuildOpen(expeditionActivity);
		}
	}
	
	/**
	 * 广播世界建筑变化
	 * @param serverWorldBuildData
	 */
	public void broadWorldBuildChange(ServerWorldBuildData serverWorldBuildData) {
		int serverId = serverWorldBuildData.getContext().getServerId();
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_ServerWorldBuildChange);
		serverMsg.addProperty("serverWorldBuildData", serverWorldBuildData);
		PlayerContainer.broadPlayer(serverId, serverMsg);
	}
	
	public void sendPlayerWorldBuildTroop(Player player) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
		if(serverData == null) {
			return;
		}
		ServerWorldBuildData serverWorldBuildData = serverData.getServerWorldBuildData();
		if(!serverWorldBuildData.isOpen() || serverWorldBuildData.isOver()) {
			return;
		}
		PlayerWorldBuild playerWorldBuild = WorldBuildTroopServerContainer.of(player).getPlayerWorldBuild(player.getId());
		if(playerWorldBuild != null) {
			JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldBuildTroopChange);
			serverMsg.addProperty("playerWorldTroops", playerWorldBuild.getWorldBuildTroopList());
			player.sendMsg(serverMsg);
		}
	}
	
	/**
	 * 遣返采集部队
	 * @param
	 */
	public void backWorldBuildTroop(PlayerWorldBuild playerWorldBuild,List<WorldBuildTroop> backList,boolean timeIsOver) {
		if(playerWorldBuild == null || CollUtil.isEmpty(backList)) {
			return;
		}
		Player player = PlayerUtils.getPlayer(playerWorldBuild.getId());
		if(player == null) {
			return;
		}
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
				.getServerWorldBuildData();
		long totalServerExp = 0;
		int secondAdd = serverWorldBuildData.getSecondAdd();
		//最多采集多少
		long maxScore = commValueConfig.getCommValue(CommonValueType.WorldBuildMineTime)*60 *secondAdd; 
		for (WorldBuildTroop worldBuildTroop : backList) {
			long troopExp = worldBuildTroop.getTroopAdd(secondAdd);
			troopExp = Math.min(maxScore, troopExp);
			totalServerExp += troopExp;
			player.playerWorldBuildData().addCampScore(worldBuildTroop.getCampId(), troopExp);
		}
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldBuildTroopChange);
		if(timeIsOver) {
			serverMsg.addProperty("playerWorldTroops", Lists.newArrayList());
		}else{
			serverMsg.addProperty("playerWorldTroops", playerWorldBuild.getWorldBuildTroopList());
		}
		player.sendMsg(serverMsg);
		
		player.sendUserUpdateMsg();
		//增加服务器建筑经验
		log.error(player.getServerId()+"_"+playerWorldBuild.getId()+"增加工程经验:"+totalServerExp);
		
		doServerWorldBuildAddExp(serverWorldBuildData, totalServerExp);
	}
	
	/**
	 * 处理世界建筑添加等级
	 * @param serverWorldBuildData
	 * @param addExp
	 */
	public void doServerWorldBuildAddExp(ServerWorldBuildData serverWorldBuildData,long addExp) {
		int serverId = serverWorldBuildData.getContext().getServerId();
		serverWorldBuildData.addExp(addExp);
		int oldLv = serverWorldBuildData.getLv();
		long newExp = serverWorldBuildData.getExp();
		int newLv = worldBuildConfig.calNewLv(newExp);
		if(oldLv != newLv) {
			serverWorldBuildData.setLv(newLv);
			//世界建筑升级广播
			ObserverRouter.getInstance().notifyObservers(ObservableEnum.WorldBuildLvChange, null, serverId);
		}
		serverWorldBuildData.save();
	}
	
	/**
	 * 获取当前正在采集的部队总积分
	 * @param player
	 * @param campId
	 * @return
	 */
	public long getCampScore(Player player,int campId) {
		long score = player.playerWorldBuildData().getCampScore(campId);
		//计算当前正在采集的
		PlayerWorldBuild playerWorldBuild = WorldBuildTroopServerContainer.of(player).getPlayerWorldBuild(player.getId());
		if(playerWorldBuild == null) {
			return score;
		}
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
				.getServerWorldBuildData();
		int secondAdd = serverWorldBuildData.getSecondAdd();
		//所有当前正在采集的部队总和
		return score + playerWorldBuild.getTroopCampMap().values().stream()
							.filter(e -> e.getCampId() == campId)
							.mapToLong(e -> e.getTroopAdd(secondAdd)).sum();
	}
	
	/**
	 * 获取世界建筑等级配置
	 * @param serverId
	 * @return
	 */
	public WorldBuildLevelTemplate getWorldBuildLevelTemplate(int serverId) {
		ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
		if(serverData == null) {
			return null;
		}
		ServerWorldBuildData serverWorldBuildData = serverData.getServerWorldBuildData();
		if(serverWorldBuildData.isOpen()) {
			return worldBuildConfig.getLevelTemplate(serverWorldBuildData.getLv());
		}
		return null;
	}
	
	/**
	 * 获取世界建筑加成
	 * @param serverId
	 * @param type
	 * @return
	 */
	public double getWorldBuildAddValue(int serverId,WorldBuildAddType type) {
		WorldBuildLevelTemplate worldBuildLevelTemplate = getWorldBuildLevelTemplate(serverId);
		if(worldBuildLevelTemplate != null) {
			return worldBuildLevelTemplate.getTypeAddValue(type);
		}
		return 0d;
	}
	
	public void checkStartBuildJob(int serverId) {
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(serverId)
				.getServerWorldBuildData();
		if(!serverWorldBuildData.isOver()) {
			startBuildJob(serverId);
		}
	}
	
	public void startBuildJob(int serverId) {
		int endHour = GameConstants.WorldBuildEndHour+1;
		String cron = "0/1 * "+GameConstants.WorldBuildStartHour+"-"+endHour+" * * ?";
		GameTimerManager.getInstance().addServerTimer(serverId, WorldBuildQuartzJob.class,cron);
	}
}
