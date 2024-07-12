package com.hmkf.config.template;

import com.hm.libcore.annotation.FileConfig;
import lombok.Data;

@Data
@FileConfig("kf_pk_reward_rank")
public class KfPkRewardRankTemplate {
    private Integer id;
    private Integer stage;
    private Integer rank_down;
    private Integer rank_up;
    private String reward;
    private String weekly_reward;
}
