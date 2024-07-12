package com.hm.action.activity;

import cn.hutool.core.collection.CollUtil;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.activity.biz.ActivityReceiveBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.ActivityTaskConfig;
import com.hm.config.excel.ActiveWarmUpGiftConfig;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.templaextra.ActivityShopTemplate;
import com.hm.config.excel.templaextra.ActivityTaskTemplate;
import com.hm.enums.ActionType;
import com.hm.enums.ActivityType;
import com.hm.enums.LogType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.leaderboards.LeaderboardInfo;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.activity.*;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.kf.KFPlayerRankData;
import com.hm.model.task.ActivityTask;
import com.hm.observer.ObservableEnum;
import com.hm.observer.TaskStatus;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.ServerNameCache;
import com.hm.redis.util.RedisUtil;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.util.ServerUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Action
public class ActivityAction extends AbstractPlayerAction {
	@Resource
    private ActivityBiz activityBiz;
	@Resource
    private ItemBiz itemBiz;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private ActivityConfig activityConfig;
	@Resource
	private ShopConfig shopConfig;
	@Resource
	private ActivityEffectBiz activityEffectBiz;
	@Resource
	private ActivityTaskConfig activityTaskConfig;
	@Resource
	private ActiveWarmUpGiftConfig activeWarmUpGiftConfig;
	@Resource
	private LogBiz logBiz;
	@Resource
	private ActivityReceiveBiz activityReceiveBiz;

	/**
	 * 活动制作顺序 
	 */
	@MsgMethod(MessageComm.C2S_ActivityList)
    public void activityList(Player player, JsonMsg msg) {
		activityBiz.sendActivityList(player);
	}

	@MsgMethod(MessageComm.C2S_ActivityReward)
	public void rewardActivity(Player player, JsonMsg msg) {
		ActivityType activityType = ActivityType.getActivityType(msg.getInt("activityType"));
		int id = msg.getInt("id");
		//==================判断活动是否开启===================================
		AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
		if(activity == null || activity.isCloseForPlayer(player)) {
			player.sendErrorMsg(SysConstant.Activity_Close);
            return;
		}
		id = activity.checkGetId(id,player);//校验id.

		//判断活动是否达到领取条件
		if(id < 0 || !activity.checkCondition(player, id)) {
			player.sendErrorMsg(SysConstant.Activity_ConditionsNot);
            return;
		}
		//检查玩家是否可以领取
		PlayerActivityValue playerActivityValue = player.getPlayerActivity().getPlayerActivityValue(activityType);
		if(!playerActivityValue.checkCanReward(player, id)) {
			player.sendErrorMsg(SysConstant.Activity_ConditionsNot);
            return;
		}
		LogType logType = LogType.ActivityReward.value(activityType.getType());
		List<Items> itemList = activity.getRewardItems(player,id);
		//设置为已领取状态
		playerActivityValue.setRewardState(player, id);
		if(itemList != null) {
			itemBiz.addItem(player, itemList, logType);
		}
		player.notifyObservers(ObservableEnum.CHAct4003, activityType.getType(), id, 0);
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_ActivityReward);
		serverMsg.addProperty("activityType", activityType.getType());
		serverMsg.addProperty("itemList", itemList);
		player.sendMsg(serverMsg);
		player.sendUserUpdateMsg();
	}


	@MsgMethod(MessageComm.C2S_Activity_TaskReceive)
	public void taskReceive(Player player, JsonMsg msg) {
		int id = msg.getInt("id"); //任务id
		//根据id获取任务配置
		ActivityTaskTemplate template = activityTaskConfig.getTaskTemplate(id);
		if(template==null){
			return;
		}
		ActivityType activityType = ActivityType.getActivityType(template.getActive_id());
		//==================判断活动是否开启===================================
		AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
		if(activity == null || activity.isCloseForPlayer(player)) {
			player.sendErrorMsg(SysConstant.Activity_Close);
            return;
		}
		ActivityTask task = player.playerActivityTask().getTask(id);
		if(!task.isComplete()){
			return;
		}
		player.notifyObservers(ObservableEnum.ActivityTaskComplate, id);
		player.playerActivityTask().receive(id);
		itemBiz.addItem(player, template.getRewards(), LogType.ActivityTask.value(activityType.getType()));
		//处理活动内任务相关数据需要实现IActivityTaskValue接口,比如要加活动活跃点
		PlayerActivityValue value = player.getPlayerActivity().getPlayerActivityValue(activityType);
		if(value instanceof IActivityTaskValue){
			IActivityTaskValue taskValue = (IActivityTaskValue) value;
			taskValue.taskFinish(player, id);
			player.getPlayerActivity().SetChanged();
		}
		player.sendUserUpdateMsg();
		JsonMsg returnMsg = JsonMsg.create(MessageComm.S2C_Activity_TaskReceive);
		returnMsg.addProperty("id",id);
		returnMsg.addProperty("rewards",template.getRewards());
		player.sendMsg(returnMsg);
	}

	@MsgMethod(MessageComm.C2S_Activity_ShopBuy)
	public void shopBuy(Player player, JsonMsg msg) {
		int id = msg.getInt("id"); //商品
		int num = Math.max(1, msg.getInt("num"));
		//根据id获取任务配置
		ActivityShopTemplate template = shopConfig.getActiviyShopTemplate(id);
        if (template == null) {
			player.sendErrorMsg(SysConstant.ITEM_HAVENOT);
            return;
        }
        ActivityType activityType = ActivityType.getActivityType(template.getActive_id());
        //==================判断活动是否开启===================================
        AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
        if (activity == null || activity.isCloseForPlayer(player)) {
            player.sendErrorMsg(SysConstant.Activity_Close);
            return;
        }
        int activityVersion = activityBiz.getActivityVersion(activity);
        if (activityVersion != template.getRound()) {
            player.sendErrorMsg(SysConstant.SYS_ERROR);
            return;
        }
        PlayerActivityValue value = player.getPlayerActivity().getPlayerActivityValue(activityType);
        if (value instanceof IActivityShopValue) {
            IActivityShopValue shopValue = (IActivityShopValue) value;
            if (!shopValue.isCanBuy(player, id, num)) {
                //达到次数限制
				System.out.println(player.getId()+" 购买限制");
				return;
            }
            List<Items> cost = shopValue.getCost(player, template, num, msg);
            if (!itemBiz.checkItemEnoughAndSpend(player, cost, LogType.ActivityShop.value(activityType.getType()))) {
                player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
                return;
            }
            ;
            shopValue.addIntegral(cost);
            shopValue.buy(player, id, num);
            player.getPlayerActivity().SetChanged();
            List<Items> rewards = ItemUtils.calItemRateReward(template.getRewards(), num);
            player.notifyObservers(ObservableEnum.CHAct4006, activityType, CollUtil.getFirst(rewards), msg);
            player.notifyObservers(ObservableEnum.CHAct4005, activityType, ItemUtils.getGoldNum(cost), activityVersion);
            itemBiz.addItem(player, rewards, LogType.ActivityShop.value(activityType.getType()));
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_Activity_ShopBuy,rewards);
		}else{
			System.out.println(player.getId()+" not IActivityShopValue");
		}

	}


	@MsgMethod(MessageComm.C2S_Activity_KF_GetRank)
	public void getRank(Player player, JsonMsg msg) {
		int type = msg.getInt("type");
		int pageNo = msg.getInt("pageNo");
		pageNo = Math.max(1, pageNo);
		RankType rankType = RankType.getTypeByIndex(type);
		String myServerName = ServerNameCache.getServerName(player.getServerId());
		String rankName = activityBiz.getKfRankName(player.getServerId(), rankType);
		List<LeaderboardInfo> rankList = HdLeaderboardsService.getInstance().getAllServerGameRanks(rankName, pageNo);
		if(RankType.isScoreToInt(rankType)){
			rankList.forEach(t ->t.scoreToLong());
		}
		List<KFPlayerRankData> kfPlayerList = rankList.stream().map(t ->{
			long playerId = t.getIntId();
			int serverId = ServerUtils.getCreateServerId(playerId);
            String serverName = ServerNameCache.getServerName(serverId);
            PlayerRedisData redisData = RedisUtil.getPlayerRedisData(playerId);
            KFPlayerRankData kfPlayerData = new KFPlayerRankData(serverId, serverName, t.getRank(),t.getScore());
			kfPlayerData.load(redisData);
			return kfPlayerData;
		}).collect(Collectors.toList());
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Activity_KF_GetRank);
		if(pageNo == 1) {
			PlayerRedisData redisData = RedisUtil.getPlayerRedisData(player.getId());
			KFPlayerRankData myRankData;
			LeaderboardInfo rankData = HdLeaderboardsService.getInstance().getLeaderboardInfo(player, 0,rankName);
			if(rankData != null) {
				myRankData = new KFPlayerRankData(player.getServerId(),myServerName,rankData.getRank(),rankData.getScore());
			}else{
				myRankData = new KFPlayerRankData(player.getServerId(),myServerName,-1,-1);
			}
			myRankData.load(redisData);
			serverMsg.addProperty("myRankData", myRankData);
		}
		serverMsg.addProperty("type", type);
		serverMsg.addProperty("pageNo", pageNo);
		serverMsg.addProperty("ranks", kfPlayerList);
		player.sendMsg(serverMsg);
	}

	//获取跨服分组排行
	@MsgMethod(MessageComm.C2S_Activity_KF_Group_GetRank)
	public void getKfGroupRank(Player player, JsonMsg msg) {
		int type = msg.getInt("type");
		int pageNo = msg.getInt("pageNo");
		ActivityType activityType = RankType.getActivityTypeByRankType(type);
		if(activityType==null){
			return;
		}
		AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
		if(activity == null || activity.isCloseForPlayer(player)) {
			player.sendErrorMsg(SysConstant.Activity_Close);
            return;
		}
		pageNo = Math.max(1, pageNo);
		RankType rankType = RankType.getTypeByIndex(type);
		String rankName = activityBiz.getKfRankName(player.getServerId(), rankType);
		int kfId = activityBiz.getActivityKfId(activity);
		List<LeaderboardInfo> rankList = HdLeaderboardsService.getInstance().getGameRank(kfId,rankName, pageNo);
		if(RankType.isScoreToInt(rankType)){
			rankList.forEach(t ->t.scoreToLong());
		}
		List<KFPlayerRankData> kfPlayerList = rankList.stream().map(t ->{
			long playerId = t.getIntId();
			int serverId = ServerUtils.getCreateServerId(playerId);
            String serverName = ServerNameCache.getServerName(serverId);
            PlayerRedisData redisData = RedisUtil.getPlayerRedisData(playerId);
            KFPlayerRankData kfPlayerData = new KFPlayerRankData(serverId, serverName, t.getRank(),t.getScore());
			kfPlayerData.load(redisData);
			return kfPlayerData;
		}).collect(Collectors.toList());
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Activity_KF_Group_GetRank);
		if(pageNo == 1) {
			int myRank = (int)HdLeaderboardsService.getInstance().getRank(kfId,rankName,String.valueOf(player.getId()));
			PlayerRedisData redisData = RedisUtil.getPlayerRedisData(player.getId());
			String myServerName = ServerNameCache.getServerName(player.getServerId());
			LeaderboardInfo leaderboardInfo = HdLeaderboardsService.getInstance().getLeaderboardInfo(player, kfId, rankName);
			double s = leaderboardInfo != null ? leaderboardInfo.getScore() : 0;
			if(s > 0 && RankType.isScoreToInt(rankType)){
				s = (long)s;
			}

			KFPlayerRankData myRankData = new KFPlayerRankData(player.getServerId(),myServerName,myRank, s);
			myRankData.load(redisData);
			serverMsg.addProperty("myRankData", myRankData);
		}
		serverMsg.addProperty("type", type);
		serverMsg.addProperty("pageNo", pageNo);
		serverMsg.addProperty("ranks", kfPlayerList);
		player.sendMsg(serverMsg);
	}


	@MsgMethod(MessageComm.C2S_SINGLE_ACTIVITY)
	public void singleActivity(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		List<AbstractActivity> activityList = ActivityServerContainer.of(player).getActivityList(player);
		AbstractActivity activity = activityList.stream().filter(e -> id==e.getType()).findFirst().orElse(null);
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_SINGLE_ACTIVITY);
		serverMsg.addProperty("activity", activity);
		player.sendMsg(serverMsg);
	}

	@MsgMethod(MessageComm.C2S_Activity_SetMark)
	public void choiceSetMark(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		int type = msg.getInt("type");
		ActivityType activityType = ActivityType.getActivityType(type);
		if (activityType == null) {
			return;
		}
		if (!activityBiz.checkActivityIsOpen(player, activityType)) {
			return;
		}
		PlayerActivityValue value = player.getPlayerActivity().getPlayerActivityValue(activityType);
		if (!(value instanceof ActivityValueChoice) || id <= 0) {
			return;
		}
		ActivityValueChoice valueChoice = (ActivityValueChoice) value;
		if (!valueChoice.checkCanChoice(player, id)) {
            return;
        }
        valueChoice.setChoice(id);
        player.notifyObservers(ObservableEnum.CHAct4005, activityType, id);
        logBiz.addPlayerActionLog(player, ActionType.SelectActivityStage, "活动Id=" + value.getActivityId() + "type=" + type + ", stage =" + id);
		player.getPlayerActivity().SetChanged();
		JsonMsg jsonMsg = new JsonMsg(MessageComm.S2C_Activity_SetMark);
		jsonMsg.addProperty("value", valueChoice);
		jsonMsg.addProperty("type", type);
		player.sendUserUpdateMsg();
		player.sendMsg(jsonMsg);
	}

	@MsgMethod(MessageComm.C2S_Activity_Receive_All)
	public void receiveAll(Player player, JsonMsg msg) {
		int type = msg.getInt("type");
		ActivityType activityType = ActivityType.getActivityType(type);
		if (activityType == null) {
			return;
		}
		if (!activityBiz.checkActivityIsOpen(player, activityType)) {
			return;
		}
		List<Items> reward = activityReceiveBiz.getReceiveAllReward(player, activityType);
		reward = ItemUtils.mergeItemList(reward);
		if(CollUtil.isEmpty(reward)){
			return;
		}
		itemBiz.addItem(player, reward, LogType.ActivityReward.value(activityType.getType()));
		player.getPlayerActivity().SetChanged();
		JsonMsg jsonMsg = new JsonMsg(MessageComm.S2C_Activity_Receive_All);
		jsonMsg.addProperty("itemList", reward);
		jsonMsg.addProperty("type", type);
		player.sendUserUpdateMsg();
		player.sendMsg(jsonMsg);
	}
	@MsgMethod(MessageComm.C2S_Activity_Task_Reset)
	public void resetActivityTask(Player player, JsonMsg msg){
		int id = msg.getInt("id");
		ActivityTaskTemplate template = activityTaskConfig.getTaskTemplate(id);
		if(template==null){
			return;
		}
		if(!template.isCanReset()){
			return;
		}
		ActivityType activityType = ActivityType.getActivityType(template.getActive_id());
		//==================判断活动是否开启===================================
		AbstractActivity activity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
		if(activity == null || activity.isCloseForPlayer(player)) {
			player.sendErrorMsg(SysConstant.Activity_Close);
			return;
		}
		PlayerActivityValue value = player.getPlayerActivity().getPlayerActivityValue(activityType);
		if(value instanceof IActivityTaskValue){
			IActivityTaskValue taskValue = (IActivityTaskValue) value;
			if(!taskValue.isCanReset(player,id)){
				return;
			}
		}

		ActivityTask task = player.playerActivityTask().getTask(id);
		if(task.getState()!= TaskStatus.REWARDED){
			return;
		}
		List<Items> costs = template.getResetCosts();
		if(CollUtil.isEmpty(costs)){
			return;
		}
		if(!itemBiz.checkItemEnoughAndSpend(player,costs,LogType.ActivityTask.value(id))){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		task.reset();
		player.playerActivityTask().SetChanged();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Activity_Task_Reset,id);


	}

	public static double toShotVal(int score,double second) {
		//分数+.秒数
		double add = score + (10000d - second) / 10000d;
		BigDecimal big = new BigDecimal(add);
		add = big.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		return add;
	}

}
