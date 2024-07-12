package com.hm.model.personalchat;

import com.google.common.collect.Lists;
import com.hm.action.passenger.vo.PersonalChatRoomVo;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.model.player.SimplePlayerVo;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.util.LogMode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@RedisMapperType(type = MapperType.STRING_HASH)
public class PersonalChatRoom extends BaseEntityMapper<String> {
    private List<Long> playerIds = Lists.newArrayList();
    private LogMode<PersonalChatMsg> msgs = new LogMode<PersonalChatMsg>(50);

    public PersonalChatRoom(int serverId, long playerId, long desId) {
        this.setServerId(serverId);
        this.setId(serverId + "_" + PrimaryKeyWeb.getPrimaryKey("PersonalChat", serverId));
        this.playerIds = Lists.newArrayList(playerId, desId);
    }

    public List<Long> getPlayerIds() {
        return playerIds;
    }

    public void addMsg(PersonalChatMsg msg) {
        this.msgs.addLog(msg);
    }

    public PersonalChatRoomVo createRoomVo() {
        List<SimplePlayerVo> playerVos = this.playerIds.stream().map(t -> {
            SimplePlayerVo vo = new SimplePlayerVo();
            PlayerRedisData redisData = RedisUtil.getPlayerRedisData(t);
            if (redisData == null) {
                return null;
            }
            vo.load(redisData);
            return vo;
        }).filter(t -> t != null).collect(Collectors.toList());
        if (playerVos.size() < 2) {
            return null;
        }
        return new PersonalChatRoomVo(this.getId(), playerVos, this.msgs);
    }
}
