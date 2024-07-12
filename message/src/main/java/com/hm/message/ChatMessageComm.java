package com.hm.message;

/**
 * @author 司云龙
 * @version 1.0
 * @date 2021/1/18 21:18
 */
public class ChatMessageComm implements IMessageComm {
    public static final int S2C_ErrorMsg = 2100101;
    public static final int R2G_InnerMsg_Player = 2100110;

    // 游戏服向聊天发消息
    public static final int G2C_SendChatMsg = 2101001;
    // 玩家加入军团
    public static final int G2C_JoinGuild = 2101002;
    // 玩家退出军团
    public static final int G2C_QuitGuild = 2101003;
    // 解散军团
    public static final int G2C_DelGuild = 2101004;
    // 禁言玩家
    public static final int G2C_GagPlayer = 2101005;
    // 玩家加入阵营
    public static final int G2C_JoinCamp = 2101006;
    // 玩家加入阵营
    public static final int G2C_QuitCamp = 2101007;
    // 游戏服向聊天发系統紅包
    public static final int G2C_SysRedMsg = 2101008;
    //清空聊天
    public static final int G2C_ClearChat = 2101009;
    // 玩家区域变化
    public static final int G2C_ChangeArea = 2101010;
    // 重载配置
    public static final int G2C_ReloadJson = 2101011;



    public static final int G2C_PlayerLogin = 2101101;
    public static final int G2C_PlayerChat = 2101102;
    public static final int G2C_PlayerLoginOut = 2101103;


    public static final int S2C_BroadChatMsg = 2101504; //收消息
    public static final int S2C_SendHistoryMsg = 2101505; //历史记录

}
