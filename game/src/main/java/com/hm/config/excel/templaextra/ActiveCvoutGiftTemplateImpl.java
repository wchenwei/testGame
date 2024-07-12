package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveCvoutGiftTemplate;

/**
 * @author wyp
 * @description
 * @date 2021/2/5 13:48
 */
@FileConfig("active_cvout_gift")
public class ActiveCvoutGiftTemplateImpl extends ActiveCvoutGiftTemplate {

    public boolean isFit(int lv) {
        return lv>=getPlayer_lv_down()&&lv<=getPlayer_lv_up();
    }
}
