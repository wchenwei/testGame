package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.EnemyArenaTrumpTemplate;
import com.hm.config.excel.templaextra.ArenaTrumpRewardTemplateImpl;
import com.hm.config.excel.templaextra.ArenaTrumpTemplateImpl;
import com.hm.enums.TrumpType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
/**
 * Description:
 * User: yang xb
 * Date: 2019-03-28
 */
@Config
public class TrumpArenaConfig extends ExcleConfig {
    /**
     * id:object
     */
    private Map<Integer, EnemyArenaTrumpTemplate> enemyArenaTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, ArenaTrumpRewardTemplateImpl> arenaTrumpRewardTemplateMap = Maps.newConcurrentMap();
    /**
     * stage:floor:object
     */
    private Table<Integer, Integer, ArenaTrumpTemplateImpl> arenaTrumpTemplateTable = HashBasedTable.create();

    @Override
    public void loadConfig() {
        List<EnemyArenaTrumpTemplate> list = JSONUtil.fromJson(getJson(EnemyArenaTrumpTemplate.class), new TypeReference<List<EnemyArenaTrumpTemplate>>() {
        });

        Map<Integer, EnemyArenaTrumpTemplate> tempMap = list.stream().collect(Collectors.toMap(EnemyArenaTrumpTemplate::getId, Function.identity()));
        enemyArenaTemplateMap = ImmutableMap.copyOf(tempMap);

        List<ArenaTrumpRewardTemplateImpl> list1 = JSONUtil.fromJson(getJson(ArenaTrumpRewardTemplateImpl.class), new TypeReference<List<ArenaTrumpRewardTemplateImpl>>() {
        });
        Map<Integer, ArenaTrumpRewardTemplateImpl> tMap = Maps.newConcurrentMap();
        list1.forEach(e -> {
            e.init();
            tMap.put(e.getId(), e);
        });
        arenaTrumpRewardTemplateMap = ImmutableMap.copyOf(tMap);

        List<ArenaTrumpTemplateImpl> list2 = JSONUtil.fromJson(getJson(ArenaTrumpTemplateImpl.class), new TypeReference<List<ArenaTrumpTemplateImpl>>() {
        });
        Table<Integer, Integer, ArenaTrumpTemplateImpl> tbl = HashBasedTable.create();
        list2.forEach(template -> {
            template.init();
            tbl.put(template.getStage(), template.getFloor(), template);
        });

        arenaTrumpTemplateTable = ImmutableTable.copyOf(tbl);
    }

    /**
     * 获取特定段位npc
     *
     * @param trumpType
     * @return
     */
    public Collection<EnemyArenaTrumpTemplate> getEnemyArenaTrumpColl(TrumpType trumpType) {
        return enemyArenaTemplateMap.values().stream().filter(c -> c.getFloor_id().equals(trumpType.getType())).
                sorted(Comparator.comparingInt(EnemyArenaTrumpTemplate::getRank)).collect(Collectors.toList());
    }

    public EnemyArenaTrumpTemplate getEnemyArenaTrumpTemplate(int id) {
        return enemyArenaTemplateMap.getOrDefault(id, null);
    }

    public ArenaTrumpRewardTemplateImpl getArenaTrumpRewardTemplateImpl(int id) {
        return arenaTrumpRewardTemplateMap.getOrDefault(id, null);
    }

    public Collection<ArenaTrumpRewardTemplateImpl> getArenaTrumpRewardTemplateImplList() {
        return arenaTrumpRewardTemplateMap.values();
    }

    /**
     * 获取下一期的id
     *
     * @return
     */
    public int getNextStage() {
        return arenaTrumpTemplateTable.rowKeySet().stream().min(Integer::compareTo).orElse(0);
    }

    public int getNextStage(int curStageId) {
        Optional<Integer> min = arenaTrumpTemplateTable.rowKeySet().stream().filter(e -> e > curStageId).min(Integer::compareTo);
        return min.orElseGet(this::getNextStage);
    }

    public ArenaTrumpTemplateImpl getArenaTrumpTemplateImpl(int stage, int floor) {
        return arenaTrumpTemplateTable.get(stage, floor);
    }

    /**
     * 获取该段最大rank的npc id
     *
     * @param trumpType
     * @return
     */
    public int getMaxNpcId(int trumpType) {
        Optional<EnemyArenaTrumpTemplate> max = enemyArenaTemplateMap.values().stream().
                filter(template -> template.getFloor_id().equals(trumpType)).
                max(Comparator.comparing(EnemyArenaTrumpTemplate::getRank));

        if (max.isPresent()) {
            return max.get().getId();
        }
        return 0;
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ArenaTrumpTemplateImpl.class, EnemyArenaTrumpTemplate.class, ArenaTrumpRewardTemplateImpl.class);
    }
}
