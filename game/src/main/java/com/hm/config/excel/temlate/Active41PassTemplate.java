package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@FileConfig("active_41_pass")
@Data
public class Active41PassTemplate {
    private Integer id;
    private Integer stage;
    private Integer pass_level;
    private Integer player_lv_down;
    private Integer player_lv_up;
    private String reward_free;
    private String reward_trump;
    private String reward_legend;
}
