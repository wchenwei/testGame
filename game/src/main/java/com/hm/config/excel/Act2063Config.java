package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 捕鱼
 */
@Config
public class Act2063Config extends ExcleConfig {
    private Map<Integer, ActiveFishFcTemplateImpl> fishTemplateMap = Maps.newHashMap();
    private Map<Integer, ActiveFishPondFcTemplateImpl> fishPondTemplateMap = Maps.newHashMap();
    private Map<Integer, Active2063GiftTemplateImpl> giftTemplateMap = Maps.newHashMap();
    private Map<Integer, Active2063IntegralboxTemplateImpl> boxMap = Maps.newHashMap();
    //配方
    private Map<Integer, Active2063FormulaTemplateImpl> formulaTemplate = Maps.newConcurrentMap();
    @Override
    public void loadConfig() {
//        loadFishTemplate();
//        loadGoFishPondTemplate();
//        loadGiftTemplate();
//        loadBoxMap();
//        loadFormulaTemplate();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveFishFcTemplateImpl.class,Active2063GiftTemplateImpl.class,
                ActiveFishPondFcTemplateImpl.class, Active2063IntegralboxTemplateImpl.class,Active2063FormulaTemplateImpl.class);
    }

    public ActiveFishPondFcTemplateImpl getFishPondById(int id){
        return fishPondTemplateMap.get(id);
    }

    public ActiveFishFcTemplateImpl getFishTemplateById(int id){
        return fishTemplateMap.get(id);
    }

    public Active2063GiftTemplateImpl getGiftTemplate(int giftId){
        return giftTemplateMap.get(giftId);
    }

    public Active2063IntegralboxTemplateImpl getIntegralBox(int id){
        return boxMap.get(id);
    }

    public Active2063FormulaTemplateImpl getFormula(int id) {
        return formulaTemplate.get(id);
    }

    private void loadFishTemplate(){
        List<ActiveFishFcTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveFishFcTemplateImpl.class), new TypeReference<List<ActiveFishFcTemplateImpl>>() {
        });
        Map<Integer, ActiveFishFcTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        fishTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadGoFishPondTemplate(){
        List<ActiveFishPondFcTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveFishPondFcTemplateImpl.class), new TypeReference<List<ActiveFishPondFcTemplateImpl>>() {
        });
        Map<Integer, ActiveFishPondFcTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        fishPondTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadGiftTemplate(){
        List<Active2063GiftTemplateImpl> list = JSONUtil.fromJson(getJson(Active2063GiftTemplateImpl.class), new TypeReference<List<Active2063GiftTemplateImpl>>() {
        });
        Map<Integer, Active2063GiftTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            map.put(e.getRecharge_gift_id(), e);
        });
        giftTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadBoxMap(){
        List<Active2063IntegralboxTemplateImpl> list = JSONUtil.fromJson(getJson(Active2063IntegralboxTemplateImpl.class), new TypeReference<List<Active2063IntegralboxTemplateImpl>>() {
        });
        Map<Integer, Active2063IntegralboxTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        boxMap = ImmutableMap.copyOf(map);
    }

    private void loadFormulaTemplate() {
        Map<Integer, Active2063FormulaTemplateImpl> tempFormulaTemplate = Maps.newConcurrentMap();
        for(Active2063FormulaTemplateImpl template :JSONUtil.fromJson(getJson(Active2063FormulaTemplateImpl.class),
                new TypeReference<ArrayList<Active2063FormulaTemplateImpl>>() {
                })) {
            template.init();
            tempFormulaTemplate.put(template.getId(), template);
        }
        this.formulaTemplate  = ImmutableMap.copyOf(tempFormulaTemplate);
    }
}
