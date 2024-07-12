package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveFishPondTemplateImpl;
import com.hm.config.excel.templaextra.ActiveFishTemplateImpl;
import com.hm.config.excel.templaextra.ActiveGofishLevelTemplateImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 捕鱼
 */
@Config
public class FishConfig extends ExcleConfig {
    private Map<Integer, ActiveFishTemplateImpl> fishTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, ActiveGofishLevelTemplateImpl> fishLevelTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, ActiveFishPondTemplateImpl> fishPondTemplateMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
//        loadFishTemplate();
//        loadGofishLevelTemplate();
//        loadGoFishPondTemplate();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveFishTemplateImpl.class,ActiveGofishLevelTemplateImpl.class,
                ActiveFishPondTemplateImpl.class);
    }


    public ActiveGofishLevelTemplateImpl getGofishLevelById(int level){
        return fishLevelTemplateMap.get(level);
    }

    public ActiveFishPondTemplateImpl getFishPondById(int id){
        return fishPondTemplateMap.get(id);
    }

    public ActiveFishTemplateImpl getFishTemplateById(int id){
        return fishTemplateMap.get(id);
    }

    public int getGoFishLevel(int totalExp){
        ActiveGofishLevelTemplateImpl template = fishLevelTemplateMap.values().stream().sorted(Comparator.comparing(ActiveGofishLevelTemplateImpl::getExp_total).reversed())
                .filter(e -> e.getExp_total() <= totalExp).findFirst().orElse(null);
        if(template == null){
            return 0;
        }
        return template.getLevel();
    }



    private void loadFishTemplate(){
        List<ActiveFishTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveFishTemplateImpl.class), new TypeReference<List<ActiveFishTemplateImpl>>() {
        });
        Map<Integer, ActiveFishTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        fishTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadGofishLevelTemplate(){
        List<ActiveGofishLevelTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveGofishLevelTemplateImpl.class), new TypeReference<List<ActiveGofishLevelTemplateImpl>>() {
        });
        Map<Integer, ActiveGofishLevelTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getLevel(), e);
        });
        fishLevelTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadGoFishPondTemplate(){
        List<ActiveFishPondTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveFishPondTemplateImpl.class), new TypeReference<List<ActiveFishPondTemplateImpl>>() {
        });
        Map<Integer, ActiveFishPondTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        fishPondTemplateMap = ImmutableMap.copyOf(map);
    }
}
