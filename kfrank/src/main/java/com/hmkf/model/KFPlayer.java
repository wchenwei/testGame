package com.hmkf.model;

import org.springframework.data.annotation.Transient;

public class KFPlayer extends KFBasePlayer{
    @Transient
    private transient KfPlayerTemp playerTemp = new KfPlayerTemp();

    public KfPlayerTemp getPlayerTemp() {
        return playerTemp;
    }

    @Override
    public long getCombat() {
        return this.playerTemp.getCombat();
    }
}
