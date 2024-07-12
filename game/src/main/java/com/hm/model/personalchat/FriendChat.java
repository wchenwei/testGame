package com.hm.model.personalchat;

import com.google.common.collect.Lists;
import com.hm.action.personalchat.vo.ChatVo;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.libcore.springredis.util.RedisMapperUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wyp
 * @description 玩家不在线时  收到的消息数据
 * @date 2021/12/15 9:15
 */
@Data
@NoArgsConstructor
@RedisMapperType(type = MapperType.STRING_HASH)
public class FriendChat extends BaseEntityMapper<Long> {

    private List<ChatVo> chatVos = Lists.newArrayList();

    public FriendChat(int serverId, long playerId) {
        setId(playerId);
        setServerId(serverId);
    }

    public static FriendChat get(int serverId, long playerId) {
        FriendChat vo = RedisMapperUtil.queryOne(serverId, playerId, FriendChat.class);
        return vo == null ? new FriendChat(serverId, playerId) : vo;
    }

    public void addMsgAndSave(ChatVo chatVo) {
        this.chatVos.add(chatVo);
        saveDB();
    }

    public long getPlayerId() {
        return getId();
    }

    public void del() {
        this.delete();
    }

}
