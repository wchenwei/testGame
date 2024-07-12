package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("mission_box")
public class MissionBoxTemplate {
    protected int id;
    protected int reward_group;
    protected String reward;
    protected int mission_min;
    protected int mission_max;
    protected int cd;
}
