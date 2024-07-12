package com.hm.config.excel;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.Active12RandomTemplate;
import com.hm.config.excel.temlate.Active12SalesTemplate;
import com.hm.config.excel.temlate.Active12TicketTemplate;
import com.hm.config.excel.templaextra.Active12SalesTemplateImpl;
import com.hm.config.excel.templaextra.Active12TicketTemplateImpl;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;
import org.springframework.data.util.Pair;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.common.collect.*;
/**
 * Description:
 * User: yang xb
 * Date: 2019-06-21
 */
@Config
public class Active12Config extends ExcleConfig {
    /**
     * id:obj
     */
    private Map<Integer, Active12SalesTemplateImpl> salesMap = Maps.newConcurrentMap();
    /**
     * id:obj
     */
    private Map<Integer, Active12TicketTemplateImpl> ticketMap = Maps.newConcurrentMap();
    /**
     * [level.low, level.high] : weightMeta
     */
    private Map<Pair<Integer, Integer>, WeightMeta<Integer>> wmMap = Maps.newConcurrentMap();

    /**
     * 天数:单日购买次数:object
     */
    private Table<Integer, Integer, Active12RandomTemplate> randomTable = HashBasedTable.create();

    public Active12SalesTemplateImpl getSalesCfg(int id) {
        return salesMap.getOrDefault(id, null);
    }

    /**
     * 根据服务器等级随机一条配置信息
     *
     * @param serverLv
     * @return
     */
    public Active12TicketTemplateImpl pickTicketCfg(int serverLv) {
        for (Pair<Integer, Integer> pair : wmMap.keySet()) {
            if (pair.getFirst() <= serverLv && serverLv <= pair.getSecond()) {
                Integer id = wmMap.get(pair).random();
                return ticketMap.getOrDefault(id, null);
            }
        }
        return null;
    }

    public Active12TicketTemplateImpl getTicketCfg(int id) {
        return ticketMap.getOrDefault(id, null);
    }

    /**
     * 获取保底次数列表
     * [Active12TicketTemplateImpl::getId : base]
     *
     * @param serverLv
     * @return
     */
    public Map<Integer, Integer> getBaseMap(int serverLv) {
        Map<Integer, Integer> map = ticketMap.values().stream()
                .filter(e -> e.getBase() > 0 && e.getSever_lv_down() <= serverLv && serverLv <= e.getSever_lv_up())
                .collect(Collectors.toMap(Active12TicketTemplate::getId, Active12TicketTemplate::getBase));
        return map;
    }

    /**
     * 获取每个计费点购买次数map
     *
     * @return
     */
    public Map<Integer, Integer> getBuyTimesMap() {
        return salesMap.values().stream().collect(Collectors.toMap(Active12SalesTemplate::getId, Active12SalesTemplate::getBuy_times));
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
        Active12RandomTemplate e = randomTable.get(nDays, nTimes);
        return RandomUtil.randomInt(e.getRandom_down(), e.getRandom_up() + 1);
    }

    @Override
    public void loadConfig() {
//        List<Active12TicketTemplateImpl> list = JSONUtil.fromJson(getJson(Active12TicketTemplateImpl.class), new TypeReference<List<Active12TicketTemplateImpl>>() {
//        });
//        Map<Integer, Active12TicketTemplateImpl> tm = Maps.newConcurrentMap();
//        list.forEach(e -> {
//            e.init();
//            tm.put(e.getId(), e);
//        });
//        ticketMap = ImmutableMap.copyOf(tm);
//
//        Map<Pair<Integer, Integer>, List<Active12TicketTemplateImpl>> map = list.stream().collect(Collectors.groupingBy(Active12TicketTemplateImpl::genGroupKey));
//        Map<Pair<Integer, Integer>, WeightMeta<Integer>> wmMapTemp = Maps.newConcurrentMap();
//        for (Map.Entry<Pair<Integer, Integer>, List<Active12TicketTemplateImpl>> entry : map.entrySet()) {
//            // id:weight
//            Map<Integer, Integer> wm = entry.getValue().stream().collect(Collectors.toMap(Active12TicketTemplate::getId, Active12TicketTemplate::getWeight));
//            WeightMeta<Integer> weightMeta = RandomUtils.buildWeightMeta(wm);
//            wmMapTemp.put(entry.getKey(), weightMeta);
//        }
//        wmMap = ImmutableMap.copyOf(wmMapTemp);
//
//        List<Active12SalesTemplateImpl> list1 = JSONUtil.fromJson(getJson(Active12SalesTemplateImpl.class), new TypeReference<List<Active12SalesTemplateImpl>>() {
//        });
//        Map<Integer, Active12SalesTemplateImpl> tm1 = Maps.newConcurrentMap();
//        list1.forEach(e -> {
//            e.init();
//            tm1.put(e.getId(), e);
//        });
//        salesMap = ImmutableMap.copyOf(tm1);
//
//        Table<Integer, Integer, Active12RandomTemplate> tbl = HashBasedTable.create();
//        List<Active12RandomTemplate> list2 = JSONUtil.fromJson(getJson(Active12RandomTemplate.class), new TypeReference<List<Active12RandomTemplate>>() {
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
        List<Active12TicketTemplateImpl> list = JSONUtil.fromJson(getJson(Active12TicketTemplateImpl.class), new TypeReference<List<Active12TicketTemplateImpl>>() {
        });
        Iterator<Active12TicketTemplateImpl> iterator = list.iterator();
        while (iterator.hasNext()) {
            Active12TicketTemplateImpl template = iterator.next();
            if (template.getLimit() > 0 && exclude.containsKey(template.getId())) {
                if (exclude.get(template.getId()) >= template.getLimit()) {
                    iterator.remove();
                }
            }
        }

        Map<Pair<Integer, Integer>, List<Active12TicketTemplateImpl>> map = list.stream().collect(Collectors.groupingBy(Active12TicketTemplateImpl::genGroupKey));
        Map<Pair<Integer, Integer>, WeightMeta<Integer>> wmMapTemp = Maps.newConcurrentMap();
        for (Map.Entry<Pair<Integer, Integer>, List<Active12TicketTemplateImpl>> entry : map.entrySet()) {
            // id:weight
            Map<Integer, Integer> wm = entry.getValue().stream().collect(Collectors.toMap(Active12TicketTemplate::getId, Active12TicketTemplate::getWeight));
            WeightMeta<Integer> weightMeta = RandomUtils.buildWeightMeta(wm);
            wmMapTemp.put(entry.getKey(), weightMeta);
        }
        wmMap = ImmutableMap.copyOf(wmMapTemp);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active12TicketTemplateImpl.class, Active12SalesTemplateImpl.class, Active12RandomTemplate.class);
    }
}
