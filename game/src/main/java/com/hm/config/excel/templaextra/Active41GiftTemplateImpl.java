package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active41GiftTemplate;

/**
 * @author wyp
 * @description
 * @date 2022/3/21 10:34
 */
@FileConfig("active_41_gift")
public class Active41GiftTemplateImpl extends Active41GiftTemplate {

    public boolean isFit(int lv) {
        return lv >= getPlayer_lv_down() && lv <= getPlayer_lv_up();
    }
}
