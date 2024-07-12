package com.hm.log;

import com.google.common.collect.Maps;

import java.util.Map;

public enum AliLogType {
	PlayerItemLog("item_log","道具变化日志") {
		@Override
		public Map<String, String> getIndexMap() {
			Map<String,String> indexMap = createPlayerMap();
			indexMap.put("itemId","long");
			indexMap.put("itemType","long");
			indexMap.put("num","long");
			indexMap.put("logType","long");
			indexMap.put("type","long");
			indexMap.put("total","long");
			indexMap.put("logExtra","text");
			indexMap.put("channel","long");
			return indexMap;
		}
	},
	PlayerDelMailLog("player_delmail_log","删除邮件日志") {
		@Override
		public Map<String, String> getIndexMap() {
			Map<String,String> indexMap = Maps.newHashMap();
			indexMap.put("playerId","long");
			indexMap.put("channel","long");
			return indexMap;
		}
	},
	PlayerLogin("player_login","玩家登陆注册日志") {
		@Override
		public Map<String, String> getIndexMap() {
			Map<String,String> indexMap = Maps.newHashMap();
			indexMap.put("playerId","long");
			indexMap.put("serverId","long");
			indexMap.put("uid","long");
			indexMap.put("phone","text");
			indexMap.put("imei","text");
			indexMap.put("type","long");//0-登录 1-注册
			indexMap.put("createTime","long");//0-登录 1-注册
			indexMap.put("channel","long");
			return indexMap;
		}
	},
	PlayerRechargeLog("player_recharge_log","玩家充值日志") {
		@Override
		public Map<String, String> getIndexMap() {
			Map<String,String> indexMap = createPlayerMap();
			indexMap.put("rechargeId","long");
			indexMap.put("rmb","long");
			indexMap.put("uid","long");
			indexMap.put("channel","long");
			return indexMap;
		}
	},
	PlayerTaskLog("player_task_log", "玩家任务日志") {
        @Override
        public Map<String, String> getIndexMap() {
            Map<String,String> indexMap = Maps.newHashMap();
            indexMap.put("playerId","long");
            indexMap.put("serverId","long");
            indexMap.put("taskId","long");
			indexMap.put("channel","long");
            return indexMap;
        }
    },
    PlayerLevel("player_level_log", "玩家任务日志") {
        @Override
        public Map<String, String> getIndexMap() {
            Map<String,String> indexMap = Maps.newHashMap();
            indexMap.put("playerId","long");
            indexMap.put("serverId","long");
            indexMap.put("openServerDay","long");
			indexMap.put("channel","long");
            return indexMap;
        }
    },
    PlayerBattle("player_battle_log", "玩家副本通关日志") {
        @Override
        public Map<String, String> getIndexMap() {
            Map<String,String> indexMap = Maps.newHashMap();
            indexMap.put("playerId","long");
            indexMap.put("serverId","long");
            indexMap.put("militaryLv","long");
            indexMap.put("combat","long");
            indexMap.put("missionId", "long");
            indexMap.put("battleType","long");
            indexMap.put("result", "long");
			indexMap.put("channel","long");
            return indexMap;
        }
    },
    PlayerActionLog("player_action_log", "玩家行为日志") {
        @Override
        public Map<String, String> getIndexMap() {
            Map<String,String> indexMap = Maps.newHashMap();
            indexMap.put("playerId","long");
            indexMap.put("serverId","long");
			indexMap.put("channel","long");
            indexMap.put("type","long");
            indexMap.put("militaryLv","long");
            indexMap.put("extra","text");
            return indexMap;
        }
    },
    TeamItemLog("team_item_log", "团队道具变化日志") {
		@Override
		public Map<String, String> getIndexMap() {
			Map<String,String> indexMap = Maps.newHashMap();
			indexMap.put("playerId","long");
			indexMap.put("teamId","long");
			indexMap.put("itemId","long");
			indexMap.put("itemType","long");
			indexMap.put("num","long");
			indexMap.put("type","long");
			indexMap.put("extra","text");
			indexMap.put("channel","long");
			return indexMap;
		}
	},
    PlayerPersonChatLog("player_personchat_log", "玩家私聊日志") {
        @Override
        public Map<String, String> getIndexMap() {
            Map<String, String> indexMap = Maps.newHashMap();
            indexMap.put("playerId", "long");
            indexMap.put("toPlayerId", "long");
            indexMap.put("content", "text");
            indexMap.put("serverId", "long");
			indexMap.put("channel","long");
            return indexMap;
        }
    },
	PlayerTroopLog("player_troop_log", "玩家部队变化日志") {
		@Override
		public Map<String, String> getIndexMap() {
			Map<String,String> indexMap = Maps.newHashMap();
			indexMap.put("playerId","long");
			indexMap.put("serverId","long");
			indexMap.put("channel","long");
			indexMap.put("militaryLv","long");
			indexMap.put("lv1","double");
			indexMap.put("lv2","double");
			indexMap.put("lv3","double");
			indexMap.put("lv4","double");
			indexMap.put("lv5","double");
			indexMap.put("fStar1","double");
			indexMap.put("fStar2","double");
			indexMap.put("fStar3","double");
			indexMap.put("fDriverLv1","double");
			indexMap.put("fDriverLv2","double");
			indexMap.put("fDriverLv3","double");
			return indexMap;
		}
	},
	PlayerEquipLog("player_equip_log", "玩家装备变化日志") {
		@Override
		public Map<String, String> getIndexMap() {
			Map<String,String> indexMap = Maps.newHashMap();
			indexMap.put("playerId","long");
			indexMap.put("serverId","long");
			indexMap.put("channel","long");
			indexMap.put("militaryLv","long");
			indexMap.put("type","long");
			indexMap.put("idx0","double");
			indexMap.put("idx1","double");
			indexMap.put("idx2","double");
			indexMap.put("idx3","double");
			indexMap.put("idx4","double");
			indexMap.put("idx5","double");
			indexMap.put("idx6","double");
			indexMap.put("idx7","double");
			return indexMap;
		}
	},
	/*
	 * PlayerFbLog("player_fb_log","副本日志") {
	 * 
	 * @Override public Map<String, String> getIndexMap() { Map<String,String>
	 * indexMap = createPlayerMap(); indexMap.put("missionId","long");
	 * indexMap.put("win","long"); indexMap.put("star","long");
	 * indexMap.put("sweep","long"); indexMap.put("combat","long"); return indexMap;
	 * } },
	 */
	;
	
	private AliLogType(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}
	
	private String name;
	private String desc;
	
	public String getName() {
		return name;
	}
	
	public static Map<String,String> createPlayerMap() {
		Map<String,String> indexMap = Maps.newHashMap();
		indexMap.put("playerId","long");
		indexMap.put("level","long");
		indexMap.put("commLv","long");
		indexMap.put("vipLv","long");
		indexMap.put("serverId","long");
		indexMap.put("gameSecond","long");
		return indexMap;
	}
	
	public abstract Map<String,String> getIndexMap();
}
