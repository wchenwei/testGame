package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ServiceMergeRankRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-04-29
 */
@FileConfig("service_merge_rank_reward")
public class ServiceMergeRankRewardTemplateImpl extends ServiceMergeRankRewardTemplate {
    private List<Items> rewardList = Lists.newArrayList();
    public void init() {
        rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
    
    public boolean isFitRank(int rank) {
    	return rank >= getRank1() && rank <= getRank2();
    }
}
