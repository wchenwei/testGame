package com.hm.action;


import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.chat.ChatMsgType;
import com.hm.enums.ChatRoomType;
import com.hm.message.ChatMessageComm;
import com.hm.libcore.inner.InnerAction;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.SysConstant;
import com.hm.model.Player;
import com.hm.utils.ChatUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 聊天
 * ClassName: ChatAction. <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2018年2月2日 下午4:28:14 <br/>
 *
 * @author yanpeng
 */
@Slf4j
@Action
public class ChatAction extends InnerAction {
    @Resource
    private PlayerContainer playerContainer;
    @Resource
    private ChatBiz chatBiz;

    /**
     * 初始化聊天
     */
    @MsgMethod(ChatMessageComm.G2C_PlayerLogin)
    public void initChat(JsonMsg msg) {
        try {
            long playerId = msg.getInt("playerId");
            int serverId = msg.getInt("serverId");
            int guildId = msg.getInt("guildId");
            log.info("add chat:" + playerId + " serverId:" + serverId);

            Player onlinePlayer = playerContainer.getOnlinePlayer(playerId);
            if (onlinePlayer != null) {//旧连接直接关掉
                chatBiz.doLoginOut(onlinePlayer);
            }

            Player player = PlayerUtils.getPlayer(playerId);
            if (player == null) {
                log.error(playerId+" player is null");
                return;
            }
            player.setServerId(serverId);
            player.setGuildId(guildId);
            playerContainer.addPlayer2Map(player);
            // 把此玩家加入到指定的room里

            if (ChatUtil.isNoChat()) {
                return;
            }
            chatBiz.addPlayer(player);

        } catch (Exception e) {
            log.error("聊天初始化失败", e);
        }
    }

    /**
     * 发消息
     */
    @MsgMethod(ChatMessageComm.G2C_PlayerChat)
    public void chatMsg(JsonMsg msg) {
        if (ChatUtil.isNoChat()) {
            return;
        }
        Player player = playerContainer.getPlayer(msg.getPlayerId());
        player.initPlayerData();//加载最新数据

        if (!player.isCanChat()) {
            player.sendErrorMsg(SysConstant.NOT_CHAT);
            return;
        }
        int type = msg.getInt("type"); //房间类型
        ChatRoomType roomType = ChatRoomType.getType(type);
        if (roomType == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        int msgType = msg.getInt("msgType");
        ChatMsgType chatMsgType = ChatMsgType.getType(msgType);
        if (chatMsgType == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        String content = msg.getString("content");
        chatBiz.sendMsg(player, content, roomType, chatMsgType, msg);
    }
}
