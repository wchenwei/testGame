package com.hmkf.model;

import lombok.Data;

@Data
public class PlayerValue {
    private long playerId;
    private int[] value = new int[2];
    private long score;

    public PlayerValue(long playerId, int type, int value, long score) {
        this.playerId = playerId;
        this.value[type] = value;
        this.score = score;
    }

    public void addValue(int type, int value, long score) {
        this.value[type] += value;
        this.score += score;
    }

    public void clear() {
        this.score = 0;
        for (int i = 0; i < value.length; i++) {
            value[i] = 0;
        }
    }
}
