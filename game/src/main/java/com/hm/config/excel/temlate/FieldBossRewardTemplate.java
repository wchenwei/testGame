package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("field_boss_reward")
public class FieldBossRewardTemplate {
    private Integer id;
    private String drop_id;
    private String enemy_id;
}
