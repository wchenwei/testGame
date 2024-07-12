package com.hm.enums;

public enum CommonValueType {
//    HISTORY_CHAT_COUNT(90), // 每个聊天室的历史记录保存条数
//    HISTORY_HORN_COUNT(91), // 每个聊天室的玩家喇叭保存条数

    BLACK_HOUSE_LV(92), // 玩家超过此等级不封小黑屋
    BLACK_HOUSE_DYNAMIC_LV(93), // 玩家等级不超过最大限制时，进一步判断动态等级
    BLACK_HOUSE_SECONDS(94), // 封小黑屋时长（秒），第一次:第二次:第三次

    Chat_CD(1300), // 聊天间隔

    BLACK_HOUSE_VIP_LV(1302),// 玩家VIP超过此等级不封小黑屋
    BLACK_HOUSE_DYNAMIC_VIP_LV(1303),//玩家VIP等级不超过最大限制时，进一步判断动态等级
    BLACK_HOUSE_Chat_Size(1304),//20,10


    ;

    public int getId() {
        return id;
    }

    private final int id;

    CommonValueType(int id) {
        this.id = id;
    }
}
