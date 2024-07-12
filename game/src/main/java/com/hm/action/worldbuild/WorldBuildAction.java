package com.hm.action.worldbuild;

import cn.hutool.core.collection.CollUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.kf.kfexpedition.KfExpeditionBiz;
import com.hm.action.troop.biz.WorldTroopBiz;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.WorldBuildConfig;
import com.hm.config.excel.templaextra.WorldBuildLevelTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerWorldBuildData;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.worldbuild.PlayerWorldBuild;
import com.hm.servercontainer.worldbuild.WorldBuildTroop;
import com.hm.servercontainer.worldbuild.WorldBuildTroopServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;


/**
 * @Description: 世界建筑
 * @author siyunlong  
 * @date 2019年10月18日 下午6:37:07 
 * @version V1.0
 */
@Action
public class WorldBuildAction extends AbstractPlayerAction{
	@Resource
	private WorldBuildBiz worldBuildBiz;
	@Resource
	private ItemBiz itemBiz;

	@Resource
	private WorldTroopBiz worldTroopBiz;

	@Resource
	private WorldBuildConfig worldBuildConfig;
	@Resource
	private CommValueConfig commValueConfig;
	
	
	@MsgMethod (MessageComm.C2S_GetWorldBuildData )
	public void getWorldBuildData(Player player, JsonMsg msg){
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
				.getServerWorldBuildData();
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_ServerWorldBuildChange);
		serverMsg.addProperty("serverWorldBuildData", serverWorldBuildData);
		player.sendMsg(serverMsg);
	}
	
	//颁布
	@MsgMethod (MessageComm.C2S_WorldBuildReleaseTask )
	public void releaseTask(Player player, JsonMsg msg){
		//没有权限
		if(!KfExpeditionBiz.haveExpeditionPower(player.getId())) {
			return;
		}
		int hour = DateUtil.thisHour(true);
		if(hour < GameConstants.WorldBuildStartHour || hour > GameConstants.WorldBuildEndHour) {
			return;
		}
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
				.getServerWorldBuildData();
		if(!serverWorldBuildData.isOpen()) {
			return;
		}
		WorldBuildLevelTemplate worldBuildLevelTemplate = worldBuildConfig.getLevelTemplate(serverWorldBuildData.getLv());
		if(worldBuildLevelTemplate == null) {
			return;
		}
		int secondAdd = worldBuildLevelTemplate.getCollect();
		int maxScore = worldBuildLevelTemplate.getResource();
		
		synchronized (serverWorldBuildData) {
			if(serverWorldBuildData.isRelease()) {
				return;//已经颁布过了
			}
			int releaseMinute = commValueConfig.getCommValue(CommonValueType.WorldBuildReleaseTime);
			serverWorldBuildData.doReleaseTask(secondAdd,maxScore,releaseMinute);
			serverWorldBuildData.save();
			
			worldBuildBiz.startBuildJob(player.getServerId());
			worldBuildBiz.broadWorldBuildChange(serverWorldBuildData);
			player.sendMsg(MessageComm.S2C_WorldBuildReleaseTask);
			
			player.notifyObservers(ObservableEnum.WorldBuildTaskRelease,player.getServerId());
		}
	}
	
	//领取每日奖励
	@MsgMethod (MessageComm.C2S_WorldBuildDayReward )
	public void dayReward(Player player, JsonMsg msg){
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
				.getServerWorldBuildData();
		if(!serverWorldBuildData.isOpen()) {
			return;
		}
		if(player.playerWorldBuildData().isTodayReward()) {
			return;
		}
		WorldBuildLevelTemplate worldBuildLevelTemplate = worldBuildConfig.getLevelTemplate(serverWorldBuildData.getLv());
		if(worldBuildLevelTemplate == null) {
			return;
		}
		List<Items> rewardList = worldBuildLevelTemplate.getDayItems();
		itemBiz.addItem(player, rewardList, LogType.WorldBuildDayReward);
		
		player.playerWorldBuildData().setTodayReward(true);
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldBuildDayReward);
		serverMsg.addProperty("itemList", rewardList);
		player.sendMsg(serverMsg);
	}

	//遣返采集队列
	@MsgMethod (MessageComm.C2S_WorldBuildTroopBack )
	public void worldBuildTroopback(Player player, JsonMsg msg){
		List<String> troopIds = StringUtil.splitStr2StrList(msg.getString("ids"), ",");
		if(CollUtil.isEmpty(troopIds)) {
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}
		PlayerWorldBuild playerWorldBuild = WorldBuildTroopServerContainer.of(player).getPlayerWorldBuild(player.getId());
		if(playerWorldBuild == null) {
			player.sendErrorMsg(SysConstant.WorldTroop_Operation_Not);
			return;
		}
		List<WorldBuildTroop> backList = playerWorldBuild.removeBuildTroops(troopIds);
		if(!backList.isEmpty()) {
			worldBuildBiz.backWorldBuildTroop(playerWorldBuild, backList, false);
			playerWorldBuild.saveDB();
		}
		player.sendMsg(MessageComm.S2C_WorldBuildTroopBack );
	}
	
	//领取任务奖励
	@MsgMethod (MessageComm.C2S_WorldBuildCampReward)
	public void worldBuildCampReward(Player player, JsonMsg msg){
		int campId = msg.getInt("camp");//派遣到哪
		if(!player.playerWorldBuildData().isCanReward(campId)) {
			return;
		}
		//判断是否开启
		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
				.getServerWorldBuildData();
		if(!serverWorldBuildData.isOpen()) {
			return;
		}
		WorldBuildLevelTemplate worldBuildLevelTemplate = worldBuildConfig.getLevelTemplate(serverWorldBuildData.getReleaseLv());
		if(worldBuildLevelTemplate == null) {
			return;
		}
		long curScore = worldBuildBiz.getCampScore(player, campId);
		if(worldBuildLevelTemplate.getResource() > curScore) {
			return;
		}
		List<Items> rewardList = worldBuildLevelTemplate.getTaskItems(campId);
		itemBiz.addItem(player, rewardList, LogType.WorldBuildTaskReward);
		
		player.playerWorldBuildData().doCampReward(campId);
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldBuildCampReward);
		serverMsg.addProperty("itemList", rewardList);
		player.sendMsg(serverMsg);
	}
	
	//客户端采集满主动请求
	@MsgMethod (MessageComm.C2S_WorldBuildTroopMineFull)
	public void worldBuildTroopMineFull(Player player, JsonMsg msg){
//		int campId = msg.getInt("camp");//派遣到哪
//		//判断是否开启
//		ServerWorldBuildData serverWorldBuildData = ServerDataManager.getIntance().getServerData(player.getServerId())
//				.getServerWorldBuildData();
//		if(!serverWorldBuildData.isOpen()) {
//			return;
//		}
//		WorldBuildLevelTemplate worldBuildLevelTemplate = worldBuildConfig.getLevelTemplate(serverWorldBuildData.getReleaseLv());
//		if(worldBuildLevelTemplate == null) {
//			return;
//		}
//		//计算当前正在采集的
//		WorldBuildTroopItemContainer worldBuildTroopItemContainer = worldBuildTroopServerContainer.of(player);
//		PlayerWorldBuild playerWorldBuild = worldBuildTroopItemContainer.getPlayerWorldBuild(player.getId());
//		if(playerWorldBuild == null) {
//			return;
//		}
//		long curScore = worldBuildBiz.getCampScore(player, campId);
//		if(worldBuildLevelTemplate.getResource() > curScore) {
//			return;//还没有采集够
//		}
//		
//		List<WorldBuildTroop> backList = playerWorldBuild.removeBuildTroops(campId);
//		worldBuildBiz.backWorldBuildTroop(playerWorldBuild, backList, false);
//		
//		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_WorldBuildTroopMineFull);
//		player.sendMsg(serverMsg);
	}
}
