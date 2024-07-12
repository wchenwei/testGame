package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.CampPrisonerTemplate;
import com.hm.config.excel.templaextra.CampPrisonerResearcherTemplateImpl;
import com.hm.config.excel.templaextra.CampPrisonerTemplateImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: 战俘营
 * @author: chenwei
 * @create: 2020-07-10 00:01
 **/
@Config
public class CaptiveConfig extends ExcleConfig {

    private Map<Integer, CampPrisonerTemplateImpl> prisonerMap = Maps.newConcurrentMap();
    private Map<Integer, CampPrisonerResearcherTemplateImpl> researcherMap = Maps.newConcurrentMap();

    private int defResearcher; //默认研究员
    private int maxPrisoner; //最大等级战俘营

    @Override
    public void loadConfig() {
       loadPrisoner();
       loadResearcher();
    }

    private void loadResearcher() {
        ArrayList<CampPrisonerResearcherTemplateImpl> campPrisonerTemplates = JSONUtil.fromJson(getJson(CampPrisonerResearcherTemplateImpl.class), new TypeReference<ArrayList<CampPrisonerResearcherTemplateImpl>>() {});
        campPrisonerTemplates.forEach(e -> {
            e.init();
            if (e.getLast_time() == -1){
                this.defResearcher = e.getId();
            }
        });
        Map<Integer, CampPrisonerResearcherTemplateImpl> tempMap = campPrisonerTemplates.stream().collect(Collectors.toMap(CampPrisonerResearcherTemplateImpl::getId, Function.identity()));
        researcherMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadPrisoner(){
        ArrayList<CampPrisonerTemplateImpl> campPrisonerTemplates = JSONUtil.fromJson(getJson(CampPrisonerTemplateImpl.class), new TypeReference<ArrayList<CampPrisonerTemplateImpl>>() {});
        campPrisonerTemplates.forEach(e -> e.init());
        Map<Integer, CampPrisonerTemplateImpl> tempMap = campPrisonerTemplates.stream().collect(Collectors.toMap(CampPrisonerTemplate::getId, Function.identity()));
        prisonerMap = ImmutableMap.copyOf(tempMap);
        this.maxPrisoner = tempMap.keySet().stream().max(Comparator.comparing(Function.identity())).get();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(CampPrisonerTemplateImpl.class, CampPrisonerResearcherTemplateImpl.class);
    }

    public CampPrisonerResearcherTemplateImpl getCampPrisonerResearcherTemplate(int id){
        return this.researcherMap.get(id);
    }

    public CampPrisonerTemplateImpl getCampPrisonerTemplate(int id){
        return this.prisonerMap.get(id);
    }

    public int getDefResearcher() {
        return defResearcher;
    }

    public int getMaxPrisoner() {
        return maxPrisoner;
    }
}
