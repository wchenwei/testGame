package com.hm.action.http;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Sets;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.activity.ActivityServerConfBiz;
import com.hm.action.build.biz.RobMathUtils;
import com.hm.action.kf.KfBuildExtortUtils;
import com.hm.action.kf.pk.ServerLevelReward;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.queue.biz.QueueBiz;
import com.hm.config.excel.KfConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.model.activity.AbstractActivity;
import com.hm.model.activity.kfactivity.AbstractKfActivity;
import com.hm.model.activity.kfactivity.KfScoreActivity;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerKfData;
import com.hm.model.serverpublic.kf.pk.KfPkLevelData;
import com.hm.servercontainer.activity.ActivityItemContainer;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.util.ItemUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
@Slf4j
@Service("kfnew.do")
public class KFNewAction {
	
	@Resource
	private ActivityServerConfBiz activityServerConfBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private KfConfig kfConfig;
	@Resource
	private QueueBiz queueBiz;
	@Resource
	private PlayerBiz playerBiz;
	

	public void syncActivity(HttpSession session) {
		activityServerConfBiz.syncActivity();
		
	}
	public void sendGameKfScoreOpen(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			KfScoreActivity scoreActivity = (KfScoreActivity)ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KFScoreWar);
			if(scoreActivity == null) {
				session.Write("0");
				return;
			}
			scoreActivity.doCheckHourActivity();
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void sendKfLevelPlayerServerReward(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
			int snum = Integer.parseInt(session.getParams("snum"));
			ServerLevelReward serverLevelReward = GSONUtils.FromJSONString(session.getParams("serverLevelReward"), ServerLevelReward.class);
			MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.KfPkLevelReward);
			if(mailTemplate == null) {
				log.error("发放段位奖励出错,MailConfigEnum.KfPkLevelReward");
				return;
			}
			for (Map.Entry<Integer,List<Long>> entry : serverLevelReward.getTypeMap().entrySet()) {
				try {
					int type = entry.getKey();
					KfLevelType kfLevelType = KfLevelType.getKfLevelType(type);
					if(kfLevelType == null) {
						log.error("发放段位奖励出错,type不存在:"+type);
						continue;
					}
					List<Long> playerIds = entry.getValue();
					List<Items> itemList = kfConfig.getKfLevelRewardSeason(snum, type);
					log.error("段位:"+type+"="+GSONUtils.ToJSONString(playerIds)+"="+GSONUtils.ToJSONString(itemList));
					mailBiz.sendSysMail(serverId, Sets.newHashSet(playerIds), MailConfigEnum.KfPkLevelReward,itemList, LanguageVo.createStr(kfLevelType.getDesc()));
				} catch (Exception e) {
					log.error("发放段位奖励出错",e);
				}
			}
			
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void sendKfLevelChangeKing(HttpSession session) {
		try {
			List<Integer> kingIds = StringUtil.splitStr2IntegerList(session.getParams("kings"), "_"); 
			List<Integer> serverIds = StringUtil.splitStr2IntegerList(session.getParams("serverIds"), "_"); 
			for (int serverId : serverIds) {
				try {
					ServerKfData serverKfData = ServerDataManager.getIntance().getServerData(serverId).getServerKfData();
					KfPkLevelData kfPkLevelData = (KfPkLevelData)serverKfData.getKfData(KfType.PKLevel);
					if(kfPkLevelData == null) {
						kfPkLevelData = new KfPkLevelData();
						serverKfData.setKfData(KfType.PKLevel, kfPkLevelData);
					}
					kfPkLevelData.changeKings(kingIds);
					serverKfData.save();
					serverKfData.getContext().broadServerUpdate();
				} catch (Exception e) {
					log.error(serverId+"发放段位kings出错",e);
				}
			}
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}


    public void kfActivityTest(HttpSession session) {
		for (ActivityItemContainer activityItemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
            for (AbstractActivity activity : activityItemContainer.getActivityList()) {
                if (activity instanceof AbstractKfActivity) {
                    activityItemContainer.removeActivity(activity.getId());
                }
			}
		}
		activityServerConfBiz.syncActivity();
		for (ActivityItemContainer activityItemContainer : ActivityServerContainer.getServerMap().getAllContainer()) {
            for (AbstractActivity activity : activityItemContainer.getActivityList()) {
                if (activity instanceof AbstractKfActivity) {
                    activity.doCheckHourActivity();
                }
			}
		}
		session.Write("1");
	}
	
	public void calRobItem(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null) {
				double rate = Double.parseDouble(session.getParams("rate"));
				long robMax = Long.parseLong(session.getParams("robMax"));
				List<Integer> buildIds = StringUtil.splitStr2IntegerList(session.getParams("buildIds"), ",");
				List<Items> rewardItems = queueBiz.plunder(player, buildIds);
				//按照比例抢劫
				long robVal = Math.min((long)(RobMathUtils.calBeRobMine(player)*rate), robMax);
				boolean isRobCsy = robVal > 0 && playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Crystal, robVal, LogType.BuildRob);
				if(!isRobCsy) {
					robVal = 0;
				}
				if(isRobCsy || CollUtil.isNotEmpty(rewardItems)) {
					player.sendUserUpdateMsg();
					KfBuildExtortUtils.playerAdd(player);
				}
				if(robVal > 0) {
					rewardItems.add(new Items(PlayerAssetEnum.Crystal.getTypeId(), robVal, ItemType.CURRENCY));
				}
				String result= ItemUtils.itemListToString(rewardItems);
				session.Write(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
	}
}
