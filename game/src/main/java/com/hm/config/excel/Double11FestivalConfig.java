package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveShoppingLibraryTemplateImpl;
import com.hm.config.excel.templaextra.ActiveShoppingRechargeOnceTemplateImpl;
import com.hm.config.excel.templaextra.ActiveShoppingRewardTemplateImpl;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import com.google.common.collect.*;
/**
 * @author wyp
 * @description
 *          双十一 狂欢盛典
 * @date 2020/10/15 11:46
 */
@Config
public class Double11FestivalConfig extends ExcleConfig{
    // 充值返利
    private List<ActiveShoppingRechargeOnceTemplateImpl> listRechargeOnce = Lists.newArrayList();

    private Set RechargeGoldSet = Sets.newHashSet();

    // reward_type:entity   奖品
    private Map<Integer, List<ActiveShoppingLibraryTemplateImpl>> dataTable = Maps.newConcurrentMap();
    // 抽奖物品
    private Map<Integer, ActiveShoppingRewardTemplateImpl> rawardMap = Maps.newConcurrentMap();

    // 普通奖励 0, 幸运奖励 1
    private Map<Integer, WeightMeta<Integer>> mapWM = Maps.newHashMap();


    @Override
    public void loadConfig() {
//        loadShoppingRechargeOnce();
//        loadShoppingLibrary();
//        loadShoppingReward();

    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveShoppingRechargeOnceTemplateImpl.class,
                            ActiveShoppingLibraryTemplateImpl.class,
                            ActiveShoppingRewardTemplateImpl.class);
    }

    private void loadShoppingReward(){
        List<ActiveShoppingRewardTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveShoppingRewardTemplateImpl.class), new TypeReference<List<ActiveShoppingRewardTemplateImpl>>() {});

        // 构造随机WeightMeta obj
        Map<Integer, WeightMeta<Integer>> weightMetaMap = Maps.newHashMap();
        Map<Integer, ActiveShoppingRewardTemplateImpl> map = Maps.newConcurrentMap();
        Map<Integer, Integer> rate = Maps.newHashMap();
        Map<Integer, Integer> luck = Maps.newHashMap();

        list.stream().forEach(entity->{
            if (entity.getRate() > 0) {
                rate.put(entity.getPos(), entity.getRate());
            }
            if (entity.getLucky_rate() > 0) {
                luck.put(entity.getPos(), entity.getLucky_rate());
            }
            map.put(entity.getPos(),entity);
        });
        weightMetaMap.put(0, RandomUtils.buildWeightMeta(rate));
        weightMetaMap.put(1, RandomUtils.buildWeightMeta(luck));
        this.mapWM = ImmutableMap.copyOf(weightMetaMap);
        this.rawardMap = ImmutableMap.copyOf(map);
    }


    private void loadShoppingRechargeOnce(){
        List<ActiveShoppingRechargeOnceTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveShoppingRechargeOnceTemplateImpl.class), new TypeReference<List<ActiveShoppingRechargeOnceTemplateImpl>>() {});
        List<ActiveShoppingRechargeOnceTemplateImpl> data = Lists.newArrayList();
        Set set = Sets.newHashSet();
        list.forEach(e->{
            e.init();
            data.add(e);
            set.add(e.getRecharge_gold());
        });
        this.listRechargeOnce = ImmutableList.copyOf(data);
        this.RechargeGoldSet = ImmutableSet.copyOf(set);
    }


    private void loadShoppingLibrary(){
        List<ActiveShoppingLibraryTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveShoppingLibraryTemplateImpl.class), new TypeReference<List<ActiveShoppingLibraryTemplateImpl>>() {});
        Map<Integer, List<ActiveShoppingLibraryTemplateImpl>> map = Maps.newConcurrentMap();
        list.forEach(e->{
            e.init();
            Integer reward_type = e.getReward_type();
            List<ActiveShoppingLibraryTemplateImpl> entryList = map.containsKey( reward_type) ? map.get(reward_type) : Lists.newArrayList();
            entryList.add(e);
            map.put(reward_type,entryList);
        });
        this.dataTable = ImmutableMap.copyOf(map);
    }


    public ActiveShoppingLibraryTemplateImpl getReward(ActiveShoppingRewardTemplateImpl activeShoppingRewardTemplate, int playerLv){
        List<ActiveShoppingLibraryTemplateImpl> activeShoppingLibraryTemplates = dataTable.get(activeShoppingRewardTemplate.getReward_id());
        ActiveShoppingLibraryTemplateImpl activeShoppingLibraryTemplate = activeShoppingLibraryTemplates.stream().filter(e -> e.isFit(playerLv)).findFirst().orElse(null);
        return activeShoppingLibraryTemplate;
    }

    /**
     * @description
     * @param isNormal  0 为普通奖励，1 为稀有奖励
     * @return com.hm.config.excel.templaextra.ActiveShoppingRewardTemplateImpl
     * @author wyp
     * @date 2020/10/15 15:41
     */
    public ActiveShoppingRewardTemplateImpl getShoppingRewardTemplate(boolean isNormal){
        int key = 1;
        if(isNormal){
            key = 0;
        }
        Integer random = mapWM.get(key).random();
        if (random == null) {
            return null;
        }
        ActiveShoppingRewardTemplateImpl activeShoppingRewardTemplate = rawardMap.get(random);
        if(Objects.isNull(activeShoppingRewardTemplate)){
            return null;
        }
        return activeShoppingRewardTemplate;
    }

    /**
     * @description
     *          获取充值后的奖励
     * @param rechargeGold  充值金砖数量
     * @param playerLv  用户等级
     * @return com.hm.config.excel.templaextra.ActiveShoppingRechargeOnceTemplateImpl
     * @author wyp
     * @date 2020/10/15 12:02
     */
    public ActiveShoppingRechargeOnceTemplateImpl getByrechargeAndLv(int rechargeGold,int playerLv){
        ActiveShoppingRechargeOnceTemplateImpl activeShoppingRechargeOnceTemplate = listRechargeOnce.stream().filter(e -> e.isFit(playerLv) && e.getRecharge_gold() == rechargeGold).findFirst().orElse(null);
        return activeShoppingRechargeOnceTemplate;
    }

    public ActiveShoppingRechargeOnceTemplateImpl getById(int id, int playerLv){
        ActiveShoppingRechargeOnceTemplateImpl activeShoppingRechargeOnceTemplate = listRechargeOnce.stream().filter(e -> e.isFit(playerLv) && e.getId() == id).findFirst().orElse(null);
        return activeShoppingRechargeOnceTemplate;
    }

    public boolean containRechargePoint(int rechargePoint) {
        return RechargeGoldSet.contains(rechargePoint);
    }
}
