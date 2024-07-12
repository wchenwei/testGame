package com.hmkf.action.npc;

import com.google.common.collect.Lists;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.libcore.util.GameIdUtils;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.config.template.NpcArenaExTemplate;
import com.hmkf.db.KfDBUtils;
import com.hmkf.model.IPKPlayer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NpcPlayer extends DBEntity<String> implements IPKPlayer {
    private String rankId;
    private int levelType;
    private int npcId;
    private long score;
    private String name;
    private long combat;
    private int day;

    private List<Integer> wlist = Lists.newArrayList();//战斗的分钟id

    public NpcPlayer(String rankId, int levelType, NpcArenaExTemplate template) {
        setId("npc_"+GameIdUtils.nextStrId());
        this.rankId = rankId;
        this.levelType = levelType;
        this.npcId = template.getId();
        this.day = template.getDay();
        this.score = KFLevelConstants.InitScore;
        this.wlist = NpcCheckBiz.randomWList();
        this.combat = template.getPower();
    }




    public void save() {
        KfDBUtils.getMongoDB().save(this);
    }

    @Override
    public void addScore(long add) {
        this.score += add;
        this.score = Math.max(0,this.score);
    }

    @Override
    public String getPlayerId() {
        return getId();
    }

    @Override
    public boolean isNpc() {
        return true;
    }

    public void doDayReset() {
        this.wlist = NpcCheckBiz.randomWList();
    }

}
