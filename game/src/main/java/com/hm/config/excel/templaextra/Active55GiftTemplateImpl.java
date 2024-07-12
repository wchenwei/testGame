package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active55GiftTemplate;

/**
 * @ClassName Active55GiftTemplateImpl
 * @Deacription 55计费礼包
 * @Author zxj
 * @Date 2021/12/3 10:05
 * @Version 1.0
 **/
@FileConfig("active_55_gift")
public class Active55GiftTemplateImpl extends Active55GiftTemplate {
    public boolean isFit(int lv) {
        return lv>=getPlayer_lv_down()&&lv<=getPlayer_lv_up();
    }
}
