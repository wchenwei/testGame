package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("ad_reward")
public class AdRewardTemplate {
    private Integer id;
    private Integer type;
    private Integer max_count;
    private String param;
    private Integer cd_time;
}
