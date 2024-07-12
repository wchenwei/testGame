package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active0801SpeaksignTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import lombok.Data;

import java.util.List;

/**
 * @author wyp
 * @description
 * @date 2022/6/21 16:43
 */

@FileConfig("active_0801_speaksign")
@Data
public class Active0801SpeaksignTemplateImpl extends Active0801SpeaksignTemplate {
    private List<Items> rewards = Lists.newArrayList();
    public void init(){
        this.rewards = ItemUtils.str2ItemList(this.getSpeak_sign(), ",", ":");
    }

    public boolean checkPlayerLv(int lv) {
        return lv>=this.getLv_down() && lv<= this.getLv_up();
    }
}

