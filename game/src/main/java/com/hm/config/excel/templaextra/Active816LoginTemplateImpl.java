package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active816LoginTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_816_login")
public class Active816LoginTemplateImpl extends Active816LoginTemplate {
    private List<Items> signItems;
    private List<Items> rewardItems;
    private List<Items> reward2Items;
    private List<Items> reward3Items;

    public void init() {
        signItems = ItemUtils.str2DefaultItemImmutableList(getReward_sign());
        rewardItems = ItemUtils.str2DefaultItemImmutableList(getReward_recharge());
        reward2Items = ItemUtils.str2DefaultItemImmutableList(getReward_recharge_2());
        reward3Items = ItemUtils.str2DefaultItemImmutableList(getReward_recharge_3());
    }

    public List<Items> getSignItems() {
        return signItems;
    }

    public List<Items> getRewardItems() {
        return rewardItems;
    }

    public List<Items> getReward2Items() {
        return reward2Items;
    }

    public List<Items> getReward3Items() {
        return reward3Items;
    }
}
