package com.hm.config.excel;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.ActiveRechargeCircleTemplate;
import com.hm.config.excel.templaextra.ActiveRechargeCircleTemplateImpl;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @description: 充值转盘
 * @author: chenwei
 * @create: 2019-12-24 16:58
 **/
@Config
public class ActiveRechargeCircleConfig extends ExcleConfig{
    // 最小可记录品质
    public static final int MIN_RECEIVE_QUALITY = 4;
    // 最大记录数量
    public static final int MAX_RECORD_QUANTITY = 20;

    //充值转盘
    private Map<Integer, ActiveRechargeCircleTemplateImpl> rechargeCircleMap = Maps.newConcurrentMap();
    private ArrayListMultimap<Integer,ActiveRechargeCircleTemplateImpl> versionTemplate = ArrayListMultimap.create();

    @Override
    public void loadConfig() {
//        loadRechargeCircleConfig();
    }

    private void loadRechargeCircleConfig() {
        ArrayListMultimap<Integer,ActiveRechargeCircleTemplateImpl> versionTemplateImplMap = ArrayListMultimap.create();
        List<ActiveRechargeCircleTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveRechargeCircleTemplateImpl.class), new TypeReference<List<ActiveRechargeCircleTemplateImpl>>(){});
        list.forEach(e -> {
            e.init();
            versionTemplateImplMap.put(e.getRound(),e);
        });

        Map<Integer,ActiveRechargeCircleTemplateImpl> templateMap = list.stream().collect(Collectors.toMap(ActiveRechargeCircleTemplate::getId, Function.identity()));
        rechargeCircleMap = ImmutableMap.copyOf(templateMap);
        versionTemplate = versionTemplateImplMap;

    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveRechargeCircleTemplateImpl.class);
    }

    public ActiveRechargeCircleTemplateImpl getRechargeCircle(int id){
        return rechargeCircleMap.get(id);
    }

    /**
     * 获取可领取次数 = 满足条件 - (已领取 + 未领取）
     * @param version 当前版本
     * @param serverLv 服务器等级
     * @param totalRecharge 充值总金砖
     * @param alreadyNum 已领取 + 未领取
     * @return
     */
    public long getAllCanReceiveCount(int version,int serverLv,int totalRecharge, int alreadyNum){
        long count = versionTemplate.get(version).stream()
                .filter(e -> e.isFit(serverLv))
                .filter(e -> e.getRecharge_gold() <= totalRecharge)
                .count();
        return count - alreadyNum;
    }


    /**
     * 获取随机奖励
     * @param version 当前版本
     * @param serverLv 服务器等级
     * @param idList 已领取
     * @param receiveIds 未领取
     * @return
     */
    public ActiveRechargeCircleTemplateImpl getRandomTemplate(int version,int serverLv,List<Integer> idList, List<Integer> receiveIds){
        // 可领取的奖励
        List<ActiveRechargeCircleTemplateImpl> list = this.getRechargeCircleList(version, serverLv, idList, receiveIds);
        if (CollUtil.isEmpty(list)){
            return null;
        }
        WeightMeta<ActiveRechargeCircleTemplateImpl> randomLibrary = getRandomLibrary(list);
        return randomLibrary.random();

    }

    /**
     * 获取当前次数可领取的奖励
     * @param version
     * @param serverLv 服务器等级
     * @param receiveIds
     * @return
     */
    public List<ActiveRechargeCircleTemplateImpl> getRechargeCircleList(int version,int serverLv,List<Integer> idList,List<Integer> receiveIds){
        // 第几次领取
        int times = idList.size() + receiveIds.size() + 1;
        List<ActiveRechargeCircleTemplateImpl> collect = versionTemplate.get(version).stream()
                .filter(e -> e.isFit(serverLv))
                .filter(e -> !receiveIds.contains(e.getId()))
                .filter(e -> !idList.contains(e.getId()))
                .filter(e -> e.getNumber() <= times).collect(Collectors.toList());
        return collect;
    }

    private WeightMeta<ActiveRechargeCircleTemplateImpl> getRandomLibrary(List<ActiveRechargeCircleTemplateImpl> rechargeCircleLib) {
        Map<ActiveRechargeCircleTemplateImpl, Integer> wm = Maps.newConcurrentMap();
        for (int i = 0; i < rechargeCircleLib.size(); i++) {
            ActiveRechargeCircleTemplateImpl temp = rechargeCircleLib.get(i);
            wm.put(temp, temp.getWeight());
        }
        return RandomUtils.buildWeightMeta(wm);
    }

}
