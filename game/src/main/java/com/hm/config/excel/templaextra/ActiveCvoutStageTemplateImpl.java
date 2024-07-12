package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveCvoutStageTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.List;
import java.util.Map;

/**
 * @author wyp
 * @description
 * @date 2021/2/5 16:44
 */
@FileConfig("active_cvout_stage")
public class ActiveCvoutStageTemplateImpl extends ActiveCvoutStageTemplate {
    // 藏宝图全合成奖励
    private List<Items> rewardList = Lists.newArrayList();

    private List<Items> costList = Lists.newArrayList();
    // 重置消耗
    private List<Items> costReset = Lists.newArrayList();
    // 快速合成藏宝图 道具ID：实际出行消耗Id   active_cvout
    private Map<Integer, Integer> treasureSingleMap = Maps.newHashMap();

    public void init() {
        rewardList = ItemUtils.str2ItemList(getReward_final(), ",", ":");
        costList = ItemUtils.str2ItemList(getCost_id(), ",", ":");
        costReset = ItemUtils.str2ItemList(getCost_refresh(), ",", ":");
        treasureSingleMap = StringUtil.strToMap(this.getCost_piece(), "#", ",");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }

    public List<Items> getCostList() {
        return costList;
    }

    public List<Items> getCostReset() {
        return costReset;
    }

    public int getActiveCvout(int itemId) {
        return treasureSingleMap.get(itemId);
    }
}
