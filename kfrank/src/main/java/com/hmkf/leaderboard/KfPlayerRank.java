package com.hmkf.leaderboard;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KfPlayerRank {
    private long id;
    private int rank;

    public KfPlayerRank(long id, int rank) {
        super();
        this.id = id;
        this.rank = rank;
    }
}
