package com.hm.leaderboards;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.libcore.language.LanguageVo;
import com.hm.action.mail.biz.MailBiz;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.MailVO;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.RankConfig;
import com.hm.config.excel.RankReward;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.config.excel.templaextra.RankTemplate;
import com.hm.db.MailUtils;
import com.hm.db.PlayerUtils;
import com.hm.enums.LogType;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.MailSendType;
import com.hm.enums.RankType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.mail.Mail;
import com.hm.model.player.Player;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.mail.MailServerContainer;
import com.hm.util.PubFunc;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Tuple;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Biz
public class DayRankRewardBiz {
	@Resource
	private RankConfig rankConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private MailBiz mailBiz;



	public void doLeaderboardDayReward(int serverId) {
		ArrayListMultimap<Long, Mail> playerMailMap = ArrayListMultimap.create();
		//发放排行奖励
		for (RankType rankType : RankType.getDayRewardRanks()) {
			try {
				doRedisRankReward(playerMailMap,serverId, rankType);
			} catch (Exception e) {
				log.error(serverId+"每日:服务器排行奖励发放异常：", e);
			}
		}
		//发放阵容荣誉排行
//		sendServerCampHonorRank(serverId,playerMailMap);
		//保存数据库
		MailUtils.saveMailList(serverId, Lists.newArrayList(playerMailMap.values()));
		
		for (long playerId : playerMailMap.keySet()) {
			try {
				sendPlayerMailList(serverId, playerId, playerMailMap.get(playerId));
			} catch (Exception e) {
				log.error(serverId+"_"+playerId+"每日:服务器排行奖励发放异常：", e);
			}
		}
	}

	
	public void sendPlayerMailList(int serverId,long playerId,List<Mail> mailList) {
		Player player = PlayerUtils.getPlayer(playerId);
		if(player != null) {
			List<MailVO> mailVoList = Lists.newArrayList();
			for (Mail mail : mailList) {
				MailServerContainer.of(serverId).addMail(mail);
				player.playerMail().addMail(mail);
				mailVoList.add(MailBiz.createMailVO(player, mail));
			}
			JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_AddMailList);
			serverMsg.addProperty("mailList",mailVoList);
			player.sendMsg(serverMsg);
			player.saveDB();
		}
	}


	public void doRedisRankReward(ArrayListMultimap<Long, Mail> playerMailMap,int serverId,RankType rankType) {
		String rankName = rankType.getRankName();
		if(rankType.isDayReset()){
			rankName = rankName +":"+RankRedisUtils.createMark();
		}
		if (rankType.isDayResetName()){
			rankName = rankType.getRankName(RankRedisUtils.createMark());
		}
		doRedisRankReward(playerMailMap,serverId,rankType,rankName);
	}
	
	public void doRedisRankReward(ArrayListMultimap<Long, Mail> playerMailMap,int serverId,RankType rankType,String rankName) {
		RankReward rankReward = rankConfig.getRankReward(rankType,serverId);
		if(rankReward == null) {
			log.error(serverId+"处理排行奖励出错每日奖励:"+rankType.getDesc());
			return;
		}
		System.err.println(serverId+"排行奖励:"+rankName);
		List<Tuple> topRanks = RankRedisUtils.getRankList(serverId, rankName, 1, rankReward.getMaxRank());
		if(CollUtil.isEmpty(topRanks)) {
			log.error(serverId+"处理排行奖励排行为空:"+rankType.getDesc());
			return;
		}
		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getRankRewardMail(rankType));
		if(mailTemplate == null) {
			log.error(serverId+"处理排行奖励出错每日奖励邮件发放:"+rankType.getDesc());
			return;
		}
		String srcId = serverId+"_"+PrimaryKeyWeb.getPrimaryKey(Mail.class.getSimpleName(), serverId);
		for (int i = 0; i < topRanks.size(); i++) {
			Tuple leaderboardInfo = topRanks.get(i);
			int rank = i +1;
			RankTemplate rankTemplate = rankReward.getRankTemplate(rank);
			if(rankTemplate != null) {
				long playerId = PubFunc.parseInt(leaderboardInfo.getElement());
				if(playerId<=0 
						//判断是否是此机器的玩家,用于跨服排行发放奖励
						|| !GameServerManager.getInstance().isServerMachinePlayer(playerId)){
					continue;
				}
                if (LossPlayerUtils.isSkipRankReward(playerId, rankType)) {
                    log.error(rankName + "跳过:" + playerId + " ->" + rank);
                    continue;//玩家很久没有上线了 不发奖励
                }
				List<Items> itemList = rankTemplate.getRewardList();
				//计算是否添加称号
				addPlayerRankRewardForTitle(playerId,rankTemplate);
				
				String mailId = srcId +"_"+rank;
				Mail mail = new Mail(mailId,serverId,mailTemplate.getMail_id(), itemList,MailSendType.One, LanguageVo.createStr(rank));
				mail.setReceivers(Sets.newHashSet(playerId));
				playerMailMap.put(playerId, mail);
			}
		}
	}

	/**
	 * 给玩家添加称号
	 * @param playerId
	 * @param rankTemplate
	 */
	public void addPlayerRankRewardForTitle(long playerId,RankTemplate rankTemplate) {
		Items titleItem = rankTemplate.getTitleItem();
		if(titleItem == null) {
			return;
		}
		Player player = PlayerUtils.getPlayer(playerId);
		if(player == null) {
			return;
		}
		itemBiz.addItem(player,titleItem, LogType.RankReward);
		//内部已经发送了
//		player.sendUserUpdateMsg();
	}
}
