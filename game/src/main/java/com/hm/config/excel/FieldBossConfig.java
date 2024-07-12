package com.hm.config.excel;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.FieldBossRewardTemplate;
import com.hm.config.excel.templaextra.FieldBossRewardTemplateImpl;
import com.hm.config.excel.templaextra.FieldBossTemplateImpl;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.annotation.ConfigLoadIndex;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Config
@ConfigLoadIndex(90)
public class FieldBossConfig extends ExcleConfig{

    private Map<Integer, FieldBossTemplateImpl> fieldBossMap = Maps.newHashMap();
    private Map<Integer, FieldBossRewardTemplateImpl> fieldBossRewardMap = Maps.newHashMap();
    private FieldBossRewardTemplateImpl firstFieldBossRewardTemplate;

    @Override
    public void loadConfig() {
        loadFieldBossConfig();
        loadFieldBossRewardConfig();
    }

    private void loadFieldBossRewardConfig() {
        List<FieldBossRewardTemplateImpl> templates = json2List(FieldBossRewardTemplateImpl.class);
        templates.stream().sorted(Comparator.comparing(FieldBossRewardTemplate::getId)).forEach(e -> e.init(templates));
        this.fieldBossRewardMap = templates.stream().collect((Collectors.toMap(FieldBossRewardTemplateImpl::getId, Function.identity())));
        this.firstFieldBossRewardTemplate = fieldBossRewardMap.values().stream().min(Comparator.comparing(FieldBossRewardTemplate::getId)).get();
    }

    private void loadFieldBossConfig() {
        List<FieldBossTemplateImpl> fieldBossTemplates = json2List(FieldBossTemplateImpl.class);
        Map<Integer, FieldBossTemplateImpl> fieldBossMap = Maps.newHashMap();
        fieldBossTemplates.forEach(e -> e.getOpenDays().forEach(day -> fieldBossMap.put(day, e)));
        this.fieldBossMap = ImmutableMap.copyOf(fieldBossMap);
    }

    public FieldBossTemplateImpl getFieldBossTemplate(int weekDay){
        return fieldBossMap.get(weekDay);
    }

    public FieldBossRewardTemplateImpl getFirstFieldBossRewardTemplate() {
        return firstFieldBossRewardTemplate;
    }

    public FieldBossRewardTemplateImpl getFieldBossRewardTemplate(int id){
        return fieldBossRewardMap.get(id);
    }
}
