package com.hmkf.config.template;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("kf_pk_score_base")
public class KfPkScoreBaseTemplate {
    private Integer id;
    private Integer lv_down;
    private Integer lv_up;
    private Integer score_base;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLv_down() {
        return lv_down;
    }

    public void setLv_down(Integer lv_down) {
        this.lv_down = lv_down;
    }

    public Integer getLv_up() {
        return lv_up;
    }

    public void setLv_up(Integer lv_up) {
        this.lv_up = lv_up;
    }

    public Integer getScore_base() {
        return score_base;
    }

    public void setScore_base(Integer score_base) {
        this.score_base = score_base;
    }
}
