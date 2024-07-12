
package com.hmkf.container;

import com.hm.config.GameRandomNameConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hmkf.action.npc.NpcPlayer;
import com.hmkf.config.KfRankConfig;
import com.hmkf.config.template.NpcArenaExTemplate;
import com.hmkf.db.KfDBUtils;
import com.hmkf.redis.RankItem;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class KFNpcContainer {
    public static Map<String, NpcPlayer> npcMap = new ConcurrentHashMap<>();

    public static void init() {
        List<NpcPlayer> npcPlayerList = KfDBUtils.getMongoDB().queryAll(NpcPlayer.class);
        for (NpcPlayer npcPlayer : npcPlayerList) {
            npcMap.put(npcPlayer.getId(),npcPlayer);
        }
    }

    public static NpcPlayer getNpcPlayer(String id) {
        return npcMap.get(id);
    }

    public static void addNpcPlayer(NpcPlayer npcPlayer) {
        npcMap.put(npcPlayer.getId(),npcPlayer);
    }

    public static void weekClear() {
        npcMap.clear();
        KfDBUtils.getMongoDB().dropCollection(NpcPlayer.class);
    }

    public static void doDayZero() {
        for (NpcPlayer value : KFNpcContainer.npcMap.values()) {
            value.doDayReset();
            value.save();
        }
    }

    public static List<String> getAllNpcName() {
        return npcMap.values().stream().map(e -> e.getName()).collect(Collectors.toList());
    }

    public static NpcPlayer createNpcForError(String rankId, RankItem rankItem) {
        System.out.println("npc不存在:"+rankItem.getPlayerId());
        KfRankConfig kfRankConfig = SpringUtil.getBean(KfRankConfig.class);
        GameRandomNameConfig randomNameConfig = SpringUtil.getBean(GameRandomNameConfig.class);
        int levelType = Integer.parseInt(rankId.split("_")[1]);
        List<NpcArenaExTemplate> npcList = kfRankConfig.getNpcIdList(levelType,1);
        NpcPlayer npcPlayer = new NpcPlayer(rankId,levelType,npcList.get(0));
        npcPlayer.setId(rankItem.getPlayerId());
        npcPlayer.setScore((long)rankItem.getScore());
        npcPlayer.setName(randomNameConfig.randomName());
        npcPlayer.save();
        addNpcPlayer(npcPlayer);

        return npcPlayer;
    }
}

