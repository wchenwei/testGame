
package com.hm.rmi;

import cn.hutool.core.date.DateUtil;
import com.aliyun.openservices.log.common.LogItem;
import com.google.common.collect.Lists;
import com.hm.action.item.ItemBiz;
import com.hm.enums.ActionType;
import com.hm.enums.LogType;
import com.hm.log.AliLogActionBiz;
import com.hm.log.AliLogType;
import com.hm.log.SlgAliLog;
import com.hm.log.ali.AliLogProducerUtil;
import com.hm.model.player.BasePlayer;
import com.hm.model.serverpublic.ServerData;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.observer.ObservableEnum;
import com.hm.redis.PlayerRedisData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Title: LogServerRmiHandler.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年8月28日 上午10:03:24
 * @version 1.0
 */
@Slf4j
@Service
public class LogServerRmiHandler{

	@Resource
	private ItemBiz itemBiz;
	@Resource
	private AliLogActionBiz aliLogActionBiz;
	
	public void addItemInLog(BasePlayer player,int itemId,int itemType,long num,LogType logType){
		addItemLog(player, itemId, itemType, num, logType, 1);
		player.notifyObservers(ObservableEnum.ItemChange, itemId, itemType, num, logType, 1);
	}
	public void addItemOutLog(BasePlayer player,int itemId,int itemType,long num,LogType logType){
		addItemLog(player, itemId, itemType, num, logType, 0);
		player.notifyObservers(ObservableEnum.ItemChange, itemId, itemType, num, logType, 0);
	}
	public void addItemLog(BasePlayer player,int itemId,int itemType,long num,LogType logType,int type){
		if(logType == null) {
			return;
		}
		try {
			SlgAliLog log = new SlgAliLog(AliLogType.PlayerItemLog);
			log.putPlayer(player);
			log.put("itemId", itemId);
			log.put("itemType", itemType);
			log.put("num", num);
			log.put("logType", logType.getCode());
			log.put("total", itemBiz.getItemTotal(player, itemType, itemId));
			String logExtra = logType.getExtra();
			if(logExtra != null) {
				log.put("logExtra", logExtra);
			}
			log.put("type", type);
			log.sendLog();
		} catch (Exception e) {
			log.error("添加物品收入日志出错", e);
		}
	}
	public void addPlayerLoginLog(BasePlayer player,int type){
		try {
			SlgAliLog log = new SlgAliLog(AliLogType.PlayerLogin);
			log.put("playerId", player.getId());
			log.put("name", player.getName());
			log.put("uid", player.getUid());
			log.put("imei", player.playerBaseInfo().getImei());
			log.put("phone", player.playerBaseInfo().getLastPohone());
			log.put("createTime", player.playerBaseInfo().getCreateDate().getTime());
			log.put("type", type);
			log.setServerId(player.getServerId());
			log.sendLog();
		} catch (Exception e) {
			log.error("添加物品收入日志出错", e);
		}
	}
	
	public void addPlayerActionLog(BasePlayer player,int type,String extra){
		try {
			SlgAliLog log = new SlgAliLog(AliLogType.PlayerActionLog);
			log.putPlayer(player);
			log.put("uid", player.getUid());
			log.put("type", type);
			log.put("extra", extra);
			log.sendLog();
		} catch (Exception e) {
			log.error("添加玩家行为日志出错", e);
		}
	}

    public void addPlayerPersonChatLog(BasePlayer player, long toPlayerId, String content) {
        try {
            SlgAliLog log = new SlgAliLog(AliLogType.PlayerPersonChatLog);
            log.putPlayer(player);
            log.put("toPlayerId", toPlayerId);
            log.put("content", content);
            log.sendLog();
        } catch (Exception e) {
            log.error("添加玩家行为日志出错", e);
        }
    }

	public void addRechargeLog(BasePlayer player,int rechargeId,long rmb){
		try {
			SlgAliLog log = new SlgAliLog(AliLogType.PlayerRechargeLog);
			log.putPlayer(player);
			log.put("uid", player.getUid());
			log.put("rechargeId", rechargeId);
			log.put("rmb", rmb);
			log.sendLog();
		} catch (Exception e) {
			log.error("添加充值记录日志出错", e);
		}
	}
	public void addDelMailLog(BasePlayer player,List<String> ids){
		try {
			List<LogItem> logItems = Lists.newArrayList();
			for (int i = ids.size()-1; i >= 0; i--) {
				String mailId = ids.get(i);
				LogItem logItem = new LogItem((int)(System.currentTimeMillis()/1000));
				logItem.PushBack("playerId", player.getId()+"");
				logItem.PushBack("mailId", mailId);
				logItems.add(logItem);
			}
			AliLogProducerUtil.sendLog(AliLogType.PlayerDelMailLog.getName(), logItems);
		} catch (Exception e) {
			log.error("添加删除邮件日志出错", e);
		}
	}

	public void addTaskLog(BasePlayer player,int taskType, int status, int taskId, long timeUsedSecond) {
		try {
			SlgAliLog log = new SlgAliLog(AliLogType.PlayerTaskLog);
			log.putPlayer(player);
			log.put("taskId", taskId);
			log.put("taskType", taskType);
			log.put("status", status);
			log.put("spend", timeUsedSecond);
			log.sendLog();
		} catch (Exception e) {
			log.error("添加任务日志出错, " + player.getId() + " : " + taskId, e);
		}
	}
    public void addTaskLog(BasePlayer player,int taskType, int status, int taskId) {
        try {
            SlgAliLog log = new SlgAliLog(AliLogType.PlayerTaskLog);
            log.putPlayer(player);
            log.put("taskId", taskId);
			log.put("taskType", taskType);
			log.put("status", status);
            log.sendLog();
        } catch (Exception e) {
            log.error("添加任务日志出错, " + player.getId() + " : " + taskId, e);
        }
    }
    public void addPlayerLevel(BasePlayer player){
    	try {
            SlgAliLog log = new SlgAliLog(AliLogType.PlayerLevel);
            log.putPlayer(player);
            ServerData serverData = ServerDataManager.getIntance().getServerData(player.getServerId());
            //开服第几天创建的玩家
            log.put("createServerDay", DateUtil.betweenDay(serverData.getServerOpenData().getFirstOpenDate(), player.playerBaseInfo().getCreateDate(), true) + 1);
            log.sendLog();
        } catch (Exception e) {
            log.error("添加等级日志出错, " + player.getId() + " : " + player.playerLevel().getLv(), e);
        }
    }
	public void addPlayerBattleLog(BasePlayer player,int battleType, int missionId,long combat,int result) {
		try {
            SlgAliLog log = new SlgAliLog(AliLogType.PlayerBattle);
            log.putPlayer(player);
            //副本类型
            log.put("battleType", battleType);
            log.put("missionId", missionId);
            log.put("combat", combat);
            log.put("result", result);
            log.sendLog();
        } catch (Exception e) {
            log.error("添加战役日志出错, " + player.getId() + " : " + missionId, e);
        }
	}
	
	public void addPVELog(BasePlayer player,int missionId,boolean win,int star, boolean isSweep){
		try {
			/*
			 * SlgAliLog log = new SlgAliLog(AliLogType.PlayerFbLog); log.putPlayer(player);
			 * log.put("missionId", missionId); log.put("win", win?1:0); log.put("star",
			 * star); log.put("sweep", isSweep?1:0); log.put("combat",
			 * player.getPlayerDynamicData().getCombat()); log.sendLog();
			 */
		} catch (Exception e) {
			log.error("添加PVE日志出错", e);
		}
	}

	public void addPlayerActionLog(PlayerRedisData player, ActionType type, String extra){
		try {
			SlgAliLog log = new SlgAliLog(AliLogType.PlayerActionLog);
			log.putPlayer(player);
			log.put("uid", player.getUid());
			log.put("type", type.getCode());
			log.put("extra", extra);
			log.put("channel", player.getChannelId());
			log.sendLog();
		} catch (Exception e) {
			log.error("添加玩家行为日志出错", e);
		}
	}

	public void addPlayerTroopLog(BasePlayer player){
		try {
			SlgAliLog log = new SlgAliLog(AliLogType.PlayerTroopLog);
			log.putPlayer(player);
			log.put("uid", player.getUid());
			aliLogActionBiz.buildPlayerTroopsMsg(player, log);
			log.sendLog();
		} catch (Exception e) {
			log.error("添加玩家行为日志出错", e);
		}
	}

	public void addPlayerEquipLog(BasePlayer player, int type, double [] lvs){
		try {
			SlgAliLog log = new SlgAliLog(AliLogType.PlayerEquipLog);
			log.putPlayer(player);
			for (int i = 0; i < lvs.length; i++) {
				log.put("idx"+i, lvs[i]);
			}
			log.put("uid", player.getUid());
			log.put("type", type);
			log.sendLog();
		} catch (Exception e) {
			log.error("添加玩家行为日志出错", e);
		}
	}
}

