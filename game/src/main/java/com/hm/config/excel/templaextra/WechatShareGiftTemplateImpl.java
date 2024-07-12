package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.WechatShareGiftTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.PubFunc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wyp
 * @description
 * @date 2021/1/9 16:23
 */
@FileConfig("wechat_share_gift")
public class WechatShareGiftTemplateImpl extends WechatShareGiftTemplate {

    private List<Items> rewardList;

    private List<Integer> ids;

    public void init() {
        this.rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
        this.ids = Arrays.asList(getCount_zone().split(","))
                .stream()
                .map(str -> PubFunc.parseInt(str))
                .collect(Collectors.toList());
    }

    public List<Integer> getIds() {
        return ids;
    }

    public List<Items> getRewardList() {
        return rewardList;
    }
}
