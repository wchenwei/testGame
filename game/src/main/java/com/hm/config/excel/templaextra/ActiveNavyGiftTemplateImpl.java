package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveNavyGiftTemplate;

/**
 * @description: 海军节
 * @author: chenwei
 * @create: 2020-03-30 15:07
 **/
@FileConfig("active_navy_gift")
public class ActiveNavyGiftTemplateImpl extends ActiveNavyGiftTemplate {


    public boolean isFit(int lv) {
        return lv>=getPlayer_lv_down()&&lv<=getPlayer_lv_up();
    }
}
