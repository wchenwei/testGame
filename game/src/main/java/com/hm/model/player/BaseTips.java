package com.hm.model.player;

import com.hm.enums.TipsType;

public class BaseTips {
    private int type;

    public BaseTips(TipsType type) {
        this.type = type.getType();
    }
}
