package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.Active119GiftTemplate;

/**
 * @author wyp
 * @description
 * @date 2022/8/11 9:33
 */
@FileConfig("active_119_gift")
public class Active119GiftTemplateImpl extends Active119GiftTemplate {

    public boolean isFit(int lv){
        return lv>= this.getPlayer_lv_down()&&lv<=this.getPlayer_lv_up();
    }
}
