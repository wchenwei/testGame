package com.hm.action.personalchat.vo;

import com.google.common.collect.Lists;
import com.hm.model.player.PlayerFriend;
import com.hm.redis.util.RedisUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 * @date 2022/1/12 17:18
 */
@Data
@NoArgsConstructor
public class PlayerFriendVo {
    //我的所有好友
    private List<FriendVo> friendList = Lists.newArrayList();
    //我请求别人好友列表
    public List<FriendVo> requestList = Lists.newArrayList();
    //别人请求我得好友列表
    public List<FriendVo> beRequestList = Lists.newArrayList();

    public PlayerFriendVo(PlayerFriend playerFriend) {
        friendList = playerFriend.getFriendMap().keySet().stream().filter(t -> RedisUtil.isExitPlayer(t)).map(playerId -> new FriendVo(playerId)).collect(Collectors.toList());
        requestList = playerFriend.getRequestMap().entrySet().stream().filter(t -> RedisUtil.isExitPlayer(t.getKey())).map(e -> new FriendVo(e.getKey(), e.getValue())).collect(Collectors.toList());
        beRequestList = playerFriend.getBeRequestMap().entrySet().stream().filter(t -> RedisUtil.isExitPlayer(t.getKey())).map(e -> new FriendVo(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

}
