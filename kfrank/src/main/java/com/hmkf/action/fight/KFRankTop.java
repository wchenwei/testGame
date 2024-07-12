package com.hmkf.action.fight;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.config.GameConstants;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hmkf.action.level.KfLevelBiz;
import com.hmkf.action.level.vo.KfPlayerDetailVo;
import com.hmkf.model.IPKPlayer;
import com.hmkf.model.KFPlayer;
import com.hmkf.redis.KFRankRedisUtils;
import com.hmkf.redis.RankItem;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KFRankTop {
    private KfPlayerDetailVo[] topList;

    private transient String rankId;
    private transient long minScore;
    private transient List<String> topPlayerIds = Lists.newArrayList();
    private transient long endTime;

    public KFRankTop(String rankId) {
        this.rankId = rankId;
    }

    public void loadFromDB() {
        KfLevelBiz kfLevelBiz = SpringUtil.getBean(KfLevelBiz.class);
        List<RankItem> list =  KFRankRedisUtils.findTop(rankId,3);
        KfPlayerDetailVo[] topList = new KfPlayerDetailVo[list.size()];
        List<String> topPlayerIds = Lists.newArrayList();
        System.out.println(this.rankId+"============top加载====================");
        for (int i = 0; i < list.size(); i++) {
            KfPlayerDetailVo detailVo = kfLevelBiz.buildKfPlayerDetailVo(rankId,list.get(i));
            if(detailVo != null) {
                topList[i] = detailVo;
                topList[i].setRank(i+1);
                topPlayerIds.add(topList[i].getId());
                System.out.println(i+"->"+ GSONUtils.ToJSONString(detailVo));
            }
        }
        this.topPlayerIds = topPlayerIds;
        this.topList = topList;
        this.minScore = Arrays.stream(topList).filter(Objects::nonNull).mapToLong(e -> e.getScore()).min().orElse(0);
        this.endTime = System.currentTimeMillis()+ GameConstants.MINUTE*3;
    }

    public void checkTime() {
        if(System.currentTimeMillis() > this.endTime) {
            loadFromDB();
        }
    }


    public void checkTop(IPKPlayer atk, IPKPlayer def) {
        if(atk.getScore() >= this.minScore || CollUtil.contains(this.topPlayerIds,atk.getPlayerId())
                || CollUtil.contains(this.topPlayerIds,def.getPlayerId())) {
            loadFromDB();
        }
    }
    //重新加载top3数据
    public void doPlayerLogin(KFPlayer player) {
        if(topList == null) {
            return;
        }
        for (int i = 0; i < topList.length; i++) {
            if(topList[i] != null && StrUtil.equals(topList[i].getId(),player.getPlayerId())) {
                topList[i].loadPlayerInfo(player.getId());
            }
        }
    }

}
