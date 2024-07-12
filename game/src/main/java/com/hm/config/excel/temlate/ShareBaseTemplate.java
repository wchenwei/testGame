package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("share_base")
public class ShareBaseTemplate {
    private Integer id;
    private Integer type;
    private Integer limit_num;
    private Integer count_need;
    private Integer share_cd;
    private String effect_value;
    private Integer day_reset;
}
