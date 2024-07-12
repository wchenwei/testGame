package com.hmkf.action.level;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hmkf.action.fight.KFRankTop;
import com.hmkf.action.level.vo.KfPlayerDetailVo;
import com.hmkf.model.IPKPlayer;
import com.hmkf.model.KFPlayer;
import com.hmkf.redis.KFRankRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KfTopBiz {
    private Map<String, KFRankTop> topMap = Maps.newConcurrentMap();

    public void addRank(String rankId) {
        KFRankTop rankTop = new KFRankTop(rankId);
        rankTop.loadFromDB();
        topMap.put(rankId,rankTop);
    }

    public void doPlayerLogin(KFPlayer player) {
        KFRankTop rankTop = this.topMap.get(player.getRankId());
        if(rankTop == null) {
            return;
        }
        rankTop.doPlayerLogin(player);
    }

    public synchronized KFRankTop getKFRankTopAndLoad(String rankId) {
        KFRankTop rankTop = this.topMap.get(rankId);
        if(rankTop != null){
            return rankTop;
        }
        addRank(rankId);
        return this.topMap.get(rankId);
    }

    public void checkTop(IPKPlayer atk,IPKPlayer def) {
        String rankId = atk.getRankId();
        KFRankTop rankTop = this.topMap.get(rankId);
        if(rankTop == null){
            addRank(rankId);
            return;
        }
        rankTop.checkTop(atk,def);
    }

    public void weekClear() {
        this.topMap.clear();
    }


    public void fillPlayerOppoInfo(KFPlayer player, JsonMsg msg) {
        //我的排行
        KFRankTop rankTop = getKFRankTopAndLoad(player.getRankId());
        rankTop.checkTime();

        msg.addProperty("myRank", KFRankRedisUtils.getPlayerRank(player.getRankId(),player.getPlayerId()));
        msg.addProperty("topList",rankTop);
        KfPlayerDetailVo[] oppoInfo = player.getLevelPlayerInfo().getMatchInfos();
        if(oppoInfo != null) {
            if(player.getLevelPlayerInfo().isCanRefreshRank()) {
                List<String> ids = Arrays.stream(oppoInfo)
                        .filter(Objects::nonNull)
                        .map(e -> e.getId()).collect(Collectors.toList());
                Map<String,Long> rankMap = KFRankRedisUtils.getPlayerRank(player.getRankId(),ids);
                for (KfPlayerDetailVo kfPlayerDetailVo : oppoInfo) {
                    if(kfPlayerDetailVo != null && rankMap.containsKey(kfPlayerDetailVo.getId())) {
                        kfPlayerDetailVo.setRank(rankMap.getOrDefault(kfPlayerDetailVo.getId(),9999L).intValue());
                    }
                }
                player.getLevelPlayerInfo().SetChanged();
            }
            msg.addProperty("oppoInfo", oppoInfo);
        }
    }
}
