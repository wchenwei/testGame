package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveDrawPaperTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

@FileConfig("active_draw_paper")
public class ActiveDrawPaperTemplateImpl extends ActiveDrawPaperTemplate{
	private List<Items> rewardList = Lists.newArrayList();
	
	public void init() {
        rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
    }
    public List<Items> getRewardList() {
        return rewardList;
    }
}
