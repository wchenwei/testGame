package com.hm.chat;


/**
 * 聊天室类型
 *
 * @author yl
 * @version 2013-3-2
 */
public enum ChatRoomType {
    World(1,"世界聊天"),
	Guild(2,"军团聊天"),
    Camp(3, "阵营") ,
    System(4, "系统") ,
    Area(5, "区域") ,
    Build_Troop(6, "组队要求") ,

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

}
