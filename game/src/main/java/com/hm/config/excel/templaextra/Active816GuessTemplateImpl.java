package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active816GuessTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Getter;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/8/4 14:13
 */
@FileConfig("active_816_guess")
public class Active816GuessTemplateImpl extends Active816GuessTemplate {
    @Getter
    private List<Items> rewards = Lists.newArrayList();

    public void init() {
        this.rewards = ItemUtils.str2DefaultItemImmutableList(this.getReward());
    }

    public boolean isFit(int playerLv, int stage) {
        if(stage != this.getStage()){
            return false;
        }
        return playerLv >= getPlayer_lv_down() && playerLv <= getPlayer_lv_up();
    }
}
