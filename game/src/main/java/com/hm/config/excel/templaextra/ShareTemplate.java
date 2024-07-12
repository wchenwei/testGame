package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ShareBaseTemplate;
import com.hm.enums.ShareType;
import lombok.Data;

@Data
@FileConfig("share_base")
public class ShareTemplate extends ShareBaseTemplate {
    private ShareType shareType;
    private boolean canReset;

    public void init() {
        this.shareType = ShareType.getType(getType());
        this.canReset = getDay_reset() == 1;
    }
}
