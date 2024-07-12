package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WechatShareTimeRewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/1/11 17:38
 */
@FileConfig("wechat_share_time_reward")
public class WechatShareTimeImpl extends WechatShareTimeRewardTemplate {
    private List<Items> rewardList;

    public void init() {
        this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
