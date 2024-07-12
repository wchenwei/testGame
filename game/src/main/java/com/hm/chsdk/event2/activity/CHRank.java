package com.hm.chsdk.event2.activity;

import lombok.NoArgsConstructor;
import redis.clients.jedis.Tuple;

@NoArgsConstructor
public class CHRank {
    private int id;
    private int rank;
    private long score;

    public CHRank(Tuple tuple, int rank) {
        this.id = Integer.parseInt(tuple.getElement());
        this.score = (long) tuple.getScore();
        this.rank = rank;
    }
}
