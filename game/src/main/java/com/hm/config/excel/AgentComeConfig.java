package com.hm.config.excel;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.ActiveAgentComeTemplateImpl;
import com.hm.enums.ItemType;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import com.hm.util.WeightMeta;

import java.util.*;
import com.google.common.collect.*;
/**
 * Description:
 * User: yang xb
 * Date: 2019-08-01
 */
@Config
public class AgentComeConfig extends ExcleConfig {
    // 期数,position:config obj
    private Table<Integer, Integer, ActiveAgentComeTemplateImpl> rewardTable = HashBasedTable.create();
    // 期数:weight
    // 普通奖励
    private Map<Integer, WeightMeta<Integer>> normalWM = Maps.newHashMap();
    // 幸运奖励
    private Map<Integer, WeightMeta<Integer>> luckyWM = Maps.newHashMap();

    public ActiveAgentComeTemplateImpl getRandReward(int stage, boolean isNormal) {
        if (!rewardTable.containsRow(stage)) {
            return null;
        }

        WeightMeta<Integer> meta = isNormal ? normalWM.get(stage) : luckyWM.get(stage);

        Integer randomPos = meta.random();
        if (randomPos == null) {
            return null;
        }

        return rewardTable.get(stage, randomPos);
    }

    public int gatAgentId(int stage) {
        ActiveAgentComeTemplateImpl activeAgentComeTemplate = rewardTable.row(stage).values().stream().filter(e -> StrUtil.isNotBlank(e.getMain_reward())).findFirst().orElse(null);
        if (activeAgentComeTemplate == null) {
            return 0;
        }
        String mainReward = activeAgentComeTemplate.getMain_reward();
        int[] ints = StringUtil.strToIntArray(mainReward, ":");
        if (ints.length <= 1) {
            return 0;
        }
        return ints[1];
    }

    /**
     * 获取这些期活动奖励内容里出现的所有特工id list
     *
     * @param stageList
     * @return
     */
    public Collection<Integer> getAgentIdList(Collection<Integer> stageList) {
        Set<Integer> hashSet = new HashSet<>();
        for (Integer stage : stageList) {
            Collection<ActiveAgentComeTemplateImpl> values = rewardTable.row(stage).values();
            for (ActiveAgentComeTemplateImpl e : values) {
                if (StrUtil.isEmpty(e.getMain_reward())) {
                    continue;
                }
                String[] split = e.getMain_reward().split(":");
                if (split.length == 2 && split[0].equals(String.valueOf(ItemType.AGENT.getType()))) {
                    hashSet.add(Integer.parseInt(split[1]));
                }
            }
        }

        return hashSet;
    }

    @Override
    public void loadConfig() {
       /* List<ActiveAgentComeTemplateImpl> list = JSONUtil.fromJson(getJson(ActiveAgentComeTemplateImpl.class), new TypeReference<List<ActiveAgentComeTemplateImpl>>() {
        });
        // 期数,position:config obj
        Table<Integer, Integer, ActiveAgentComeTemplateImpl> tempTable = HashBasedTable.create();
        list.forEach(template -> {
            template.init();
            tempTable.put(template.getStage(), template.getPos(), template);
        });
        rewardTable = ImmutableTable.copyOf(tempTable);

        // 构造随机WeightMeta obj
        Map<Integer, WeightMeta<Integer>> normal = Maps.newHashMap();
        Map<Integer, WeightMeta<Integer>> lucky = Maps.newHashMap();
        tempTable.rowMap().forEach((stage, value) -> {
            Map<Integer, Integer> rate = Maps.newHashMap();
            Map<Integer, Integer> luck = Maps.newHashMap();

            value.forEach((position, rewardTemplate) -> {
                if (rewardTemplate.getRate() > 0) {
                    rate.put(position, rewardTemplate.getRate());
                }
                if (rewardTemplate.getLucky_rate() > 0) {
                    luck.put(position, rewardTemplate.getLucky_rate());
                }
            });
            normal.put(stage, RandomUtils.buildWeightMeta(rate));
            lucky.put(stage, RandomUtils.buildWeightMeta(luck));
        });
        normalWM = ImmutableMap.copyOf(normal);
        luckyWM = ImmutableMap.copyOf(lucky);*/
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveAgentComeTemplateImpl.class);
    }
}
