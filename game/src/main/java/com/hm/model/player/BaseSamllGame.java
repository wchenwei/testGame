package com.hm.model.player;

import lombok.Data;

/**
 * @author xjt
 * @version 1.0
 * @desc 表述
 * @date 2022/2/11 10:23
 */
@Data
public class BaseSamllGame {
    private int id;
    private int lv = 1;
    private long exp;

    private long score;//当天的分数

    public BaseSamllGame(int id) {
        this.setId(id);
    }

    public void addExp(long exp, long score) {
        this.exp += exp;
        this.score+=score;
    }

    public void dayReset() {
        this.score = 0;
    }
}
