package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveCvoutMapTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2021/2/5 16:22
 */
@FileConfig("active_cvout_map")
public class ActiveCvoutMapTemplateImpl extends ActiveCvoutMapTemplate {

    private List<Items> rewardList = Lists.newArrayList();

    private List<Items> formulaList = Lists.newArrayList();

    public void init() {
        rewardList = ItemUtils.str2ItemList(getReward(), ",", ":");
        formulaList = ItemUtils.str2ItemList(getFormula(), ",", ":");
    }

    public List<Items> getRewardList() {
        return rewardList;
    }

    public List<Items> getFormulaList() {
        return formulaList;
    }
}
