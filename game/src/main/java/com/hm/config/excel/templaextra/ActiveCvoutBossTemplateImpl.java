package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveCvoutBossTemplate;

/**
 * @author wyp
 * @description
 * @date 2021/2/6 9:12
 */
@FileConfig("active_cvout_boss")
public class ActiveCvoutBossTemplateImpl extends ActiveCvoutBossTemplate {

    public boolean isFit(int mapId) {
        return getMap_id() == mapId;
    }
}
