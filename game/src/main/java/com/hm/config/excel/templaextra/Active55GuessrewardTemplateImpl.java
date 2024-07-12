package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active55GuessrewardTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;
import lombok.Getter;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/8/4 14:16
 */
@FileConfig("active_55_guessreward")
public class Active55GuessrewardTemplateImpl extends Active55GuessrewardTemplate {
    @Getter
    private List<Items> rewards = Lists.newArrayList();

    private List<Integer> ruleList = Lists.newArrayList();

    public void init() {
        this.rewards = ItemUtils.str2DefaultItemImmutableList(this.getReward());
        ruleList = StringUtil.splitStr2IntegerList(this.getRule(), ",");
    }

    public boolean isFitRule(int playerLv, int successNum, int stage){
        if(stage != this.getStage() || !isFit(playerLv)){
            return false;
        }
        return this.ruleList.contains(successNum);
    }

    private boolean isFit(int playerLv) {
        return playerLv >= getPlayer_lv_down() && playerLv <= getPlayer_lv_up();
    }
}
