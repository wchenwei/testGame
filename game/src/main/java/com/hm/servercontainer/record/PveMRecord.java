package com.hm.servercontainer.record;

import com.hm.libcore.springredis.base.BaseEntityMapper;
import com.hm.libcore.springredis.common.MapperType;
import com.hm.libcore.springredis.common.RedisMapperType;
import com.hm.model.player.Player;
import com.hm.redis.PlayerRedisData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * pve战斗记录
 */
@Getter
@Setter
@NoArgsConstructor
@RedisMapperType(type = MapperType.STRING_HASH)
public class PveMRecord extends BaseEntityMapper<String> {
    private int type;//类型
    private int mid;//关卡id


    private long pid;//玩家id
    private String name;
    public String icon;
    public int frameIcon;
    private long combat;
//    private long time;

    public PveMRecord(Player player,String id) {
        setId(id);
        setServerId(player.getServerId());
        this.pid = player.getId();
        this.name = player.getName();
        this.icon = player.playerHead().getIcon();
        this.frameIcon = player.playerHead().getFrameIcon();
        this.combat = player.getCombat();
//        this.time = System.currentTimeMillis();
    }

    public PveMRecord(PlayerRedisData playerRedisData,int serverId,String id) {
        setId(id);
        setServerId(serverId);
        this.pid = playerRedisData.getId();
        this.name = playerRedisData.getName();
        this.icon = playerRedisData.getIcon();
        this.frameIcon = playerRedisData.getFrameIcon();
        this.combat = playerRedisData.getCombat();
//        this.time = System.currentTimeMillis();
    }

    public void save() {
        System.out.println(getType()+"->"+mid+" save pverecord");
        saveDB();
    }

    public static List<PveMRecord> getPveMRecords(int serverId){
        return queryListAll(serverId, PveMRecord.class);
    }
}
