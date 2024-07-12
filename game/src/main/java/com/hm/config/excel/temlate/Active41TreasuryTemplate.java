package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_41_treasury")
@Data
public class Active41TreasuryTemplate {
    private Integer id;
    private Integer stage;
    private Integer progress;
    private Integer lv_down;
    private Integer lv_up;
    private Integer Box_order;
    private String Box_reward;
    private String bigbox_reward;
    private String consume;
}
