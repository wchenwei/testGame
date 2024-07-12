package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active51MineTemplate;
import com.hm.config.excel.temlate.Active51RateFirstTemplate;
import com.hm.config.excel.temlate.Active51TreasureRateTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
/**
 * @description: 五一活动
 * @author: chenwei
 * @create: 2020-04-02 16:11
 **/
@Config
public class Activity51Config extends ExcleConfig {

    private int maxLv;
    // 地图
    private Map<Integer, Active51MineTemplate> mineTemplateMap = Maps.newConcurrentMap();
    private ArrayListMultimap<Integer, Active51MineTemplate> mineTemplateListMultimap = ArrayListMultimap.create();
    private Table<Integer,Integer,Active51MineTemplate> mineTemplateTable = HashBasedTable.create();
    // 砖块
    private Table<Integer, Integer, Active51BrickTemplateImpl> brickTemplateTable = HashBasedTable.create();
    // 计费点
    private ArrayListMultimap<Integer, Active51GiftTemplateImpl> giftTemplateMap = ArrayListMultimap.create();
    // 聚宝盆
    private List<Active51RateFirstTemplateImpl> firstTemplateList = Lists.newArrayList();
    private List<Active51TreasureRateTemplateImpl> treasureRateTemplateList= Lists.newArrayList();
    // 宝藏
    private List<Active51RewardTemplateImpl> rewardTemplateList = Lists.newArrayList();
    private ArrayListMultimap<Integer,Active51RewardTemplateImpl> rewardTemplateArrayListMultimap = ArrayListMultimap.create();
    // 期数
    private Map<Integer,Active51StageTemplateImpl> stageTemplateMap = Maps.newConcurrentMap();


    @Override
    public void loadConfig() {
//        loadActive51MineTemplate();
//        loadActive51BrickTemplate();
//        loadActive51GiftTemplate();
//        loadActive51RateFirstTemplate();
//        loadActive51TreasureRateTemplate();
//        loadActive51RewardTemplate();
//        loadActive51StageTemplate();
    }

    private void loadActive51StageTemplate() {
        List<Active51StageTemplateImpl> list = JSONUtil.fromJson(getJson(Active51StageTemplateImpl.class), new TypeReference<List<Active51StageTemplateImpl>>() {});
        list.forEach(e -> {e.init();});
        Map<Integer, Active51StageTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(Active51StageTemplateImpl::getId, Function.identity()));
        stageTemplateMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadActive51RewardTemplate() {
        ArrayListMultimap<Integer,Active51RewardTemplateImpl> tempArrayListMultimap = ArrayListMultimap.create();
        List<Active51RewardTemplateImpl> list = JSONUtil.fromJson(getJson(Active51RewardTemplateImpl.class), new TypeReference<List<Active51RewardTemplateImpl>>() {});
        list.forEach(e -> {
            e.init();
            tempArrayListMultimap.put(e.getBrick_id(), e);
        });
        rewardTemplateList = ImmutableList.copyOf(list);
        rewardTemplateArrayListMultimap = tempArrayListMultimap;
    }

    private void loadActive51MineTemplate() {
        int lv = 1;
        List<Active51MineTemplate> list = JSONUtil.fromJson(getJson(Active51MineTemplate.class), new TypeReference<List<Active51MineTemplate>>() {});
        ArrayListMultimap<Integer, Active51MineTemplate> tempListMultimap = ArrayListMultimap.create();
        list.forEach(e -> {
            tempListMultimap.put(e.getLevel(), e);
            maxLv = Math.max(lv, e.getLevel());
            mineTemplateTable.put(e.getLevel(),e.getColumn(),e);
        });
        Map<Integer, Active51MineTemplate> tempMap = list.stream().collect(Collectors.toMap(Active51MineTemplate::getId, Function.identity()));
        mineTemplateMap = ImmutableMap.copyOf(tempMap);
        mineTemplateListMultimap = tempListMultimap;
    }

    private void loadActive51BrickTemplate() {
        Table<Integer, Integer, Active51BrickTemplateImpl> tempBrickTemplateTable = HashBasedTable.create();
        List<Active51BrickTemplateImpl> list = JSONUtil.fromJson(getJson(Active51BrickTemplateImpl.class), new TypeReference<List<Active51BrickTemplateImpl>>() {});
        list.forEach(e -> {
            e.init();
            tempBrickTemplateTable.put(e.getStage(),e.getId_sub(), e);
        });
        brickTemplateTable = tempBrickTemplateTable;
    }

    private void loadActive51GiftTemplate() {
        ArrayListMultimap<Integer,Active51GiftTemplateImpl> tempMap = ArrayListMultimap.create();
        List<Active51GiftTemplateImpl> list = JSONUtil.fromJson(getJson(Active51GiftTemplateImpl.class), new TypeReference<List<Active51GiftTemplateImpl>>() {});
        list.forEach(e ->{
            tempMap.put(e.getStage(), e);
        });
        giftTemplateMap = tempMap;
    }

    private void loadActive51RateFirstTemplate() {
        List<Active51RateFirstTemplateImpl> list = JSONUtil.fromJson(getJson(Active51RateFirstTemplateImpl.class), new TypeReference<List<Active51RateFirstTemplateImpl>>() {});
        list.forEach(e -> e.init());
        firstTemplateList = ImmutableList.copyOf(list);
    }

    private void loadActive51TreasureRateTemplate() {
        List<Active51TreasureRateTemplateImpl> list = JSONUtil.fromJson(getJson(Active51TreasureRateTemplateImpl.class), new TypeReference<List<Active51TreasureRateTemplateImpl>>() {});
        list.forEach(e -> e.init());
        treasureRateTemplateList = ImmutableList.copyOf(list);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active51MineTemplate.class,
                Active51BrickTemplateImpl.class,
                Active51GiftTemplateImpl.class,
                Active51RateFirstTemplate.class,
                Active51TreasureRateTemplate.class,
                Active51RewardTemplateImpl.class,
                Active51StageTemplateImpl.class);
    }

    public Active51MineTemplate getActive51MineTemplate(int id){
        return mineTemplateMap.get(id);
    }

    /**
     * 获取某一层的地图
     * @param lv 层数
     * @return
     */
    public List<Active51MineTemplate> getActive51MineTemplateByLv(int lv){
        return mineTemplateListMultimap.get(lv);
    }

    public Active51BrickTemplateImpl getActive51Brick(int stage, int subId){
        return brickTemplateTable.get(stage,subId);
    }

    public Active51GiftTemplateImpl getActive51GiftTemplate(int stage, int playerLv, int giftId){
        List<Active51GiftTemplateImpl> active51GiftTemplates = giftTemplateMap.get(stage);
        if (CollUtil.isNotEmpty(treasureRateTemplateList)){
            return active51GiftTemplates.stream().filter(e -> e.isFit(playerLv)).filter(e -> e.getRecharge_gift_id() == giftId).findFirst().orElse(null);
        }
        return null;
    }

    public Active51MineTemplate Active51MineTemplate(int lv,int col){
        return mineTemplateTable.get(lv,col);
    }

    public Active51RewardTemplateImpl get51RewardTemplate(int playerLv){
        return rewardTemplateList.stream().filter(e -> e.isFit(playerLv)).findFirst().orElse(null);
    }


    public Active51MineTemplate getActive51MineTemplate(int lv,int col){
        return mineTemplateTable.get(lv, col);
    }

    public Active51BrickTemplateImpl getActive51BrickTemplate(int satge, int lv,int col){
        Active51MineTemplate template = getActive51MineTemplate(lv, col);
        if (template != null && template.getBrick_id() != 0){
            return getActive51Brick(satge, template.getBrick_id());
        }
        return null;
    }

    public Active51RewardTemplateImpl getActive51RewardTemplate(int brickId, int playerLv){
        List<Active51RewardTemplateImpl> templateList = rewardTemplateArrayListMultimap.get(brickId);
        if (CollUtil.isNotEmpty(templateList)){
            return templateList.stream().filter(e -> e.isFit(playerLv)).findFirst().orElse(null);
        }
        return null;
    }

    public int getMaxLv(){
        return maxLv;
    }

    /**
     * 获取赠送比例(首轮)
     *
     * @param value
     * @return
     */
    public double getTreasureRateFirst(int value) {
        Active51RateFirstTemplateImpl cfg = firstTemplateList.stream().
                filter(t -> t.getRecharge_down() <= value && value <= t.getRecharge_up()).
                findFirst().orElse(null);
        if (cfg == null) {
            return 0;
        }
        double v;
        if (cfg.getRate_down().equals(cfg.getRate_up())) {
            v = cfg.getRate_down();
        } else {
            v = RandomUtils.randomDouble(cfg.getRate_down(), cfg.getRate_up());
        }
        return v * cfg.getWeightMeta().random();
    }

    /**
     * 获取赠送比例
     *
     * @param value
     * @return
     */
    public double getTreasureRate(int value) {
        Active51TreasureRateTemplateImpl cfg = treasureRateTemplateList.stream().
                filter(t -> t.getRecharge_down() <= value && value <= t.getRecharge_up()).
                findFirst().orElse(null);
        if (cfg == null) {
            return 0;
        }
        double v;
        // 上下限一样的
        if (cfg.getRate_down().equals(cfg.getRate_up())) {
            v = cfg.getRate_down();
        } else {
            v = RandomUtils.randomDouble(cfg.getRate_down(), cfg.getRate_up());
        }
        return v * cfg.getWeightMeta().random();
    }

    public Active51StageTemplateImpl getActive51StageTemplate(int stage){
        return stageTemplateMap.get(stage);
    }

}
