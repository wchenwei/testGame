package com.hm.action.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.libcore.util.string.StringUtil;
import com.hm.action.activity.ActivityBiz;
import com.hm.action.bag.BagBiz;
import com.hm.action.http.kf.KfMsgBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.titile.TitleBiz;
import com.hm.action.treasury.biz.TreasuryBiz;
import com.hm.action.troop.biz.TroopBiz;
import com.hm.config.excel.*;
import com.hm.config.excel.templaextra.RankTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.model.activity.kfactivity.KfExpeditionActivity;
import com.hm.model.item.Items;
import com.hm.model.kf.PlayerValue;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerKfData;
import com.hm.model.serverpublic.kf.KFSportsData;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.activity.ActivityServerContainer;
import com.hm.sysConstant.ItemConstant;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;
import com.hm.util.PubFunc;
import com.hm.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
@Slf4j
@Service("kf.do")
public class KFAction {
	@Resource
    private PlayerBiz playerBiz;
	@Resource
    private MailBiz mailBiz;
	@Resource
    private RankConfig rankConfig;
	@Resource
	private MailConfig mailConfig;
	@Resource
	private LanguageCnTemplateConfig languageCnTemplateConfig;
	@Resource
	private TitleBiz titleBiz;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private BagBiz bagBiz;
	@Resource
	private KfMsgBiz kfMsgBiz;
	
	@Resource
	private ActivityBiz activityBiz;
	@Resource
    private TreasuryBiz treasuryBiz;
	@Resource
    private TroopBiz troopBiz;
	@Resource
    private CommValueConfig commValueConfig;
	

	/**
	 * 开启指定服务器
	 * @param session
	 */
	public void addPlayerDiamond(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			int gold = Integer.parseInt(session.getParams("gold"));
			long kfId = PubFunc.parseLong(session.getParams("kfId"));
            log.error(playerId + ":addPlayerDiamond " + gold + "  kfId:" + kfId);
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null && gold != 0) {
				int trueGold = Math.abs(gold);
				if(gold < 0) {
					boolean isSuc = playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, trueGold, LogType.KfActivity.value(kfId));
					if(isSuc) {
						player.sendUserUpdateMsg();
						session.Write("1");
						return;
					}
				}else{
					playerBiz.addPlayerCurrency(player, CurrencyKind.Gold, trueGold, LogType.KfActivity.value(kfId));
					player.sendUserUpdateMsg();
					session.Write("1");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	/**
	 * 开启指定服务器
	 * @param session
	 */
	public void addOrSpendPlayerItem(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			String items = URLDecoder.decode(session.getParams("items"), "utf-8");
			List<Items> itemList = ItemUtils.str2ItemList(items, ",", ":");
			boolean isAdd = Integer.parseInt(session.getParams("addType")) == 0;
			long kfId = PubFunc.parseLong(session.getParams("kfId"));
			if(CollUtil.isEmpty(itemList)) {
				session.Write("0");
				return;
			}
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null) {
				if(isAdd) {
					itemBiz.addItem(player, itemList, LogType.KfActivity.value(kfId));
				}else{
					if(!itemBiz.checkItemEnoughAndSpend(player, itemList, LogType.KfActivity.value(kfId))) {
						session.Write("0");
						return;
					}
				}
				player.sendUserUpdateMsg();
				session.Write("1");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void checkSpendPlayerItem(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			String items = URLDecoder.decode(session.getParams("items"), "utf-8");
			List<Items> itemList = ItemUtils.str2ItemList(items, ",", ":");
			if(CollUtil.isEmpty(itemList)) {
				session.Write("1");
				return;
			}
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null && itemBiz.checkItemEnough(player, itemList)) {
				session.Write("1");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void betResultForPlayer(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			int gold = Integer.parseInt(session.getParams("gold"));
			boolean isWin = Boolean.parseBoolean(session.getParams("isWin"));
			Player player = PlayerUtils.getPlayer(playerId);
			int serverId = ServerUtils.getServerId(playerId);
			if(player != null && gold != 0) {
				MailConfigEnum mailConfigEnum = isWin?MailConfigEnum.KfBetWin:MailConfigEnum.KfBetLose;
				Items reward = new Items(PlayerAssetEnum.SysGold.getTypeId(), gold, ItemType.CURRENCY);
				mailBiz.sendSysMail(serverId, playerId, mailConfigEnum, Lists.newArrayList(reward));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("1");
		return;
	}
	
	public void sendServerKfRankReward(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			int rank = Integer.parseInt(session.getParams("rank"));
			Player player = PlayerUtils.getPlayer(playerId);
			RankType rankType = RankType.KfSportsRank;
			if(player != null) {
				RankReward rankReward = rankConfig.getRankReward(rankType);
				if(rankReward != null) {
					RankTemplate rankTemplate = rankReward.getRankTemplate(rank);
					List<Items> itemList = rankTemplate.getRewardList();
					mailBiz.sendSysMail(player, MailConfigEnum.getRankRewardMail(rankType), itemList, LanguageVo.createStr(rank));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void sendServerKfWinId(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("winId"));
			String serverName = URLDecoder.decode(session.getParams("serverName"), "utf-8");
			int serverId = Integer.parseInt(session.getParams("serverId"));
			PlayerRedisData playerData = RedisUtil.getPlayerRedisData(playerId);
			if(playerData == null) {
				return;
			}
			//保存战争之王
			KFSportsData sportsData = new KFSportsData();
			sportsData.setPlayerId(playerId);
			ServerKfData serverKfData = ServerDataManager.getIntance().getServerData(serverId).getServerKfData();
			serverKfData.setKfData(KfType.Sports, sportsData);
			serverKfData.save();
			serverKfData.getContext().broadServerUpdate();
			
			doServerKingTitle(serverId, playerId);
			
			String content = languageCnTemplateConfig.getValue("broadcast_22");
			content = String.format(content, serverName,playerData.getName());
			Map<String, String> params = Maps.newHashMap();
			params.put("serverId", serverId+"");
			params.put("maxTimes", "3");
			params.put("content", content);
			params.put("interval", "1");
			ObserverRouter.getInstance().notifyObservers(ObservableEnum.SYSTS, null, params);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void doServerKingTitle(int serverId,long playerId) {
		try {
			if(GameServerManager.getInstance().containServer(ServerUtils.getCreateServerId(playerId))) {
				Player winPlayer = PlayerUtils.getPlayer(playerId);
				if(winPlayer != null && winPlayer.getServerId() == serverId) {
					titleBiz.addTitle(winPlayer, PlayerTitleType.KING);
					return;
				}
			}
			titleBiz.replacePlayerTitle(serverId, PlayerTitleType.KING,playerId);
		} catch (Exception e) {
			log.error("跨服竞技场称号出错", e);
		}
	}
	
	public void sendPlayerMineReward(HttpSession session) {
		try {
			if(MergeContants.isMergeRunning) {
				//正在合服
				session.Write("0");
				return;
			}
			List<PlayerValue> valueList = GSONUtils.FromJSONString(session.getParams("valueList"), new TypeToken<List<PlayerValue>>(){}.getType());
			for (PlayerValue playerValue : valueList) {
				try {
					Player player = PlayerUtils.getPlayer(playerValue.getPlayerId());
					if(player != null) {
						playerBiz.addPlayerCurrency(player, CurrencyKind.Oil, playerValue.getValue()[0], LogType.KfActivity);
						playerBiz.addPlayerCurrency(player, CurrencyKind.Cash, playerValue.getValue()[1], LogType.KfActivity);
//						playerBiz.addPlayerCurrency(player, CurrencyKind.MineKfScore, playerValue.getScore(), LogType.KfActivity);
						player.sendUserUpdateMsg();
					}
				} catch (Exception e) {
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
	
	
	public void handFullTankHp(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			int totalSecond = Integer.parseInt(session.getParams("totalSecond"));
			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null || totalSecond == 0) {
				session.Write("0");
				return;
			}
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
				session.Write("0");
				return;
			}
			if(spendItemNum > 0) {
				bagBiz.spendItem(player, ItemConstant.TroopRepairHammer, spendItemNum, LogType.HandTroopFullHp);
			}
			player.sendUserUpdateMsg();
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void checkPlayerTroopItems(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null) {
				session.Write("0");
				return;
			}
			Items item = troopBiz.calPlayerCloneItem(player);
			if(!itemBiz.checkItemEnough(player, item)) {
				session.Write("0");
				return;
			}
			session.Write(ItemUtils.itemListToString(Lists.newArrayList(item)));
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	/**
	 * 别人对我宣战
	 * @param session
	 */
	public void sendServerDeclareToTargetId(HttpSession session) {
		try {
			//被宣战的人
			int serverId = Integer.parseInt(session.getParams("targetId"));
			//宣战的人
			int targetId = Integer.parseInt(session.getParams("serverId"));
			
			int declareId = Integer.parseInt(session.getParams("declareId"));
			String declaration = URLDecoder.decode(session.getParams("declaration"), "utf-8");
			String url = URLDecoder.decode(session.getParams("url"), "utf-8");
			
			log.error("别人对我宣战:"+GSONUtils.ToJSONString(session.getParams()));
			KfExpeditionActivity expeditionActivity = (KfExpeditionActivity)ActivityServerContainer.of(serverId).getAbstractActivity(ActivityType.KfExpeditionActivity);
			boolean activityIsOpen = expeditionActivity != null && expeditionActivity.isOpen();
			if(!activityIsOpen) {
				session.Write("0");
				return;
			}
			expeditionActivity.declareServerForMe(declareId,targetId, declaration, url);
			expeditionActivity.saveDB();
			
			activityBiz.broadPlayerActivityUpdate(expeditionActivity);
			
			ServerData serverData = ServerDataManager.getIntance().getServerData(serverId);
			int serverLv = serverData.getServerStatistics().getServerLv();
			session.Write(Math.max(serverLv, 1)+"");
			return;
		} catch (Exception e) {
			log.error("sendServerDeclareToTargetId:",e);
		}
		session.Write("0");
		return;
	}
	
	/**
	 * 购买石油
	 * @param session
	 */
	public void buyTreasury(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			int count = Integer.parseInt(session.getParams("count"));
			int type = Integer.parseInt(session.getParams("type"));
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null) {
				int spend = treasuryBiz.getBuySpend(type,count);
				if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, spend, LogType.KfBuyOil)) {
					session.Write("0");
					return;
				}
				player.sendUserUpdateMsg();
				session.Write(GSONUtils.ToJSONString(treasuryBiz.createTreasuryForItem(player, count, type)));
				return;
			}
		} catch (Exception e) {
			log.error("sendKfMineChange:",e);
		}
		session.Write("0");
		return;
	}
	
	public void reduceTankOil(HttpSession session) {
		try {
			long playerId = Long.parseLong(session.getParams("playerId"));
			List<Integer> tankIds = StringUtil.splitStr2IntegerList(session.getParams("tankIds"), ",");
			String extraInfo = session.getParams("extra");
			
			Player player = PlayerUtils.getPlayer(playerId);
			if(player == null || CollUtil.isEmpty(tankIds)) {
				session.Write("0");
				return;
			}
			long oil = player.playerTank().getTankTotalOil(tankIds);
			if(StrUtil.equals(session.getParams("oilDiscount"), "1")) {
				double disVal = commValueConfig.getDoubleCommValue(CommonValueType.TankOilDiscount);
				if(disVal > 0) {
					oil = (long)(disVal*oil);
				}
			}
			if(!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Oil, oil, LogType.KfActivity.value(extraInfo))){
				player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
				session.Write("0");
				return;
			}
			player.sendUserUpdateMsg();
			session.Write("1");
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public void doKfEventMsg(HttpSession session) {
		try {
			log.error("收到跨服消息:"+GSONUtils.ToJSONString(session.getParams()));
			String msgId = session.getParams("msgId");
			if(kfMsgBiz.doMsgEvent(session,msgId)) {
				session.Write("1");
			}else{
				session.Write("0");
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.Write("0");
		return;
	}
	
	public static void main(String[] args) {
		String items = "1:5:40000000000";
		System.err.println(ItemUtils.str2ItemList(items, ",", ":"));
	}
}
