package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveNavyGiftTemplate;
import com.hm.config.excel.temlate.ActiveNavyWeaponTemplate;
import com.hm.config.excel.templaextra.ActiveNavyGiftTemplateImpl;
import com.hm.config.excel.templaextra.ActiveNavyRewardTemplateImpl;
import com.hm.config.excel.templaextra.ActiveNavyWeaponTemplateImpl;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.collect.*;
/**
 * @description: 海军节
 * @author: chenwei
 * @create: 2020-03-30 14:53
 **/
@Config
public class ActivityNavyConfig extends ExcleConfig{
    private Map<Integer, ActiveNavyGiftTemplateImpl> navyGiftTemplateMap = Maps.newConcurrentMap();
    private ArrayListMultimap<Integer,ActiveNavyGiftTemplateImpl> navyGiftMultimap = ArrayListMultimap.create();
    private Map<Integer, ActiveNavyWeaponTemplateImpl> navyWeaponTemplateMap = Maps.newConcurrentMap();
    private Map<Integer, ActiveNavyRewardTemplateImpl> navyRewardTemplateMap = Maps.newConcurrentMap();
    private ArrayListMultimap<Integer,ActiveNavyRewardTemplateImpl> navyRewardTemplateMultimap = ArrayListMultimap.create();


    @Override
    public void loadConfig() {
//        loadNavyGift();
//        loadNavyWeapon();
//        loadNamyReward();
    }

    private void loadNavyGift() {
        ArrayListMultimap<Integer,ActiveNavyGiftTemplateImpl> temMultimap = ArrayListMultimap.create();
        List<ActiveNavyGiftTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveNavyGiftTemplateImpl.class), new TypeReference<List<ActiveNavyGiftTemplateImpl>>() {});
        list.forEach(e -> temMultimap.put(e.getRecharge_gift_id(), e));
        Map<Integer, ActiveNavyGiftTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(ActiveNavyGiftTemplateImpl::getId, Function.identity()));

        navyGiftTemplateMap = ImmutableMap.copyOf(tempMap);
        navyGiftMultimap = temMultimap;
    }

    private void loadNavyWeapon() {
        List<ActiveNavyWeaponTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveNavyWeaponTemplateImpl.class), new TypeReference<List<ActiveNavyWeaponTemplateImpl>>() {});
        list.forEach(e ->e.init());
        Map<Integer, ActiveNavyWeaponTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(ActiveNavyWeaponTemplateImpl::getId, Function.identity()));
        navyWeaponTemplateMap = ImmutableMap.copyOf(tempMap);
    }

    private void loadNamyReward() {
        List<ActiveNavyRewardTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveNavyRewardTemplateImpl.class), new TypeReference<List<ActiveNavyRewardTemplateImpl>>() {});
        ArrayListMultimap<Integer,ActiveNavyRewardTemplateImpl> tempMultimap = ArrayListMultimap.create();
        list.forEach(e -> {
            e.init();
            tempMultimap.put(e.getType(), e);
        });
        Map<Integer, ActiveNavyRewardTemplateImpl> tempMap = list.stream().collect(Collectors.toMap(ActiveNavyRewardTemplateImpl::getId, Function.identity()));
        navyRewardTemplateMap = ImmutableMap.copyOf(tempMap);
        navyRewardTemplateMultimap = tempMultimap;
    }


    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveNavyGiftTemplate.class,
                ActiveNavyWeaponTemplate.class,
                ActiveNavyRewardTemplateImpl.class);
    }

    public ActiveNavyGiftTemplateImpl getNavyGiftTemplate(int id){
        return navyGiftTemplateMap.get(id);
    }

    public ActiveNavyWeaponTemplateImpl getNavyWeaponTemplate(int id){
        return navyWeaponTemplateMap.get(id);
    }

    /**
     * 计费点礼包
     * @param rechargeGiftId
     * @param playerLv
     * @return
     */
    public ActiveNavyGiftTemplateImpl getNavyGiftTemplate(int rechargeGiftId, int playerLv){
        List<ActiveNavyGiftTemplateImpl> list = navyGiftMultimap.get(rechargeGiftId);
        if (CollUtil.isNotEmpty(list)){
            return list.stream().filter(e -> e.isFit(playerLv)).findFirst().orElse(null);
        }
        return null;
    }

    /**
     * 获取击中奖励
     * @param type 武器类型 即武器表id
     * @param playerLv 玩家等级
     * @param times 次数
     * @return
     */
    public ActiveNavyRewardTemplateImpl getNavyRewardTemplate(int type, int playerLv, int times){
        List<ActiveNavyRewardTemplateImpl> list = navyRewardTemplateMultimap.get(type);
        if (CollUtil.isNotEmpty(list)){
            Map<ActiveNavyRewardTemplateImpl,Integer> wm = Maps.newHashMap();
            List<ActiveNavyRewardTemplateImpl> rewardTemplateList = list.stream()
                    .filter(e -> e.isFit(playerLv))
                    .filter(e -> e.getBase() <= times).collect(Collectors.toList());
            for (ActiveNavyRewardTemplateImpl template : rewardTemplateList){
                wm.put(template, template.getWeight());
                if (template.getBase() == times){
                    return template;
                }
            }
            return RandomUtils.buildWeightMeta(wm).random();
        }
        return null;
    }

}
