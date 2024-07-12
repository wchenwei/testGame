package com.hm.action.activity;

import cn.hutool.core.date.DateUtil;
import com.hm.action.mail.biz.MailBiz;
import com.hm.config.excel.ActivityConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.templaextra.ActivityMainTemplate;
import com.hm.container.PlayerContainer;
import com.hm.db.ActivityUtils;
import com.hm.enums.ActivityType;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.RankType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.IActivityKfRank;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class ActivityBiz{


	@Resource
    private MailConfig mailConfig;
	@Resource
    private MailBiz mailBiz;
	@Resource
    private ActivityConfig activityConfig;
	
	public void sendActivityList(Player player) {
		JsonMsg msg = new JsonMsg(MessageComm.S2C_ActivityList);
		msg.addProperty("activityList", ActivityServerContainer.of(player).getActivityList(player));
		player.sendMsg(msg);
		if(player.getPlayerActivity().Changed()) {
			player.sendUserUpdateMsg();
		}
	}
	
	/**
	 * 广播推送所有活动更新给玩家
	 * @param serverId
	 */
	public void broadPlayerActivityUpdate(int serverId) {
		for (Player player : PlayerContainer.getOnlinePlayersByServerId(serverId)) {
			sendActivityList(player);
		}
	}
	
	/**
	 * 广播单一活动变化消息
	 * @param activity
	 */
	public void broadPlayerActivityUpdate(AbstractActivity activity) {
		ActivityItemContainer activityItemContainer = ActivityServerContainer.of(activity.getServerId());
		JsonMsg msg = new JsonMsg(MessageComm.S2C_Activity_OneUpdate);
		msg.addProperty("activity", activity);
		
		PlayerContainer.getOnlinePlayersByServerId(activity.getServerId())
		.stream().filter(e -> activityItemContainer.checkActivityForPlayer(activity, e))
		.forEach(e -> e.sendMsg(msg));
	}
	
	/**
	 * 每日第一次登录时检查
	 * @param player
	 */
	public void checkPlayerActivityForDay(Player player) {
		ActivityItemContainer activityItemContainer = ActivityServerContainer.of(player);
		player.getPlayerActivity().doActivityClose(activityItemContainer);//检查活动是否关闭
		//检查每日登录签到活动
		for (ActivityType type : ActivityType.values()) {
			AbstractActivity activity = activityItemContainer.getAbstractActivity(type);
			if(activity != null && activity.isOpen() && !activity.isCloseForPlayer(player)) {
				activity.checkPlayerLogin(player);
			}
		}
		try {
			ActivityServerContainer.of(player).getActivityList(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 每小时检查数据库任务
	 */
	public void checkActivity() {
		for (ActivityItemContainer itemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
			//检查活动是否结算
			for (AbstractActivity activity : itemContainer.getActivityList()) {
				try {
					//小时检查
					activity.doCheckHourActivity();
					ActivityMainTemplate typeTemplate = activityConfig.getActivityMainTemplate(activity.getType());
					//检查是否到结算时间
					if(typeTemplate != null && typeTemplate.isCanCal()) {
						activity.checkDoCalActivity();
						ActivityUtils.saveOrUpdate(activity);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(DateUtil.thisHour(true) > 0) {
				//0点不执行,在每日重置里再执行
				checkActivityForServer(itemContainer.getServerId());
			}
		}
	}
	
	public void checkActivityForServer(int serverId) {
		ActivityItemContainer itemContainer = ActivityServerContainer.of(serverId);
		List<String> oldIds = itemContainer.getRunActivityIds();
		itemContainer.initContainer();
		List<String> newIds = itemContainer.getRunActivityIds();
		if(!CollectionUtils.isEqualCollection(oldIds, newIds)) {
			broadPlayerActivityUpdate(itemContainer.getServerId());
		}
	}
	
	//活动结束给玩家发送达到条件但未领取的奖励
	public void sendActivityRewardMail(BasePlayer player,
			ActivityType activityType, List<Items> sendRewards) {
		//发送邮件奖励
		mailBiz.sendSysMail(player, MailConfigEnum.ActivityReward, sendRewards, LanguageVo.createStr(activityType.getDesc()));
	}
	//校验活动是否开启
	public boolean checkActivityIsOpen(BasePlayer player, ActivityType activityType) {
		AbstractActivity tempActivity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
		return null!=tempActivity && tempActivity.isOpen() && !tempActivity.isCloseForPlayer(player);
	}
	
	//校验活动是否开启
	public boolean checkActivityIsOpen(BasePlayer player, ActivityType activityType,String activityId) {
		AbstractActivity tempActivity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
		if(tempActivity==null||!tempActivity.isOpen()||tempActivity.isCloseForPlayer(player)){
			return false;
		}
		return StringUtil.equals(activityId, tempActivity.getId());
	}
	
	//校验活动是否结算(true:已经结算过)
	public boolean checkActivityIsCal(BasePlayer player, ActivityType activityType) {
		AbstractActivity tempActivity = ActivityServerContainer.of(player).getAbstractActivity(activityType);
		if(tempActivity==null||!tempActivity.isOpen()||tempActivity.isCloseForPlayer(player)){
			return true;
		}
		return tempActivity.isCalTime();
	}

	/**
	 * 获取活动版本，有需要区分版本的自己加
	 * @param activity
	 * @return
	 */
	public int getActivityVersion(AbstractActivity activity) {
    	return 0;
    }
	
	/**
	 * 获取活动跨服分组的id
	 * @param activity
	 * @return
	 */
	public int getActivityKfId(AbstractActivity activity) {
    	return 0;
    }
	//通过rankType获取和rankType对应活动的rankName(用于跨服排行)
	public String getKfRankName(int serverId,RankType rankType){
		ActivityType activityType = RankType.getActivityTypeByRankType(rankType);
		if(activityType==null){
			return rankType.getRankName();
		}
		AbstractActivity activity = ActivityServerContainer.of(serverId).getAbstractActivity(activityType);
		if(activity==null||!activity.isOpen()){
			return rankType.getRankName();
		}
		if(activity instanceof IActivityKfRank){
			return ((IActivityKfRank)activity).getKfRankName(rankType);
		}
		return rankType.getRankName();
	}

}
