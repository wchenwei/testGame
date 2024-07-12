package com.hmkf.model.kfwarrecord;

import com.hmkf.model.IPKPlayer;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
public class SPInfoTemp {
    private long score;//变化之前的积分
    private long combat;//总战力
    private long addScore;
    private int troopSize;

    public SPInfoTemp(IPKPlayer player, int troopSize) {
        this.score = player.getScore();
        this.troopSize = troopSize;
        this.combat = player.getCombat();
    }
}
