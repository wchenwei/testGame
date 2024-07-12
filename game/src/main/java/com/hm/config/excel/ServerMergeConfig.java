package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ServiceMergeGiftTemplate;
import com.hm.config.excel.temlate.ServiceMergeRechargeRewardTemplate;
import com.hm.config.excel.templaextra.ServiceMergeDiscountTemplateImpl;
import com.hm.config.excel.templaextra.ServiceMergeRankRewardTemplateImpl;
import com.hm.config.excel.templaextra.ServiceMergeRechargeRewardTemplateImpl;
import com.hm.config.excel.templaextra.ServiceMergeSignTemplateImpl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2019-03-28
 *
 * @author Administrator
 */
@Config
public class ServerMergeConfig extends ExcleConfig {
    private Map<Integer, ServiceMergeDiscountTemplateImpl> discountMap = Maps.newConcurrentMap();
    private Map<Integer, ServiceMergeRechargeRewardTemplateImpl> rechargeMap = Maps.newConcurrentMap();
    private List<ServiceMergeRankRewardTemplateImpl> rankList = Lists.newArrayList();
    private Map<Integer, ServiceMergeGiftTemplate> giftMap = Maps.newConcurrentMap();
    private Map<Integer, ServiceMergeSignTemplateImpl> signMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
        Map<Integer, ServiceMergeDiscountTemplateImpl> tmpDiscountMap = Maps.newConcurrentMap();
        List<ServiceMergeDiscountTemplateImpl> list = JSONUtil.fromJson(getJson(ServiceMergeDiscountTemplateImpl.class), new TypeReference<List<ServiceMergeDiscountTemplateImpl>>() {
        });
        list.forEach(t -> {
            t.init();
            tmpDiscountMap.put(t.getId(), t);
        });

        discountMap = ImmutableMap.copyOf(tmpDiscountMap);

        Map<Integer, ServiceMergeRechargeRewardTemplateImpl> tmpRechargeMap = Maps.newConcurrentMap();
        List<ServiceMergeRechargeRewardTemplateImpl> li = JSONUtil.fromJson(getJson(ServiceMergeRechargeRewardTemplateImpl.class), new TypeReference<List<ServiceMergeRechargeRewardTemplateImpl>>() {
        });
        li.forEach(t -> {
            t.init();
            tmpRechargeMap.put(t.getId(), t);
        });
        rechargeMap = ImmutableMap.copyOf(tmpRechargeMap);

        List<ServiceMergeRankRewardTemplateImpl> lj = JSONUtil.fromJson(getJson(ServiceMergeRankRewardTemplateImpl.class), new TypeReference<List<ServiceMergeRankRewardTemplateImpl>>() {
        });
        lj.forEach(ServiceMergeRankRewardTemplateImpl::init);
        rankList = ImmutableList.copyOf(lj);

        Map<Integer, ServiceMergeGiftTemplate> tm = Maps.newConcurrentMap();
        List<ServiceMergeGiftTemplate> l = JSONUtil.fromJson(getJson(ServiceMergeGiftTemplate.class), new TypeReference<List<ServiceMergeGiftTemplate>>() {
        });
        l.forEach(e -> tm.put(e.getId(), e));
        giftMap = ImmutableMap.copyOf(tm);


        Map<Integer, ServiceMergeSignTemplateImpl> tm1 = Maps.newConcurrentMap();
        List<ServiceMergeSignTemplateImpl> l1 = JSONUtil.fromJson(getJson(ServiceMergeSignTemplateImpl.class), new TypeReference<List<ServiceMergeSignTemplateImpl>>() {
        });
        l1.forEach(e -> {
            e.init();
            tm1.put(e.getId(), e);
        });
        signMap = ImmutableMap.copyOf(tm1);
    }

    public ServiceMergeDiscountTemplateImpl getDiscountCfg(int id) {
        return discountMap.getOrDefault(id, null);
    }

    /**
     * 根据充值金砖数量返回可以获得的奖励 id 字段 list
     *
     * @param serverLv    服务器等级
     * @param rechargeNum 充值金额(金砖)
     * @return
     */
    public Collection<Integer> getRechargeRewardList(int serverLv, int rechargeNum) {
        return rechargeMap.values().stream().filter(e -> e.getService_level_lower() <= serverLv &&
                serverLv <= e.getService_level_upper() && e.getRecharge_num() <= rechargeNum).
                map(ServiceMergeRechargeRewardTemplate::getId).collect(Collectors.toList());
    }

    public ServiceMergeRechargeRewardTemplateImpl getServiceMergeRechargeRewardTemplate(int id) {
        return rechargeMap.getOrDefault(id, null);
    }

    /**
     * @param serverLv 服务器等级
     * @param type     排行榜类型
     * @param rank     排名
     * @return
     */
    public ServiceMergeRankRewardTemplateImpl getRankRewardCfg(int serverLv, int type, int rank) {
        return rankList.stream().filter(e -> e.getType().equals(type)).filter(e -> e.getService_level_lower() <= serverLv && serverLv <= e.getService_level_upper())
                .filter(e -> e.getRank1() <= rank && rank <= e.getRank2()).findAny().orElse(null);
    }

    /**
     * 获取排行榜奖励列表
     *
     * @param serverLv
     * @param type
     * @return
     */
    public List<ServiceMergeRankRewardTemplateImpl> getRankList(int serverLv, int type) {
        return rankList.stream().filter(e -> e.getType().equals(type)).filter(e -> e.getService_level_lower() <= serverLv && serverLv <= e.getService_level_upper()).collect(Collectors.toList());
    }

    public ServiceMergeSignTemplateImpl getSignCfg(int id) {
        return signMap.getOrDefault(id, null);
    }

    /**
     * 合服活动用到到礼包id
     *
     * @return
     */
    public Collection<Integer> getRechargeGiftList() {
        return giftMap.values().stream().map(ServiceMergeGiftTemplate::getRecharge_id).collect(Collectors.toSet());
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ServiceMergeDiscountTemplateImpl.class, ServiceMergeRankRewardTemplateImpl.class,
                ServiceMergeRechargeRewardTemplateImpl.class, ServiceMergeGiftTemplate.class,
                ServiceMergeSignTemplateImpl.class);
    }
}
