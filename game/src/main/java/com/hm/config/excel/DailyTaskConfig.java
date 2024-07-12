package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.DailyTaskConfigTemplateImpl;
import com.hm.config.excel.temlate.DailyTaskWeekLevelTemplate;
import com.hm.config.excel.temlate.DailyTaskWeekRechargeTemplate;
import com.hm.config.excel.temlate.TaskBoxTemplate;
import com.hm.config.excel.templaextra.DailyTaskWeekRewardTemplateImpl;
import com.hm.config.excel.templaextra.TaskBoxTemplateImpl;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.google.common.collect.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-08
 */
@Config
public class DailyTaskConfig extends ExcleConfig {
    // id:config
    private Map<Integer, DailyTaskConfigTemplateImpl> dailyTaskConfigTemplateMap = Maps.newConcurrentMap();
    // index:object
    private Map<Integer, TaskBoxTemplateImpl> boxMap = Maps.newConcurrentMap();
    private Set<Integer> boxIdSet = Sets.newConcurrentHashSet();

    private ListMultimap<Integer, DailyTaskConfigTemplateImpl> activityTaskMap = ArrayListMultimap.create();

    /**
     * DailyTaskWeekLevelTemplate::getId : DailyTaskWeekLevelTemplate
     */
    private Map<Integer, DailyTaskWeekLevelTemplate> weekLevelTemplateMap = Maps.newConcurrentMap();

    /**
     * DailyTaskWeekRechargeTemplate::getRecharge_gift_id : DailyTaskWeekRechargeTemplate
     */
    private Map<Integer, DailyTaskWeekRechargeTemplate> weekRechargeTemplateMap = Maps.newConcurrentMap();
    /**
     * DailyTaskWeekRewardTemplateImpl::getId : DailyTaskWeekRewardTemplateImpl
     */
    private Map<Integer, DailyTaskWeekRewardTemplateImpl> weekRewardTemplateMap = Maps.newConcurrentMap();

    public DailyTaskConfigTemplateImpl getDailyTaskCfg(int id) {
        return dailyTaskConfigTemplateMap.getOrDefault(id, null);
    }

    @Override
    public void loadConfig() {
        ArrayListMultimap<Integer, DailyTaskConfigTemplateImpl> activityTaskMap = ArrayListMultimap.create();
        List<DailyTaskConfigTemplateImpl> list = loadDailyTaskConfigTemplate();
        list.forEach(DailyTaskConfigTemplateImpl::init);
        list.forEach(e -> activityTaskMap.put(e.getActive_id(), e));
        this.activityTaskMap = ImmutableListMultimap.copyOf(activityTaskMap);

        Map<Integer, DailyTaskConfigTemplateImpl> map = list.stream().collect(
                Collectors.toMap(DailyTaskConfigTemplateImpl::getId, Function.identity()));
        dailyTaskConfigTemplateMap = ImmutableMap.copyOf(map);

        List<TaskBoxTemplateImpl> boxList = loadTaskBoxTemplateImpl();
        boxList.forEach(TaskBoxTemplateImpl::init);
        Map<Integer, TaskBoxTemplateImpl> tmpBoxMap = boxList.stream().collect(
                Collectors.toMap(TaskBoxTemplateImpl::getIndex, Function.identity()));
        boxMap = ImmutableMap.copyOf(tmpBoxMap);
        boxIdSet = boxMap.values().stream().map(TaskBoxTemplateImpl::getPos).collect(Collectors.toSet());


        List<DailyTaskWeekLevelTemplate> list1 = JSONUtil.fromJson(getJson(DailyTaskWeekLevelTemplate.class), new TypeReference<List<DailyTaskWeekLevelTemplate>>() {
        });
        Map<Integer, DailyTaskWeekLevelTemplate> m = list1.stream().collect(Collectors.toMap(DailyTaskWeekLevelTemplate::getId, Function.identity()));

        weekLevelTemplateMap = ImmutableMap.copyOf(m);

        List<DailyTaskWeekRechargeTemplate> list2 = JSONUtil.fromJson(getJson(DailyTaskWeekRechargeTemplate.class), new TypeReference<List<DailyTaskWeekRechargeTemplate>>() {
        });
        Map<Integer, DailyTaskWeekRechargeTemplate> m2 = list2.stream().collect(Collectors.toMap(DailyTaskWeekRechargeTemplate::getRecharge_gift_id, Function.identity()));

        weekRechargeTemplateMap = ImmutableMap.copyOf(m2);

        List<DailyTaskWeekRewardTemplateImpl> list3 = JSONUtil.fromJson(getJson(DailyTaskWeekRewardTemplateImpl.class), new TypeReference<List<DailyTaskWeekRewardTemplateImpl>>() {
        });
        Map<Integer, DailyTaskWeekRewardTemplateImpl> m3 = Maps.newConcurrentMap();
        list3.forEach(e -> {
            e.init();
            m3.put(e.getId(), e);
        });

        weekRewardTemplateMap = ImmutableMap.copyOf(m3);
    }

    private List<DailyTaskConfigTemplateImpl> loadDailyTaskConfigTemplate() {
        return JSONUtil.fromJson(getJson(DailyTaskConfigTemplateImpl.class), new TypeReference<List<DailyTaskConfigTemplateImpl>>() {
        });
    }

    private List<TaskBoxTemplateImpl> loadTaskBoxTemplateImpl() {
        return JSONUtil.fromJson(getJson(TaskBoxTemplateImpl.class), new TypeReference<List<TaskBoxTemplateImpl>>() {
        });
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(DailyTaskConfigTemplateImpl.class, TaskBoxTemplateImpl.class, DailyTaskWeekLevelTemplate.class,
                DailyTaskWeekRechargeTemplate.class, DailyTaskWeekRewardTemplateImpl.class
        );
    }

    public boolean isTaskUnlock(BasePlayer player, int taskId) {
        if (dailyTaskConfigTemplateMap.containsKey(taskId)) {
            DailyTaskConfigTemplateImpl dailyTaskConfigTemplate = dailyTaskConfigTemplateMap.get(taskId);
            return dailyTaskConfigTemplate.isFitLv(player.playerCommander().getMilitaryLv());
        }
        return false;
    }

    public Collection<Integer> getAllTaskId() {
        return dailyTaskConfigTemplateMap.keySet();
    }

    /**
     * 获取对应位置宝箱的奖励内容
     *
     * @param level 玩家等级
     * @param pos
     * @return
     */
    public List<Items> getReward(int level, int pos) {
        TaskBoxTemplateImpl cfg = getTaskBoxTemplateImpl(level, pos);
        if (cfg != null) {
            return cfg.getRewardList();
        }
        return Lists.newArrayList();
    }

    public TaskBoxTemplateImpl getTaskBoxTemplateImpl(int level, int pos) {
        return boxMap.values().stream().filter(b -> b.getPos().equals(pos)).filter(b -> b.getLevel() <= level).
                max(Comparator.comparing(TaskBoxTemplate::getLevel)).orElse(null);
    }

    public Set<Integer> getBoxIdSet() {
        return boxIdSet;
    }

    public ListMultimap<Integer, DailyTaskConfigTemplateImpl> getActivityTaskMap() {
        return activityTaskMap;
    }


    /**
     * 该point对应解锁的最大活跃等级
     *
     * @param point
     * @return
     */
    public int getWeekLv(int point) {
        Optional<DailyTaskWeekLevelTemplate> max = weekLevelTemplateMap.values().stream().filter(e -> e.getActive_point() <= point).
                max(Comparator.comparing(DailyTaskWeekLevelTemplate::getActive_point));
        return max.isPresent() ? max.get().getId() : 0;
    }

    /**
     * @param wkPoint 任务点数
     * @param lv      玩家等级
     * @return
     */
    public List<DailyTaskWeekRewardTemplateImpl> getRewardList(int wkPoint, int lv) {
        int weekLv = getWeekLv(wkPoint);
        return weekRewardTemplateMap.values().stream().
                filter(e -> e.getWeek_actvie_lv() <= weekLv && e.getPlayer_lv_down() <= lv && lv <= e.getPlayer_lv_up())
                .collect(Collectors.toList());
    }

    public DailyTaskWeekRewardTemplateImpl getWeekRewardCfg(int id) {
        return weekRewardTemplateMap.getOrDefault(id, null);
    }

    public DailyTaskWeekRewardTemplateImpl getWeekRewardCfg(int playerLv, int adId) {
        Optional<DailyTaskWeekRewardTemplateImpl> any = weekRewardTemplateMap.values().stream().filter(e -> e.getAd_id().equals(adId) && e.getPlayer_lv_down() <= playerLv && playerLv <= e.getPlayer_lv_up()).findAny();
        return any.orElse(null);
    }

    public DailyTaskWeekRechargeTemplate getRechargeCfg(int getRecharge_gift_id) {
        return weekRechargeTemplateMap.getOrDefault(getRecharge_gift_id, null);
    }
}
