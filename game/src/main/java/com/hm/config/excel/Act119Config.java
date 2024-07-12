package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.temlate.Active119GiftTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import lombok.Getter;

import java.util.List;
import java.util.Map;
@Config
public class Act119Config extends ExcleConfig {
    // 单笔充值领取奖励
    private Table<Integer, Integer, Active119BoxrewardTemplateImpl> boxRewardTemplateTable =  HashBasedTable.create();
    private ListMultimap<Integer, Active119RewardTemplateImpl> rewardTemplateMap = ArrayListMultimap.create();
    //private Map<Integer, Active119RewardTemplateImpl> rewardSpecialTemplateMap = Maps.newConcurrentMap();

    private Map<Integer, Active119GiftTemplateImpl> giftTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, Active119TaskRewardTemplateImpl> taskTemplateMap = Maps.newConcurrentMap();
    // 随机附加奖励
    private Map<Integer, Active119StageTemplateImpl> stageMap = Maps.newConcurrentMap();
    private Map<Integer, Active119RewardLibTemplateImpl> rewardLibMap = Maps.newConcurrentMap();
    // 每轮最多特殊奖励次数
    @Getter
    private int maxRound;


    public Active119GiftTemplate getGiftTemplate(int giftId){
        return giftTemplateMap.getOrDefault(giftId, null);
    }

    public Active119StageTemplateImpl getStageTemplateImpl(int stage){
        return stageMap.getOrDefault(stage, null);
    }

    public Active119TaskRewardTemplateImpl getTaskReward(int id){
        return taskTemplateMap.getOrDefault(id, null);
    }

    public Active119RewardLibTemplateImpl getRewardLib(int id){
        return rewardLibMap.getOrDefault(id, null);
    }


    public Active119BoxrewardTemplateImpl getBoxReward(int stage, int round){
        return boxRewardTemplateTable.get(stage, round);
    }


    public boolean isNextRound(int stage, List<Integer> drawList){
        return drawList.size() >= this.getRoundMaxNum(stage);
    }

    public int getRoundMaxNum(int stage){
       return (int) this.rewardTemplateMap.get(stage).stream()
                .filter(Active119RewardTemplateImpl::isComm)
                .count();
    }

    public int getMaxStage(){
        return CollUtil.max(stageMap.keySet());
    }

    @Override
    public void loadConfig() {
//        loadBoxreward();
//        loadRewardTemplateMap();
//        loadRewardLibMap();
//        loadGiftTemplate();
//        loadTaskRewardTemplate();
//        loadStageTemplate();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active119BoxrewardTemplateImpl.class, Active119RewardTemplateImpl.class,Active119StageTemplateImpl.class,
                Active119RewardLibTemplateImpl.class, Active119GiftTemplateImpl.class,Active119TaskRewardTemplateImpl.class);
    }

    private void loadBoxreward(){
        List<Active119BoxrewardTemplateImpl> list = JSONUtil.fromJson(getJson(Active119BoxrewardTemplateImpl.class), new TypeReference<List<Active119BoxrewardTemplateImpl>>() {
        });
        Table<Integer, Integer, Active119BoxrewardTemplateImpl> table =  HashBasedTable.create();
        list.forEach(e -> {
            e.init();
            table.put(e.getStage(), e.getRound(), e);
        });
        maxRound = list.stream().mapToInt(Active119BoxrewardTemplateImpl::getRound).max().orElse(0);
        boxRewardTemplateTable = ImmutableTable.copyOf(table);
    }

    private void loadRewardTemplateMap(){
        List<Active119RewardTemplateImpl> list = JSONUtil.fromJson(getJson(Active119RewardTemplateImpl.class), new TypeReference<List<Active119RewardTemplateImpl>>() {
        });
        ListMultimap<Integer, Active119RewardTemplateImpl> map = ArrayListMultimap.create();
        list.forEach(e -> {
            e.init();
            map.put(e.getStage(), e);
        });
        this.rewardTemplateMap = map;
    }

    private void loadRewardLibMap(){
        List<Active119RewardLibTemplateImpl> list = JSONUtil.fromJson(getJson(Active119RewardLibTemplateImpl.class), new TypeReference<List<Active119RewardLibTemplateImpl>>() {
        });
        Map<Integer, Active119RewardLibTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getReward_type(), e);
        });
        rewardLibMap = ImmutableMap.copyOf(map);
    }

    private void loadGiftTemplate(){
        List<Active119GiftTemplateImpl> list = JSONUtil.fromJson(getJson(Active119GiftTemplateImpl.class), new TypeReference<List<Active119GiftTemplateImpl>>() {
        });
        Map<Integer, Active119GiftTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            map.put(e.getRecharge_gift_id(), e);
        });
        giftTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadTaskRewardTemplate(){
        List<Active119TaskRewardTemplateImpl> list = JSONUtil.fromJson(getJson(Active119TaskRewardTemplateImpl.class), new TypeReference<List<Active119TaskRewardTemplateImpl>>() {
        });
        Map<Integer, Active119TaskRewardTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        taskTemplateMap = ImmutableMap.copyOf(map);
    }

    private void loadStageTemplate(){
        List<Active119StageTemplateImpl> list = JSONUtil.fromJson(getJson(Active119StageTemplateImpl.class), new TypeReference<List<Active119StageTemplateImpl>>() {
        });
        Map<Integer, Active119StageTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        stageMap = ImmutableMap.copyOf(map);
    }
}
