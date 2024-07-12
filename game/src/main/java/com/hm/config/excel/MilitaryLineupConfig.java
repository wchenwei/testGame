package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.MilitaryLineupAssistanceTemplateImpl;
import com.hm.config.excel.templaextra.MilitaryLineupLevelTemplateImpl;
import com.hm.config.excel.templaextra.MilitaryLineupTypeTemplateImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.google.common.collect.*;
/**
 * Description:
 * User: yang xb
 * Date: 2019-09-03
 */
@Config
public class MilitaryLineupConfig extends ExcleConfig {
    /**
     * MilitaryLineupTypeTemplateImpl::getId : MilitaryLineupTypeTemplateImpl
     */
    private Map<Integer, MilitaryLineupTypeTemplateImpl> typeMap = Maps.newConcurrentMap();

    /**
     * MilitaryLineupLevelTemplate::getType,MilitaryLineupLevelTemplate::getMi_level,object
     */
    private Table<Integer, Integer, MilitaryLineupLevelTemplateImpl> levelTable = HashBasedTable.create();
    /**
     * MilitaryLineupAssistanceTemplateImpl::getType,MilitaryLineupAssistanceTemplateImpl::getLevel,object
     */
    private Table<Integer, Integer, MilitaryLineupAssistanceTemplateImpl> assistanceTable = HashBasedTable.create();

    public MilitaryLineupTypeTemplateImpl getTypeCfg(int id) {
        return typeMap.getOrDefault(id, null);
    }

    public MilitaryLineupLevelTemplateImpl getLevelCfg(int id, int lv) {
        return levelTable.get(id, lv);
    }

    public MilitaryLineupAssistanceTemplateImpl getAssistanceCfg(int id, int lv) {
        return assistanceTable.get(id, lv);
    }

    public MilitaryLineupAssistanceTemplateImpl getAssistanceCfg(int id, long power) {
        Optional<MilitaryLineupAssistanceTemplateImpl> max = assistanceTable.row(id).values().stream().filter(e -> e.getPower() <= power)
                .max(Comparator.comparingLong(MilitaryLineupAssistanceTemplateImpl::getPower));
        return max.orElse(null);
    }

    @Override
    public void loadConfig() {
        List<MilitaryLineupTypeTemplateImpl> list = JSONUtil.fromJson(getJson(MilitaryLineupTypeTemplateImpl.class), new TypeReference<List<MilitaryLineupTypeTemplateImpl>>() {
        });

        Map<Integer, MilitaryLineupTypeTemplateImpl> tmpTypeMap = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            tmpTypeMap.put(e.getId(), e);
        });

        typeMap = ImmutableMap.copyOf(tmpTypeMap);

        List<MilitaryLineupLevelTemplateImpl> list1 = JSONUtil.fromJson(getJson(MilitaryLineupLevelTemplateImpl.class), new TypeReference<List<MilitaryLineupLevelTemplateImpl>>() {
        });

        Table<Integer, Integer, MilitaryLineupLevelTemplateImpl> tmpLevelTable = HashBasedTable.create();
        list1.forEach(e -> {
            e.init();
            tmpLevelTable.put(e.getType(), e.getMi_level(), e);
        });

        levelTable = ImmutableTable.copyOf(tmpLevelTable);

        List<MilitaryLineupAssistanceTemplateImpl> list2 = JSONUtil.fromJson(getJson(MilitaryLineupAssistanceTemplateImpl.class), new TypeReference<List<MilitaryLineupAssistanceTemplateImpl>>() {
        });

        Table<Integer, Integer, MilitaryLineupAssistanceTemplateImpl> tmpAssistanceTable = HashBasedTable.create();
        list2.forEach(e -> {
            e.init();
            tmpAssistanceTable.put(e.getType(), e.getLevel(), e);
        });

        assistanceTable = ImmutableTable.copyOf(tmpAssistanceTable);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(MilitaryLineupAssistanceTemplateImpl.class, MilitaryLineupLevelTemplateImpl.class, MilitaryLineupTypeTemplateImpl.class);
    }
}
