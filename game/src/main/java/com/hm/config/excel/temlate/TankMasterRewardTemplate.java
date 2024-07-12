package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("tank_master_reward")
public class TankMasterRewardTemplate {
    private Integer id;
    private Integer star_scores_total;
    private String reward;
}
