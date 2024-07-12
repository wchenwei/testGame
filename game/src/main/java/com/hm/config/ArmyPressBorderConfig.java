package com.hm.config;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.Mission3matchArmyExtraTemplate;
import com.hm.config.excel.templaextra.Mission3matchBoxExtraTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
@Config
public class ArmyPressBorderConfig extends ExcleConfig {
    private Map<Integer, Mission3matchBoxExtraTemplate> boxs = Maps.newConcurrentMap();
    private Table<Integer, Integer, List<Mission3matchArmyExtraTemplate>> npcs = HashBasedTable.create();
    private Map<Integer, Mission3matchArmyExtraTemplate> npcMaps = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
        loadBoxConfig();
        loadNpcConfig();
    }

    private void loadBoxConfig() {
        List<Mission3matchBoxExtraTemplate> list = JSONUtil.fromJson(getJson(Mission3matchBoxExtraTemplate.class), new TypeReference<ArrayList<Mission3matchBoxExtraTemplate>>(){});
        list.forEach(t ->t.init());
        this.boxs = ImmutableMap.copyOf(list.stream().collect(Collectors.toMap(Mission3matchBoxExtraTemplate::getId,Function.identity())));
    }

    private void loadNpcConfig() {
        Table<Integer, Integer, List<Mission3matchArmyExtraTemplate>> npcs = HashBasedTable.create();
        Map<Integer, Mission3matchArmyExtraTemplate> npcMaps = Maps.newConcurrentMap();
        List<Mission3matchArmyExtraTemplate> list = JSONUtil.fromJson(getJson(Mission3matchArmyExtraTemplate.class), new TypeReference<ArrayList<Mission3matchArmyExtraTemplate>>() {
        });
        for (Mission3matchArmyExtraTemplate template : list) {
            template.init();
            List<Mission3matchArmyExtraTemplate> npcList = npcs.get(template.getType(), template.getLevel()) == null ? Lists.newArrayList() : npcs.get(template.getType(), template.getLevel());
            npcList.add(template);
            npcs.put(template.getType(),template.getLevel(),npcList);
            npcMaps.put(template.getId(),template);
        }
        this.npcs = ImmutableTable.copyOf(npcs);
        this.npcMaps = ImmutableMap.copyOf(npcMaps);
    }


    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Mission3matchBoxExtraTemplate.class, Mission3matchArmyExtraTemplate.class);
    }

    public int getRandomNpcId(int type,int lv){
        try{
            List<Mission3matchArmyExtraTemplate> list = npcs.get(type, lv);
            if(list==null){
                return 0;
            }
            return RandomUtil.randomEle(list).getId();
        }catch(Exception e){
            System.out.print(e.toString());
        }
        return 0;
    }

    public Mission3matchBoxExtraTemplate getBox(int id) {
        return this.boxs.get(id);
    }


    public Mission3matchArmyExtraTemplate getNpc(int npcId) {
        return this.npcMaps.get(npcId);
    }
}
