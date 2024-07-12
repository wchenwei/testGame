package com.hm.log;

import cn.hutool.core.util.StrUtil;
import com.aliyun.openservices.log.common.LogItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.enums.StatisticsType;
import com.hm.log.ali.AliLogProducerUtil;
import com.hm.model.player.BasePlayer;
import com.hm.redis.PlayerRedisData;

import java.util.Map;

public class SlgAliLog {
	private String ip;
	private String logstore;
	private int serverId;
	private Map<String, String> param = Maps.newHashMap();
	private long chargeNum;
	
	public SlgAliLog(AliLogType aliLogType) {
		this.logstore = aliLogType.getName();
	}
	
	public void putPlayer(BasePlayer player) {
		put("playerId", player.getId());
		put("militaryLv", player.playerCommander().getMilitaryLv());
		put("vipLv", player.getPlayerVipInfo().getVipLv());
		put("channel", player.getChannelId());
		long createTime = player.playerBaseInfo().getCreateDate().getTime();
		put("gameSecond", (System.currentTimeMillis()-createTime)/1000);
		this.ip = player.getIp();
		this.chargeNum = player.getPlayerStatistics().getLifeStatistics(StatisticsType.RECHARGEReal);
		setServerId(player.getServerId());
	}

	public void putPlayer(PlayerRedisData player) {
		put("playerId", player.getId());
		put("militaryLv", player.getMlv());
		put("vipLv", player.getVipLv());
		put("channel", player.getChannelId());
		long createTime = player.getCreatetime();
		put("gameSecond", (System.currentTimeMillis()-createTime)/1000);
		this.chargeNum = player.getRechargeGold();
		setServerId(player.getServerid());
	}
	
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public void put(String key,String value) {
		this.param.put(key, value);
	}
	public void put(String key,long value) {
		this.param.put(key, value+"");
	}

	public void put(String key, double value){
		this.put(key, value+"");
	}

	public void sendLog() {
		LogItem logItem = new LogItem((int)(System.currentTimeMillis()/1000));
		logItem.PushBack("serverId",serverId+"");
		if (StrUtil.isNotEmpty(ip)) {
			logItem.PushBack("playerIp", ip);
		}
		for (Map.Entry<String, String> entry : this.param.entrySet()) {
			String value = entry.getValue();
			if(value != null) {
				logItem.PushBack(entry.getKey(), value);
			}
		}
		AliLogProducerUtil.sendLog(this.logstore, Lists.newArrayList(logItem));
	}
	
}
