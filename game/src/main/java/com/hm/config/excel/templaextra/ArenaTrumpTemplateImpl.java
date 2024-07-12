package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ArenaTrumpTemplate;
import com.hm.enums.BuffType;
import com.hm.model.buff.Buff;
import com.hm.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-08
 */
@FileConfig("arena_trump")
public class ArenaTrumpTemplateImpl extends ArenaTrumpTemplate {
    /**
     * 禁止阵营
     */
    private String prohibitCamp;
    /**
     * 禁止兵种
     */
    private String prohibitArms;

    /**
     * 前几名buff奖励
     * 名次:buff;名次--1,2,3,4
     */
    private Map<Integer, Buff> rewardBuff = Maps.newConcurrentMap();

    public void init() {
        if (StrUtil.isNotEmpty(this.getLimit())) {
            this.prohibitCamp = this.getLimit().split(";")[1];
            this.prohibitArms = this.getLimit().split(";")[0];
        }

        // 1:0.3,1:0.2,1:0.1
        List<String> strings = StringUtil.splitStr2StrList(this.getBuff(), ",");
        int i = 1;
        for (String string : strings) {
            String[] split = string.split(":");
            int buffType = Integer.parseInt(split[0]);
            double value = Double.parseDouble(split[1]);
            Buff buff = new Buff(BuffType.getType(buffType));
            buff.setValue(value);

            rewardBuff.put(i++, buff);
        }
    }

    public String getProhibitCamp() {
        return prohibitCamp;
    }

    public String getProhibitArms() {
        return prohibitArms;
    }

    /**
     * 获取名次对应的buff
     *
     * @param rank
     * @return
     */
    public Buff getBuff(int rank) {
        return rewardBuff.getOrDefault(rank, null);
    }

    /**
     * 获取需要发buff奖励的人数
     *
     * @return
     */
    public int getRewardCount() {
        return rewardBuff.size();
    }

    public Map<Integer, Buff> getRewardBuff() {
        return rewardBuff;
    }
}
