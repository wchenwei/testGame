package com.hm.config.excel;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveFlashSaleRandomTemplate;
import com.hm.config.excel.temlate.ActiveFlashSaleTemplate;
import com.hm.config.excel.temlate.ActiveFlashSaleTicketTemplate;
import com.hm.config.excel.templaextra.ActiveFlashSaleTemplateImpl;
import com.hm.config.excel.templaextra.ActiveFlashSaleTicketTemplateImpl;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;
import org.springframework.data.util.Pair;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2019-06-21
 */
@Config
public class Discount111Config extends ExcleConfig {
    /**
     * stage:giftId:obj
     */
    private Table<Integer, Integer, ActiveFlashSaleTemplateImpl> salesTable = HashBasedTable.create();
    private ListMultimap<Integer, Integer> giftIdMap = ArrayListMultimap.create();
    /**
     * id:obj
     */
    private Map<Integer, ActiveFlashSaleTicketTemplateImpl> ticketMap = Maps.newConcurrentMap();
    /**
     * [level.low, level.high] : weightMeta
     */
    private Map<Pair<Integer, Integer>, WeightMeta<Integer>> wmMap = Maps.newConcurrentMap();

    /**
     * 天数:单日购买次数:object
     */
    private Table<Integer, Integer, ActiveFlashSaleRandomTemplate> randomTable = HashBasedTable.create();

    public ActiveFlashSaleTemplateImpl getSalesCfg(int stage, int day) {
        return salesTable.row(stage).values().stream().filter(e->e.getDay() == day).findFirst().orElse(null);
    }

    public ActiveFlashSaleTemplateImpl getSalesCfgByGiftId(int stage, int giftId) {
        return salesTable.get(stage, giftId);
    }

    /**
     * 根据服务器等级随机一条配置信息
     *
     * @param serverLv
     * @return
     */
    public ActiveFlashSaleTicketTemplateImpl pickTicketCfg(int serverLv) {
        for (Pair<Integer, Integer> pair : wmMap.keySet()) {
            if (pair.getFirst() <= serverLv && serverLv <= pair.getSecond()) {
                Integer id = wmMap.get(pair).random();
                return ticketMap.getOrDefault(id, null);
            }
        }
        return null;
    }

    public ActiveFlashSaleTicketTemplateImpl getTicketCfg(int id) {
        return ticketMap.getOrDefault(id, null);
    }

    /**
     * 获取保底次数列表
     * [ActiveFlashSaleTicketTemplateImpl::getId : base]
     *
     * @param serverLv
     * @return
     */
    public Map<Integer, Integer> getBaseMap(int serverLv) {
        Map<Integer, Integer> map = ticketMap.values().stream()
                .filter(e -> e.getBase() > 0 && e.getSever_lv_down() <= serverLv && serverLv <= e.getSever_lv_up())
                .collect(Collectors.toMap(ActiveFlashSaleTicketTemplate::getId, ActiveFlashSaleTicketTemplate::getBase));
        return map;
    }

    /**
     * 获取每个计费点购买次数map
     *
     * @return
     */
    public Map<Integer, Integer> getBuyTimesMap() {
        return salesTable.values().stream().collect(Collectors.toMap(ActiveFlashSaleTemplate::getId, ActiveFlashSaleTemplate::getBuy_times));
    }

    /**
     * 获取活动期间充值使用的gift id
     *
     * @return
     */
    public Collection<Integer> getRechargeGiftList(int stage) {
        return giftIdMap.get(stage);
    }

    /**
     * 随机一个假数据
     *
     * @param nDays  活动第n天
     * @param nTimes 该次是该计费点第n次购买
     * @return
     */
    public int getRandomNum(int nDays, int nTimes) {
        if (!randomTable.contains(nDays, nTimes)) {
            return 0;
        }
        ActiveFlashSaleRandomTemplate e = randomTable.get(nDays, nTimes);
        return RandomUtil.randomInt(e.getRandom_down(), e.getRandom_up() + 1);
    }

    @Override
    public void loadConfig() {
//        List<ActiveFlashSaleTicketTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveFlashSaleTicketTemplateImpl.class), new TypeReference<List<ActiveFlashSaleTicketTemplateImpl>>() {
//        });
//        Map<Integer, ActiveFlashSaleTicketTemplateImpl> tm = Maps.newConcurrentMap();
//        list.forEach(e -> {
//            e.init();
//            tm.put(e.getId(), e);
//        });
//        ticketMap = ImmutableMap.copyOf(tm);
//
//        Map<Pair<Integer, Integer>, List<ActiveFlashSaleTicketTemplateImpl>> map = list.stream().collect(Collectors.groupingBy(ActiveFlashSaleTicketTemplateImpl::genGroupKey));
//        Map<Pair<Integer, Integer>, WeightMeta<Integer>> wmMapTemp = Maps.newConcurrentMap();
//        for (Map.Entry<Pair<Integer, Integer>, List<ActiveFlashSaleTicketTemplateImpl>> entry : map.entrySet()) {
//            // id:weight
//            Map<Integer, Integer> wm = entry.getValue().stream().collect(Collectors.toMap(ActiveFlashSaleTicketTemplate::getId, ActiveFlashSaleTicketTemplate::getWeight));
//            WeightMeta<Integer> weightMeta = RandomUtils.buildWeightMeta(wm);
//            wmMapTemp.put(entry.getKey(), weightMeta);
//        }
//        wmMap = ImmutableMap.copyOf(wmMapTemp);
//
//        List<ActiveFlashSaleTemplateImpl> list1 = JSONUtil.fromJson(getJson(ActiveFlashSaleTemplateImpl.class), new TypeReference<List<ActiveFlashSaleTemplateImpl>>() {
//        });
//        Table<Integer, Integer, ActiveFlashSaleTemplateImpl> tm1 = HashBasedTable.create();
//        ListMultimap<Integer, Integer> stage2GiftId = ArrayListMultimap.create();
//        list1.forEach(e -> {
//            e.init();
//            tm1.put(e.getStage(), e.getRecharge_gift_id(), e);
//            stage2GiftId.put(e.getStage(), e.getRecharge_gift_id());
//        });
//        salesTable = ImmutableTable.copyOf(tm1);
//        giftIdMap = ImmutableListMultimap.copyOf(stage2GiftId);
//
//        Table<Integer, Integer, ActiveFlashSaleRandomTemplate> tbl = HashBasedTable.create();
//        List<ActiveFlashSaleRandomTemplate> list2 = JSONUtil.fromJson(getJson(ActiveFlashSaleRandomTemplate.class), new TypeReference<List<ActiveFlashSaleRandomTemplate>>() {
//        });
//
//        list2.forEach(e -> {
//            tbl.put(e.getDays(), e.getBuy_times(), e);
//        });
//
//        randomTable = ImmutableTable.copyOf(tbl);
    }

    /**
     * 更新随机 weight meta, 排除已经没名额的奖励
     * id : count
     *
     * @param exclude
     */
    public void updateWMMap(Map<Integer, Integer> exclude) {
        List<ActiveFlashSaleTicketTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveFlashSaleTicketTemplateImpl.class), new TypeReference<List<ActiveFlashSaleTicketTemplateImpl>>() {
        });
        Iterator<ActiveFlashSaleTicketTemplateImpl> iterator = list.iterator();
        while (iterator.hasNext()) {
            ActiveFlashSaleTicketTemplateImpl template = iterator.next();
            if (template.getLimit() > 0 && exclude.containsKey(template.getId())) {
                if (exclude.get(template.getId()) >= template.getLimit()) {
                    iterator.remove();
                }
            }
        }

        Map<Pair<Integer, Integer>, List<ActiveFlashSaleTicketTemplateImpl>> map = list.stream().collect(Collectors.groupingBy(ActiveFlashSaleTicketTemplateImpl::genGroupKey));
        Map<Pair<Integer, Integer>, WeightMeta<Integer>> wmMapTemp = Maps.newConcurrentMap();
        for (Map.Entry<Pair<Integer, Integer>, List<ActiveFlashSaleTicketTemplateImpl>> entry : map.entrySet()) {
            // id:weight
            Map<Integer, Integer> wm = entry.getValue().stream().collect(Collectors.toMap(ActiveFlashSaleTicketTemplate::getId, ActiveFlashSaleTicketTemplate::getWeight));
            WeightMeta<Integer> weightMeta = RandomUtils.buildWeightMeta(wm);
            wmMapTemp.put(entry.getKey(), weightMeta);
        }
        wmMap = ImmutableMap.copyOf(wmMapTemp);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveFlashSaleTicketTemplateImpl.class, ActiveFlashSaleTemplateImpl.class, ActiveFlashSaleRandomTemplate.class);
    }
}
