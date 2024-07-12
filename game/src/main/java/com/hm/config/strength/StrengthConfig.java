package com.hm.config.strength;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.strength.excel.*;
import com.hm.enums.StrengthGridType;
import com.hm.model.strength.StrengthStore;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
@Config
public class StrengthConfig extends ExcleConfig {
    //属性普通类型
    private static final int COMM_TYPE = 0;
    //属性稀有类型
    private static final int RARE_TYPE = 1;
    public static final int RARE_Star = 5;
    // 力量 总图
    private Map<Integer, BlockMapTemplateImpl> blockMapTemplateMap = Maps.newConcurrentMap();
    // 配件模型
    private Map<Integer, BlockModelTemplateImpl> modelTemplateMap = Maps.newConcurrentMap();
    // 配件 升华
    private Map<Integer, BlockSublimeTemplateImpl> sublimeTemplateMap = Maps.newConcurrentMap();
    // 升级 lv -- 格子数 -- 需要的经验
    private Table<Integer, Integer, Integer> lvUpTable = HashBasedTable.create();
    // 星级 -- 属性类型 --
    private Table<Integer, Integer, List<BlockAttrTemplate>> attrTemplateTable = HashBasedTable.create();
    private Map<Object, BlockAttrTemplate> attrTemplateMap = Maps.newConcurrentMap();

    // 配件数据
    private Map<Integer, BlockPartsTemplateImpl> partsTemplateMap = Maps.newConcurrentMap();
    // 配件随机抽取权重
    private WeightMeta<BlockPartsTemplateImpl> weightMeta;
    // 配件稀有抽取权重
    private WeightMeta<BlockPartsTemplateImpl> rareWeightMeta;

    public BlockSublimeTemplateImpl getBlockSublimeTemplateImpl(int id){
        return sublimeTemplateMap.get(id);
    }

    public BlockModelTemplateImpl getBlockModelTemplateImpl(int id){
        return modelTemplateMap.get(id);
    }

    public int maxLvUpCost(StrengthStore store, BlockPartsTemplate blockPartsTemplate){
        int costExp = 0;
        for(int i = store.getLv(); i < blockPartsTemplate.getLevel_limit(); i++){
            costExp += lvUpTable.get(i, blockPartsTemplate.getGird());
        }
        return costExp;
    }

    public int[] calLv(StrengthStore store, BlockPartsTemplate partsTemplate, int exp){
        int lv = store.getLv();
        for(int i = store.getLv(); i < partsTemplate.getLevel_limit(); i++){
            int needExp = lvUpTable.get(i, partsTemplate.getGird());
            if(exp < needExp){
                break;
            }
            lv ++;
            exp -= needExp;
        }
        return new int[]{lv, exp};
    }


    public BlockPartsTemplateImpl getBlockPartsTemplate(int id){
        return partsTemplateMap.get(id);
    }

    public BlockMapTemplateImpl getBlockMapTemplateByType(int type){
        return blockMapTemplateMap.get(type);
    }

    public BlockAttrTemplate getBlockAttrTemplate(int id){
        return attrTemplateMap.get(id);
    }

    public List<Integer> getRandomAttr(BlockPartsTemplateImpl blockPartsTemplate){
        Integer star = blockPartsTemplate.getStar();
        List<BlockAttrTemplate> list = this.getRandomAttr(attrTemplateTable.get(star, COMM_TYPE), blockPartsTemplate.getBase_attr_num());
        list.addAll(this.getRandomAttr(attrTemplateTable.get(star, RARE_TYPE), blockPartsTemplate.getPercentage_attr_num()));
        return list.stream().map(BlockAttrTemplate::getIndex).collect(Collectors.toList());
    }

    /**
     * @param luck 是否满足幸运值
     * @return
     */
    public BlockPartsTemplateImpl getRandomBlockParts(boolean luck){
        if(luck){
            return rareWeightMeta.random();
        }
        return weightMeta.random();
    }


    @Override
    public void loadConfig() {
//        loadMap();
//        loadModelMap();
//        loadSublimeMap();
//        loadLvUp();
//        loadAttr();
//        loadParts();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(BlockMapTemplateImpl.class, BlockModelTemplateImpl.class, BlockSublimeTemplateImpl.class,
                BlockLevelupTemplate.class, BlockAttrTemplate.class, BlockPartsTemplateImpl.class);
    }

    private void loadParts(){
        List<BlockPartsTemplateImpl> list = JSONUtil.fromJson(getJson(BlockPartsTemplateImpl.class), new TypeReference<List<BlockPartsTemplateImpl>>() {
        });
        Map<Integer, BlockPartsTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            map.put(e.getId(), e);
        });
        Map<BlockPartsTemplateImpl, Integer> collect = list.stream().collect(Collectors.toMap(Function.identity(), BlockPartsTemplate::getGacha_rate));
        this.weightMeta = RandomUtils.buildWeightMeta(collect);
        Map<BlockPartsTemplateImpl, Integer> rareList = list.stream().filter(e -> e.getStar() >= RARE_Star).collect(Collectors.toMap(Function.identity(), BlockPartsTemplate::getGacha_rate));
        this.rareWeightMeta =  RandomUtils.buildWeightMeta(rareList);
        partsTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadAttr(){
        List<BlockAttrTemplate> list = JSONUtil.fromJson(getJson(BlockAttrTemplate.class), new TypeReference<List<BlockAttrTemplate>>() {
        });
        Table<Integer, Integer, List<BlockAttrTemplate>> table = HashBasedTable.create();
        Map<Object, BlockAttrTemplate> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            List<BlockAttrTemplate> templateList = table.get(e.getStar(), e.getAttr_type());
            if(templateList == null){
                templateList = Lists.newArrayList();
            }
            templateList.add(e);
            table.put(e.getStar(), e.getAttr_type(), templateList);
            map.put(e.getIndex(), e);
        });
        attrTemplateTable = ImmutableTable.copyOf(table);
        attrTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadLvUp(){
        Table<Integer, Integer, Integer> table = HashBasedTable.create();
        List<BlockLevelupTemplate> list = JSONUtil.fromJson(getJson(BlockLevelupTemplate.class), new TypeReference<List<BlockLevelupTemplate>>() {
        });
        list.forEach(e -> {
            table.put(e.getLevel(), StrengthGridType.two.getType(), e.getExp_grid2());
            table.put(e.getLevel(), StrengthGridType.three.getType(), e.getExp_grid3());
            table.put(e.getLevel(), StrengthGridType.four.getType(), e.getExp_grid4());
            table.put(e.getLevel(), StrengthGridType.five.getType(), e.getExp_grid5());
        });
        lvUpTable = ImmutableTable.copyOf(table);
    }

    private void loadSublimeMap(){
        List<BlockSublimeTemplateImpl> list = JSONUtil.fromJson(getJson(BlockSublimeTemplateImpl.class), new TypeReference<List<BlockSublimeTemplateImpl>>() {
        });
        Map<Integer, BlockSublimeTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getLevel(), e);
        });
        sublimeTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadMap(){
        List<BlockMapTemplateImpl> list = JSONUtil.fromJson(getJson(BlockMapTemplateImpl.class), new TypeReference<List<BlockMapTemplateImpl>>() {
        });
        Map<Integer, BlockMapTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getTank_type(), e);
        });
        blockMapTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadModelMap(){
        List<BlockModelTemplateImpl> list = JSONUtil.fromJson(getJson(BlockModelTemplateImpl.class), new TypeReference<List<BlockModelTemplateImpl>>() {
        });
        Map<Integer, BlockModelTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        modelTemplateMap = ImmutableMap.copyOf(map);
    }

    private List<BlockAttrTemplate> getRandomAttr(List<BlockAttrTemplate> blockAttrTemplateList, int count){
        ArrayList<BlockAttrTemplate> list = Lists.newArrayList(blockAttrTemplateList);
        Collections.shuffle(list);
        int min = Math.min(list.size(), count);
        return list.subList(0, min);
    }
}
