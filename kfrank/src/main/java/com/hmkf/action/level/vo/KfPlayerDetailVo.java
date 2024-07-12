package com.hmkf.action.level.vo;

import cn.hutool.core.convert.Convert;
import com.hm.action.troop.redis.PlayerTroopRedisDB;
import com.hm.config.excel.templaextra.EnemyArenaExTemplate;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hmkf.action.npc.NpcPlayer;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.config.template.NpcArenaExTemplate;
import com.hmkf.db.KfDBUtils;
import com.hmkf.model.KFPlayer;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class KfPlayerDetailVo {
    public String id;
    public String name;
    public long combat;
    public int carLv = 1;
    public String icon;
    public int frameIcon;//当前头像框
    public int lvType;
    public long score;
    public int npcId;
    private int mlv = 1;//军衔等级
    private int[] equQuality;//装备品质
    private int rank;

    public KfPlayerDetailVo(KFPlayer player) {
        this.id = player.getId()+"";
        loadPlayerInfo(player.getId());
        this.lvType = player.getLevelType();
        this.score = player.getLevelPlayerInfo().getScore();
    }

    public KfPlayerDetailVo(NpcPlayer npc, NpcTroopTemplate template, long combat) {
        this.id = npc.getId();
        this.name = npc.getName();
        this.icon = template.getHead_icon()+"";
        this.frameIcon = template.getHead_frame();
        this.carLv = template.getCar_lv();
        this.mlv = template.getMilitary_lv();
        this.combat = combat;
        this.lvType = npc.getLevelType();
        this.score = npc.getScore();
        this.npcId = npc.getNpcId();
        this.equQuality = template.getEquQuality();
    }


    public void loadPlayerInfo(long playerId) {
        PlayerRedisData redisData = RedisUtil.getPlayerRedisData(playerId);
        if (redisData != null) {
            this.name = redisData.getName();
            this.icon = redisData.getIcon();
            this.frameIcon = redisData.getFrameIcon();
            this.carLv = redisData.getCarLv();
            this.mlv = redisData.getMlv();
            this.equQuality = redisData.getEquQuality();
            this.combat = redisData.getTroopCombat();
        } else {
            this.name = playerId + "";
            this.icon = "1";
            this.frameIcon = 1;
        }
    }

    public int getIntId() {
        return Convert.toInt(this.id,0);
    }

    public boolean isNpc() {
        return KFLevelConstants.isNpc(id);
    }
}
