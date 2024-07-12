/**
 * Project Name:SLG_ChatFuture.
 * File Name:Player.java
 * Package Name:com.hm.model
 * Date:2018年2月23日下午3:17:29
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.
 */
package com.hm.model;

import com.hm.libcore.db.mongo.DBEntity;
import com.hm.db.PlayerUtils;
import com.hm.libcore.chat.ChatMsgType;
import com.hm.enums.ChatRoomType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;

@Data
@NoArgsConstructor
public class ChatMsg extends DBEntity<String> {
    @Indexed
    private long playerId; //玩家id

    private int msgType;
    private String content;
    private long date;
    private String extend; //扩展字段,分享战报代表战报id,红包代表红包id,前线战争分享则代表了分享的关卡id

    @Transient
    private Player playerInfo; //玩家信息


    private transient int roomType;
    private transient String roomId; //房间id

    public void initPlayerInfo() {
        if (playerId > 0) {
            initPlayerInfo(PlayerUtils.getPlayer(playerId));
        }
    }

    public void initPlayerInfo(Player player) {
        this.playerInfo = player;
    }

    public ChatMsg(long playerId, String content, ChatMsgType msgType, ChatRoomType roomType, String extend, int serverId) {
        this.msgType = msgType.getType();
        this.roomType = roomType.getType();
        this.date = System.currentTimeMillis();
        this.playerId = playerId;
        this.content = content;
        this.extend = extend;
        setServerId(serverId);
    }

    public ChatMsg(long playerId, String content, int serverId) {
        this.date = System.currentTimeMillis();
        this.playerId = playerId;
        this.content = content;
        setServerId(serverId);
    }

    public String getDBCollName(int serverId, ChatRoomType roomType) {
        return roomType.getChatDBCollName(serverId);
    }
}
