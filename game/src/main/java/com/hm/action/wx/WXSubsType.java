package com.hm.action.wx;

import com.google.common.collect.Maps;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.temlate.WxSubscribeTemplateImpl;
import com.hm.db.PlayerUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.model.player.Player;
import com.hm.redis.ServerNameCache;
import com.hm.util.ServerUtils;

import java.util.Date;
import java.util.Map;

public enum WXSubsType {
	SignUp(1,"7日签到") {
		@Override
		public Map<String, WXVal> buildData(long playerId,WxSubscribeTemplateImpl template) {
			String name = ServerNameCache.getServerName(ServerUtils.getCreateServerId(playerId));

			String[] paramArray = template.getParamArrays();
			Map<String,WXVal> dataMap = Maps.newHashMap();
			dataMap.put(paramArray[0],new WXVal(name));
			dataMap.put(paramArray[1],new WXVal("登录送豪礼"));

			return dataMap;
		}

		@Override
		public boolean checkPlayerCanSend(long playerId) {
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null) {
				return !DateUtil.isSameDay(new Date(),player.playerBaseInfo().getLastLoginDate());
			}
			return true;
		}
	},

	WarHelpReward(2,"挂机奖励"){
		@Override
		public Map<String, WXVal> buildData(long playerId,WxSubscribeTemplateImpl template) {
			String[] paramArray = template.getParamArrays();
			Map<String,WXVal> dataMap = Maps.newHashMap();
			dataMap.put(paramArray[0],new WXVal("挂机奖励已满"));
			dataMap.put(paramArray[1],new WXVal("快来领取吧"));
			return dataMap;
		}

		@Override
		public boolean checkPlayerCanSend(long playerId) {
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null) {
				long diffTime = player.playerMissionBox().getRewardBoxTime();
				return diffTime >= CommValueConfig.MissionBoxMaxTime;
			}
			return true;
		}
	},

	GuildWarStart(3,"国战开启预告"){
		@Override
		public Map<String, WXVal> buildData(long playerId,WxSubscribeTemplateImpl template) {
			String[] paramArray = template.getParamArrays();
			String date = DateUtil.formatDate(new Date());
			Map<String,WXVal> dataMap = Maps.newHashMap();
			dataMap.put(paramArray[0],new WXVal("国战开启"));
			dataMap.put(paramArray[1], new WXVal(date+" 20:00:00"));
			dataMap.put(paramArray[2], new WXVal(date+" 21:00:00"));
			dataMap.put(paramArray[3], new WXVal("国战开启"));

			return dataMap;
		}
	},


	;


	private WXSubsType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private int type;
	private String desc;


	public abstract Map<String,WXVal> buildData(long playerId,WxSubscribeTemplateImpl template);

	public boolean checkPlayerCanSend(long playerId) {
		return true;
	}

	public int getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}

	public static WXSubsType getWXSubsType(int type){
		for (WXSubsType armyPowerType:WXSubsType.values()){
			if (armyPowerType.getType() == type){
				return armyPowerType;
			}
		}
		return null;
	}
}
