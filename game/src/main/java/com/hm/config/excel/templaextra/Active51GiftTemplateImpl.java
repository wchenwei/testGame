package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active51GiftTemplate;

/**
 * @description: 计费点
 * @author: chenwei
 * @create: 2020-04-02 15:58
 **/
@FileConfig("active_51_gift")
public class Active51GiftTemplateImpl extends Active51GiftTemplate {

    public boolean isFit(int lv) {
        return lv>=getPlayer_lv_down()&&lv<=getPlayer_lv_up();
    }
}
