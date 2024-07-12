package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.temlate.ActiveCvoutMissionTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 *          航母出击
 * @date 2021/2/4 18:19
 */
@Config
public class Act106Config extends ExcleConfig {
    // 单笔充值领取奖励
    private Map<Integer, ActiveCvoutRechargeOnceTemplateImpl> rechargeOnceMap = Maps.newConcurrentMap();
    private Set<Integer> rechargePointSet = Sets.newHashSet();
    // 神秘商人计费点礼包  计费点 - entity
    private Map<Integer, ActiveCvoutGiftTemplateImpl> ActiveCvoutGiftMap = Maps.newConcurrentMap();
    //轮盘 抽奖信息
    private Map<Integer, ActiveCvoutCircleTemplateImpl> activeCvoutCircleMap = Maps.newConcurrentMap();
    // 格子信息
    private Map<Integer, List<ActiveCvoutTemplateImpl>> activeCvoutMap = Maps.newConcurrentMap();
    private Map<Integer, ActiveCvoutTemplateImpl> cvoutMap = Maps.newConcurrentMap();
    // 藏宝图碎片
    private Map<Integer, ActiveCvoutMapTemplateImpl> activeTreasureMap = Maps.newConcurrentMap();
    // 总藏宝图合成信息
    private Map<Integer, ActiveCvoutStageTemplateImpl> activeCvoutStageMap = Maps.newConcurrentMap();
    // 首领boss 信息 id - entity
    private Map<Integer, ActiveCvoutBossTemplateImpl> activeCvoutBossMap = Maps.newConcurrentMap();

    private Map<Integer, ActiveCvoutMissionTemplate> activeCvoutMissionMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
//        loadActiveCvoutRechargeOnce();
//        loadActiveCvoutCircle();
//        loadActiveCvout();
//        loadActiveCvoutGift();
//        loadActiveTreasureMap();
//        loadActiveCvoutStageMap();
//        loadActiveCvoutBossMap();
//        loadActiveCvoutMissionMap();
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveCvoutRechargeOnceTemplateImpl.class,ActiveCvoutGiftTemplateImpl.class,ActiveCvoutStageTemplateImpl.class,
                ActiveCvoutCircleTemplateImpl.class, ActiveCvoutTemplateImpl.class, ActiveCvoutMapTemplateImpl.class,
                ActiveCvoutBossTemplateImpl.class, ActiveCvoutMissionTemplate.class);
    }

    public long getComBat(int playerLv){
        ActiveCvoutMissionTemplate template = activeCvoutMissionMap.get(playerLv);
        return template == null? 0: template.getPower();
    }

    public int getMapMaxId(int stage){
        List<ActiveCvoutTemplateImpl> activeCvoutTemplates = activeCvoutMap.get(stage);
        return activeCvoutTemplates.stream().map(ActiveCvoutTemplateImpl::getMap_id).max(Integer::compareTo).orElse(5);
    }

    public ActiveCvoutStageTemplateImpl getRankBase(int stage){
        ActiveCvoutStageTemplateImpl activeCvoutStageTemplate = activeCvoutStageMap.get(stage);
        return activeCvoutStageTemplate;
    }

    /**
     * @description
     *          根据id 、期数 获取格子信息
     * @param stage
     * @param id
     * @return com.hm.config.excel.templaextra.ActiveCvoutTemplateImpl
     * @date 2021/2/5 12:47
     */
    public ActiveCvoutTemplateImpl getActiveCvoutTemplate(int stage, int mapId, int id) {
        ActiveCvoutTemplateImpl activeCvoutTemplate = activeCvoutMap.get(stage).stream().filter(e -> id == e.getLocation() && e.getStage() ==stage && e.getMap_id() ==mapId).findFirst().orElse(null);
        return activeCvoutTemplate;
    }

    public ActiveCvoutTemplateImpl getActiveCvoutTemplateById(int id) {
        return cvoutMap.getOrDefault(id, null);
    }

    /**
     * @description
     *          藏宝图 奖励
     * @param id
     * @return com.hm.config.excel.templaextra.ActiveCvoutMapTemplateImpl
     * @date 2021/2/5 16:30
     */
    public ActiveCvoutMapTemplateImpl getActiveCvoutMapTemplate(int id) {
        ActiveCvoutMapTemplateImpl template = activeTreasureMap.getOrDefault(id, null);
        return template;
    }

    public ActiveCvoutStageTemplateImpl getActiveCvoutStage(int stage) {
        ActiveCvoutStageTemplateImpl orDefault = activeCvoutStageMap.getOrDefault(stage, null);
        return orDefault;
    }

    public List<Integer> getTreasureList(){
        List<Integer> collect = activeTreasureMap.values().stream().map(ActiveCvoutMapTemplateImpl::getMap_id).collect(Collectors.toList());
        return collect;
    }


    /**
     * @description
     *          转盘获取奖励
     * @param ids  已领取的ID集合
     * @param playerLv
     * @param circleId 转盘组 id
     * @return com.hm.config.excel.templaextra.ActiveCvoutCircleTemplateImpl
     * @date 2021/2/5 9:36
     */
    public ActiveCvoutCircleTemplateImpl getRandomCircle(Set<Integer> ids, int playerLv, int circleId) {
        Map<ActiveCvoutCircleTemplateImpl, Integer> collect = activeCvoutCircleMap.values().stream().filter(e -> {
            return e.isFit(playerLv) && !ids.contains(e.getId()) && e.getCircle_id() == circleId;
        }).collect(Collectors.toMap(Function.identity(), e -> e.getWeight()));

        WeightMeta<ActiveCvoutCircleTemplateImpl> randomWeight = RandomUtils.buildWeightMeta(collect);
        return randomWeight.random();
    }

    public boolean checkCircleTimes(Set<Integer> ids, int playerLv, int circleId){
        int size = activeCvoutCircleMap.values().stream().filter(e -> {
            return e.isFit(playerLv) && e.getCircle_id() == circleId;
        }).collect(Collectors.toList()).size();
        return size > ids.size();
    }

    /**
     * @description
     *          充值返利  奖励获取
     * @param playerLv
     * @param rechargeGold
     * @return com.hm.config.excel.templaextra.ActiveCvoutRechargeOnceTemplateImpl
     * @date 2021/2/5 9:11
     */
    public ActiveCvoutRechargeOnceTemplateImpl getRechargeOnceTemplate(int playerLv, int rechargeGold){
        ActiveCvoutRechargeOnceTemplateImpl rechargeOnceTemplate = rechargeOnceMap.values().stream().filter(e -> e.isFit(playerLv) && e.getRecharge_gold() == rechargeGold).findFirst().orElse(null);
        return rechargeOnceTemplate;
    }

    public ActiveCvoutRechargeOnceTemplateImpl getRewardById(int id, int playerLv){
        ActiveCvoutRechargeOnceTemplateImpl rechargeOnceTemplate = rechargeOnceMap.values().stream().filter(e -> e.isFit(playerLv) && e.getId()== id).findFirst().orElse(null);
        return rechargeOnceTemplate;
    }

    public boolean containRechargePoint(int rechargePoint) {
        return rechargePointSet.contains(rechargePoint);
    }

    /**
     * @description
     * @param giftId
     * @param playerLv
     * @return com.hm.config.excel.templaextra.ActiveCvoutGiftTemplateImpl
     * @date 2021/2/7 13:47
     */
    public ActiveCvoutGiftTemplateImpl getByRechargeId(int giftId, int playerLv){
        ActiveCvoutGiftTemplateImpl template = ActiveCvoutGiftMap.getOrDefault(giftId, null);
        if(template != null && template.isFit(playerLv)){
            return template;
        }
        return null;
    }

    public int getByStageAndRechargeGroup(int stage, int rechargeGroup, int mapId){
        List<ActiveCvoutTemplateImpl> activeCvoutTemplates = activeCvoutMap.get(stage);
        ActiveCvoutTemplateImpl activeCvoutTemplate = activeCvoutTemplates.stream().filter(e -> rechargeGroup == e.getRecharge_gift_group() && mapId == e.getMap_id()).findFirst().orElse(null);
        if(activeCvoutTemplate == null){
            return 0;
        }
        return activeCvoutTemplate.getLocation();
    }

    public ActiveCvoutBossTemplateImpl getStageBossById(int id) {
        ActiveCvoutBossTemplateImpl template = activeCvoutBossMap.getOrDefault(id, null);
        return template;
    }


    private void loadActiveCvout(){
        List<ActiveCvoutTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveCvoutTemplateImpl.class), new TypeReference<List<ActiveCvoutTemplateImpl>>() {
        });
        Map<Integer, List<ActiveCvoutTemplateImpl>> map = Maps.newConcurrentMap();
        HashMap<Integer, ActiveCvoutTemplateImpl> hashMap = Maps.newHashMap();
        list.forEach(e -> {
            e.init();
            List<ActiveCvoutTemplateImpl> orDefault = map.getOrDefault(e.getStage(), Lists.newArrayList());
            orDefault.add(e);
            map.put(e.getStage(), orDefault);
            hashMap.put(e.getId(), e);
        });
        activeCvoutMap = ImmutableMap.copyOf(map);
        cvoutMap = ImmutableMap.copyOf(hashMap);
    }

    private void loadActiveCvoutRechargeOnce(){
        List<ActiveCvoutRechargeOnceTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveCvoutRechargeOnceTemplateImpl.class), new TypeReference<List<ActiveCvoutRechargeOnceTemplateImpl>>() {
        });
        Map<Integer, ActiveCvoutRechargeOnceTemplateImpl> map = Maps.newConcurrentMap();
        Set<Integer> set = Sets.newHashSet();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
            set.add(e.getRecharge_gold());
        });
        rechargePointSet = ImmutableSet.copyOf(set);
        rechargeOnceMap = ImmutableMap.copyOf(map);
    }

    private void loadActiveCvoutStageMap(){
        List<ActiveCvoutStageTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveCvoutStageTemplateImpl.class), new TypeReference<List<ActiveCvoutStageTemplateImpl>>() {
        });
        Map<Integer, ActiveCvoutStageTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getStage(), e);
        });
        activeCvoutStageMap = ImmutableMap.copyOf(map);
    }

    private void loadActiveCvoutMissionMap(){
        List<ActiveCvoutMissionTemplate> list = JSONUtil.fromJson(getJson(ActiveCvoutMissionTemplate.class), new TypeReference<List<ActiveCvoutMissionTemplate>>() {
        });
        Map<Integer, ActiveCvoutMissionTemplate> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            map.put(e.getLevel(), e);
        });
        activeCvoutMissionMap = ImmutableMap.copyOf(map);
    }

    private void loadActiveCvoutBossMap(){
        List<ActiveCvoutBossTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveCvoutBossTemplateImpl.class), new TypeReference<List<ActiveCvoutBossTemplateImpl>>() {
        });
        Map<Integer, ActiveCvoutBossTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            map.put(e.getId(), e);
        });
        activeCvoutBossMap = ImmutableMap.copyOf(map);
    }
    private void loadActiveTreasureMap(){
        List<ActiveCvoutMapTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveCvoutMapTemplateImpl.class), new TypeReference<List<ActiveCvoutMapTemplateImpl>>() {
        });
        Map<Integer, ActiveCvoutMapTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getMap_id(), e);
        });
        activeTreasureMap = ImmutableMap.copyOf(map);
    }

    private void loadActiveCvoutGift(){
        List<ActiveCvoutGiftTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveCvoutGiftTemplateImpl.class), new TypeReference<List<ActiveCvoutGiftTemplateImpl>>() {
        });
        Map<Integer, ActiveCvoutGiftTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            map.put(e.getRecharge_id(), e);
        });
        ActiveCvoutGiftMap = ImmutableMap.copyOf(map);
    }

    private void loadActiveCvoutCircle(){
        List<ActiveCvoutCircleTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveCvoutCircleTemplateImpl.class), new TypeReference<List<ActiveCvoutCircleTemplateImpl>>() {
        });
        Map<Integer, ActiveCvoutCircleTemplateImpl> map = Maps.newConcurrentMap();
        list.forEach(e -> {
            e.init();
            map.put(e.getId(), e);
        });
        activeCvoutCircleMap = ImmutableMap.copyOf(map);
    }
}
