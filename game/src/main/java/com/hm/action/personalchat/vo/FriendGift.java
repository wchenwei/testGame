package com.hm.action.personalchat.vo;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.redis.type.RedisTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FriendGift {
    private long id;//玩家id
    private List<Long> playerList = Lists.newArrayList();
    private String mark;

    public FriendGift(long id) {
        this.id = id;
    }

    public void addGet(Player sendPlayer) {
        checkDayClear(sendPlayer.getServerId());
        this.playerList.add(sendPlayer.getId());
        RedisTypeEnum.FriendGift.put(this.id, GSONUtils.ToJSONString(this));
    }

    private void checkDayClear(int serverId) {
        String serverMark = ServerDataManager.getIntance().getServerData(serverId)
                .getServerStatistics().getServerDayMark();
        if(!StrUtil.equals(serverMark,this.mark)) {
            this.playerList.clear();
            this.mark = serverMark;
        }
    }


    public static void addGift(long playerId, Player sendPlayer) {
        FriendGift friendGift = null;
        String result = RedisTypeEnum.FriendGift.get(playerId+"");
        if(StrUtil.isEmpty(result)) {
            friendGift = new FriendGift(playerId);
        }else{
            friendGift = GSONUtils.FromJSONString(result,FriendGift.class);
        }
        friendGift.addGet(sendPlayer);
    }

    public static List<Long> getGiftPlayerList(BasePlayer player) {
        String result = RedisTypeEnum.FriendGift.get(player.getId()+"");
        if(StrUtil.isEmpty(result)) {
            return null;
        }
        RedisTypeEnum.FriendGift.del(player.getId()+"");
        FriendGift friendGift = GSONUtils.FromJSONString(result,FriendGift.class);
        friendGift.checkDayClear(player.getServerId());
        return friendGift.getPlayerList();
    }
}
