package com.hm.libcore.chat;

/**
 * 聊天消息类型
 *
 * @author yl
 * @version 2013-3-2
 */
public enum ChatMsgType {
    Notice(1, "公告"),
    Normal(2, "普通聊天"),
    Voice(3, "语音聊天"),
    WarRecord(4, "分享"),
    Horn(5, "用户喇叭"),
    RedPacket(6, "红包"),
    Build_Troop(7, "组队要求") ,
    ;

    ChatMsgType(int type, String desc) {
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

    public static ChatMsgType getType(int type) {
        for (ChatMsgType kind : ChatMsgType.values()) {
            if (type == kind.getType()) return kind;
        }
        return null;
    }

    /**
     * 是否需要保存到数据库
     */
    public static boolean isSave(int type) {
        return type == Normal.type || type == Voice.getType();
    }
}
