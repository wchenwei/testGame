package com.hm.enums;


import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hm.model.Player;

import java.util.List;

/**
 * 聊天室类型
 *
 * @author yl
 * @version 2013-3-2
 */
public enum ChatRoomType {
    World(1,"世界聊天"){
		@Override
		public String buildId(String id) {
			return id + "_w";
		}

		@Override
		public String getChatRoomId(int serverId, Player player){
			return StrUtil.format(GameConstants.RoomWorld, serverId);
		}

        @Override
        public String getChatDBCollName(int serverId) {
//            return StrUtil.format(GameConstants.RoomWorld, serverId);
            return "room_world";
        }
    },
	Guild(2,"军团聊天"){
		@Override
		public String buildId(String id) {
			return id + "_g";
		}

		@Override
		public String getChatRoomId(int serverId, Player player){
			int guildId = player.getGuildId();
			if(guildId <= 0){
				return null;
			}
			return getGuildRoomId(serverId, guildId);
		}

        @Override
        public String getChatDBCollName(int serverId) {
//            return StrUtil.format(GameConstants.DBGuildName, serverId);
            return "room_guild";
        }
    },
    Camp(3, "阵营") {
        @Override
        public String buildId(String id) {
            return id + "_c";
        }

        @Override
        public String getChatRoomId(int serverId, Player player) {
            int camp = player.getCamp();
            if (camp <= 0) {
                return null;
            }
            return getCampRoomId(serverId, camp);
        }

        @Override
        public String getChatDBCollName(int serverId) {
            return StrUtil.format(GameConstants.DBCampName, serverId);
        }
    },
    System(4, "系统") { // 系统频道也分阵营

        @Override
        public String buildId(String id) {
            return id + "_s";
        }

        @Override
        public String getChatRoomId(int serverId, Player player) {
            return getSystemRoomId(serverId);
        }

        @Override
        public String getChatDBCollName(int serverId) {
//            return StrUtil.format(GameConstants.DBCampSysName, serverId);
            return "room_sys";
        }
    },
    Area(5, "区域") {
        @Override
        public String buildId(String id) {
            return id + "_a";
        }

        @Override
        public String getChatRoomId(int serverId, Player player) {
            int camp = player.getCamp();
            if (camp <= 0) {
                return null;
            }
            int areaId = player.getAreaId();
            if (areaId <= 0) {
                return null;
            }
            return getAreaRoomId(serverId, camp, areaId);
        }

        @Override
        public String getChatDBCollName(int serverId) {
            return StrUtil.format(GameConstants.DBAreaName, serverId);
        }
    },
    ;

    /**
     * @param type
     * @param desc
     */
    private ChatRoomType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;

    private String desc;

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static ChatRoomType getType(int type) {
        for (ChatRoomType kind : ChatRoomType.values()) {
            if (type == kind.getType()) return kind;
        }
        return null;
    }

    public static boolean isFilterWord(ChatRoomType type) {
        return type != System;
    }

    public String buildId(String id) {
        return id;
    }

    public String getChatRoomId(int serverId, Player player) {
        return null;
    }

    public abstract String getChatDBCollName(int serverId);


    public static String getGuildRoomId(int serverId, int guildId) {
        return StrUtil.format(GameConstants.Guild, serverId, guildId);
    }

    public static String getCampRoomId(int serverId, int camp) {
        return StrUtil.format(GameConstants.RoomCamp, serverId, camp);
    }

    public static String getAreaRoomId(int serverId, int camp, int area) {
        return StrUtil.format(GameConstants.RoomArea, serverId, camp, area);
    }

    public static String getSystemRoomId(int serverId) {
        return StrUtil.format(GameConstants.RoomSys, serverId);
    }

    public static boolean isSave(int type) {
        return true;
    }


    public static List<ChatRoomType> PlayerRoomList = Lists.newArrayList(ChatRoomType.World,ChatRoomType.Guild,ChatRoomType.System);
}
