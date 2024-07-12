package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.AgentFeedbackTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-07-08
 */
@FileConfig("agent_feedback")
public class AgentFeedbackTemplateImpl extends AgentFeedbackTemplate {
    private List<Items> rewardItems = Lists.newArrayList();

    public void init() {
        this.rewardItems = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardItems() {
        return rewardItems;
    }
}
