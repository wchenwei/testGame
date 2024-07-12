package com.hm.kfchat;

import com.hm.libcore.msg.JsonMsg;

/**
 * 跨服玩家
 *
 * @author 司云龙
 * @version 1.0
 * @date 2022/3/3 10:23
 */
public interface IKFPlayer {
    int getGroupId();

    int getServerId();

    Object getId();

    default long getPlayerId() {
        return (long) getId();
    }

    default int getTeamId() {
        return getServerId();
    }

    void sendMsg(JsonMsg msg);
}
