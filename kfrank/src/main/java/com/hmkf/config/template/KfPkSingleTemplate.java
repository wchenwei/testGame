package com.hmkf.config.template;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("kf_pk_single")
public class KfPkSingleTemplate {
    private Integer id;
    private String name;
    private Integer num;
    private Integer level_up;
    private Integer level_down;

    private String reward_win;
    private String reward_fail;
}
