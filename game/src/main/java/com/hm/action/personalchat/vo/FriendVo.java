package com.hm.action.personalchat.vo;

import com.hm.db.PlayerUtils;
import com.hm.model.friend.FriendVal;
import com.hm.model.player.SimplePlayerVo;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.data.GuildRedisData;
import com.hm.redis.util.RedisUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 好友信息
 *
 * @author 司云龙
 * @version 1.0
 * @date 2021/10/13 10:11
 */
@Data
@NoArgsConstructor
public class FriendVo extends SimplePlayerVo {
    public boolean online;
    //public long lastChatTime;
    public long lastLeaveTime;
    // 时间戳
    private long extendTime;
    private String guildName;

    public FriendVo(long id) {
        this.setPlayerId(id);
        this.online = PlayerUtils.getOnlinePlayer(id) != null;
        loadFriendInfo();
    }

    public FriendVo(long id, long extendTime) {
        this(id);
        this.extendTime = extendTime;
    }

    public FriendVo(long id, FriendVal friendVal) {
        this(id);
        //this.lastChatTime = friendVal.getTime();
    }

    public void loadFriendInfo() {
        PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(this.getPlayerId());
        if (playerRedisData == null) {
            return;
        }
        this.load(playerRedisData);
        if (this.guildId > 0) {
            GuildRedisData guildRedisData = RedisUtil.getGuildRedisData(this.guildId);
            if (guildRedisData == null) {
                return;
            }
            loadGuild(guildRedisData);
            this.guildName = guildRedisData.getGuildName();
        }
    }

}
