package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.*;
import com.hm.config.excel.templaextra.ActiveArmyRaceProgressTemplateImpl;
import com.hm.config.excel.templaextra.ActiveArmyRaceRechargeTemplateImpl;
import com.hm.config.excel.templaextra.ActiveArmyRaceRewardTemplateImpl;
import com.hm.config.excel.templaextra.ActiveArmyRaceTicketTemplateImpl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
/**
 * @description: 军备竞赛
 * @author: chenwei
 * @create: 2020-03-03 10:55
 **/

@Config
public class ActiveArmyRaceConfig extends ExcleConfig{
    // 订单信息（类型、星级、奖励等）
    private Map<Integer,ActiveArmyRaceTemplate> armyRaceMap = Maps.newConcurrentMap();
    // 订单信息（类型、星级、名称等）
    private Map<Integer,ActiveArmyRaceShowTemplate> armyRaceShowMap = Maps.newConcurrentMap();
    // 充值返利
    private Map<Integer, ActiveArmyRaceRechargeTemplateImpl> armyRaceRechargeMap = Maps.newConcurrentMap();
    // 根据玩家等级区分的订单奖励
    private Map<Integer, ActiveArmyRaceRewardTemplateImpl> armyRaceRewardMap = Maps.newConcurrentMap();
    // 入场券对应订单类型、星级的随机权重
    private Map<Integer, ActiveArmyRaceTicketTemplateImpl> armyRaceTicketMap = Maps.newConcurrentMap();
    // 军力奖励
    private Map<Integer, ActiveArmyRaceProgressTemplateImpl> armyRaceProgressMap = Maps.newConcurrentMap();
    // 充值金额-返利
    private ArrayListMultimap<Integer,ActiveArmyRaceRechargeTemplateImpl> goldRechargeListMap = ArrayListMultimap.create();
    // 订单奖励
    private ArrayListMultimap<Integer,ActiveArmyRaceRewardTemplateImpl> rewardListMap = ArrayListMultimap.create();
    // 类型-星级-订单
    private Table<Integer,Integer,ActiveArmyRaceTemplate> typeStarArmyRaceTable = HashBasedTable.create();


    @Override
    public void loadConfig() {
//       loadArmyRaceConfig();
//       loadArmyRaceRechargeConfig();
//       loadArmyRaceRewardConfig();
//       loadArmyRaceTicketConfig();
//       loadArmyRaceProgressConfig();
//       loadArmyRaceShowConfig();
    }

    private void loadArmyRaceShowConfig() {
        List<ActiveArmyRaceShowTemplate> list = JSONUtil.fromJson(getJson(ActiveArmyRaceShowTemplate.class), new TypeReference<List<ActiveArmyRaceShowTemplate>>() {});
        Map<Integer, ActiveArmyRaceShowTemplate> tempMap = list.stream().collect(Collectors.toMap(ActiveArmyRaceShowTemplate::getId, Function.identity()));
        armyRaceShowMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadArmyRaceProgressConfig() {
        List<ActiveArmyRaceProgressTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveArmyRaceProgressTemplateImpl.class), new TypeReference<List<ActiveArmyRaceProgressTemplateImpl>>() {});
        list.forEach(e -> e.init());
        Map<Integer, ActiveArmyRaceProgressTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(ActiveArmyRaceProgressTemplateImpl::getId, Function.identity()));
        armyRaceProgressMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadArmyRaceTicketConfig() {
        List<ActiveArmyRaceTicketTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveArmyRaceTicketTemplateImpl.class), new TypeReference<List<ActiveArmyRaceTicketTemplateImpl>>() {});
        list.forEach(e -> e.init());
        Map<Integer, ActiveArmyRaceTicketTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(ActiveArmyRaceTicketTemplate::getId, Function.identity()));
        armyRaceTicketMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadArmyRaceRewardConfig() {
        ArrayListMultimap<Integer,ActiveArmyRaceRewardTemplateImpl> tempListMap = ArrayListMultimap.create();
        List<ActiveArmyRaceRewardTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveArmyRaceRewardTemplateImpl.class), new TypeReference<List<ActiveArmyRaceRewardTemplateImpl>>() {});
        list.forEach(e -> {
            e.init();
            tempListMap.put(e.getReward_group(),e);
        });
        Map<Integer, ActiveArmyRaceRewardTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(ActiveArmyRaceRewardTemplateImpl::getId, Function.identity()));
        armyRaceRewardMap = ImmutableMap.copyOf(tempMap);
        rewardListMap = tempListMap;
    }

    private void loadArmyRaceRechargeConfig() {
        ArrayListMultimap<Integer,ActiveArmyRaceRechargeTemplateImpl> tempListMap = ArrayListMultimap.create();
        List<ActiveArmyRaceRechargeTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveArmyRaceRechargeTemplateImpl.class), new TypeReference<List<ActiveArmyRaceRechargeTemplateImpl>>() {});
        list.forEach(e -> {
            e.init();
            tempListMap.put(e.getRecharge_gold(), e);
        });
        Map<Integer, ActiveArmyRaceRechargeTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(ActiveArmyRaceRechargeTemplateImpl::getId, Function.identity()));
        armyRaceRechargeMap = ImmutableMap.copyOf(tempMap);
        goldRechargeListMap = tempListMap;
    }

    private void loadArmyRaceConfig() {
        Table<Integer,Integer,ActiveArmyRaceTemplate> tempTable = HashBasedTable.create();
        List<ActiveArmyRaceTemplate> list = JSONUtil.fromJson(getJson(ActiveArmyRaceTemplate.class), new TypeReference<List<ActiveArmyRaceTemplate>>() {});
        Map<Integer, ActiveArmyRaceTemplate> tempMap = list.stream().collect(Collectors.toMap(ActiveArmyRaceTemplate::getReward_group, Function.identity()));
        list.forEach(e -> tempTable.put(e.getType(), e.getStar(), e));
        armyRaceMap = ImmutableMap.copyOf(tempMap);
        typeStarArmyRaceTable = ImmutableTable.copyOf(tempTable);
    }


    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveArmyRaceTemplate.class,
                ActiveArmyRaceRechargeTemplate.class,
                ActiveArmyRaceRewardTemplate.class,
                ActiveArmyRaceTicketTemplate.class,
                ActiveArmyRaceProgressTemplate.class,
                ActiveArmyRaceShowTemplate.class);
    }

    public ActiveArmyRaceTemplate getArmyRaceTemplate(int id){
        return armyRaceMap.get(id);
    }

    public ActiveArmyRaceRechargeTemplateImpl getArmyRaceRechargeTemplate(int id){
        return armyRaceRechargeMap.get(id);
    }

    public ActiveArmyRaceProgressTemplateImpl getActiveArmyRaceProgress(int id){
        return armyRaceProgressMap.get(id);
    }

    public ActiveArmyRaceShowTemplate getArmyRaceShowTemplate(int id){
        return armyRaceShowMap.get(id);
    }

    public ActiveArmyRaceRechargeTemplateImpl getArmyRaceRechargeTemplate(int rechargeGlod,int playerLv){
        return goldRechargeListMap.get(rechargeGlod).stream().filter(e -> e.isFit(playerLv)).findFirst().orElse(null);
    }

    public ActiveArmyRaceRewardTemplateImpl getArmyRaceRewardTemplate(int id){
        return armyRaceRewardMap.get(id);
    }

    public ActiveArmyRaceTicketTemplateImpl getArmyRaceTicketTemplate(int id){
        return armyRaceTicketMap.get(id);
    }

    public ActiveArmyRaceRewardTemplateImpl getArmyRaceRewardTemplate(int rewardGroupId,int playerLv, int stage){
        return rewardListMap.get(rewardGroupId).stream().filter(e -> e.isFit(playerLv, stage)).findFirst().orElse(null);
    }

    public ActiveArmyRaceTemplate getArmyRaceTemplate(int type,int star){
        return typeStarArmyRaceTable.get(type, star);
    }
}
