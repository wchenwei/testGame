package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.CentralControlExpTemplate;
import com.hm.config.excel.temlate.CentralControlSkillFormulaTemplate;
import com.hm.config.excel.temlate.CentralControlSkillLevelTemplate;
import com.hm.config.excel.templaextra.CentralControlLabExtraTemplate;
import com.hm.config.excel.templaextra.ItemElementExtraTemplate;
import com.hm.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
@Config
public class ControlConfig extends ExcleConfig {
    private Map<Integer, ItemElementExtraTemplate> elementMap = Maps.newConcurrentMap();
    private Map<Integer, CentralControlSkillFormulaTemplate> skillMap = Maps.newConcurrentMap();
    private List<CentralControlExpTemplate> bigSkillLvs = Lists.newArrayList();
    private Map<Integer, CentralControlLabExtraTemplate> labs = Maps.newConcurrentMap();
    private Table<Integer, Integer, CentralControlSkillLevelTemplate> skillTable = HashBasedTable.create();

    @Override
    public void loadConfig() {
        loadElementConfig();
        loadElementSkillConfig();
        loadBigSkillLvConfig();
        loadLabConfig();
        loadSkillConfig();
    }

    private void loadSkillConfig() {
        Table<Integer, Integer, CentralControlSkillLevelTemplate> skillTable = HashBasedTable.create();
        List<CentralControlSkillLevelTemplate> list = JSONUtil.fromJson(getJson(CentralControlSkillLevelTemplate.class), new TypeReference<ArrayList<CentralControlSkillLevelTemplate>>() {
        });
        for (CentralControlSkillLevelTemplate template : list) {
            skillTable.put(template.getType(), template.getLevel(), template);
        }
        this.skillTable = ImmutableTable.copyOf(skillTable);
    }

    private void loadLabConfig() {
        List<CentralControlLabExtraTemplate> list = JSONUtil.fromJson(getJson(CentralControlLabExtraTemplate.class), new TypeReference<ArrayList<CentralControlLabExtraTemplate>>(){});
        list.forEach(t ->t.init());
        this.labs = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(CentralControlLabExtraTemplate::getId,Function.identity())));
    }

    private void loadBigSkillLvConfig() {
        List<CentralControlExpTemplate> list = JSONUtil.fromJson(getJson(CentralControlExpTemplate.class), new TypeReference<ArrayList<CentralControlExpTemplate>>(){});
        this.bigSkillLvs = ImmutableList.copyOf(list);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ItemElementExtraTemplate.class, CentralControlSkillFormulaTemplate.class, CentralControlExpTemplate.class, CentralControlLabExtraTemplate.class, CentralControlSkillLevelTemplate.class);
    }

    public void loadElementConfig(){
        Map<Integer, ItemElementExtraTemplate> elementMap = Maps.newConcurrentMap();
        for(ItemElementExtraTemplate template: JSONUtil.fromJson(getJson(ItemElementExtraTemplate.class), new TypeReference<ArrayList<ItemElementExtraTemplate>>(){})){
            template.init();
            elementMap.put(template.getId(), template);
        }
        this.elementMap = ImmutableMap.copyOf(elementMap);
    }

    public void loadElementSkillConfig(){
        Map<Integer, CentralControlSkillFormulaTemplate> skillMap = Maps.newConcurrentMap();
        for(CentralControlSkillFormulaTemplate template: JSONUtil.fromJson(getJson(CentralControlSkillFormulaTemplate.class), new TypeReference<ArrayList<CentralControlSkillFormulaTemplate>>(){})){
            skillMap.put(template.getId(), template);
        }
        this.skillMap = ImmutableMap.copyOf(skillMap);
    }

    public ItemElementExtraTemplate getElement(int id){
        return this.elementMap.get(id);
    }

    public int getSkillByColors(String colors){
        CentralControlSkillFormulaTemplate template = skillMap.values().stream().filter(t->t.getType()==1&& StringUtil.equals(t.getFormula_position(),colors)).findFirst().orElse(null);
        return template==null?0:template.getSkill_id();
    }

    public int getBigSkillByColors(String colors){
        CentralControlSkillFormulaTemplate template = skillMap.values().stream().filter(t->t.getType()==2&& StringUtil.equals(t.getFormula_no_position(),colors)).findFirst().orElse(null);
        return template==null?0:template.getSkill_type();
    }

    public int getBigSkillLv(long exp){
        for(int i=bigSkillLvs.size()-1;i>=0;i--){
            CentralControlExpTemplate template = bigSkillLvs.get(i);
            if(exp>=template.getElement_exp_total()){
                return template.getId();
            }
        }
        return 0;
    }
    //获取初始化库
    public CentralControlLabExtraTemplate getLabInit(){
        return this.labs.values().stream().filter(t ->t.getLibrary_level()==1).findFirst().orElse(null);
    }

    public CentralControlLabExtraTemplate getLab(int id){
        return this.labs.get(id);
    }


    public int getBigSkillId(int type, int lv) {
        if (skillTable.contains(type, lv)) {
            return skillTable.get(type, lv).getSkill_id();
        }
        return 0;
    }
}
