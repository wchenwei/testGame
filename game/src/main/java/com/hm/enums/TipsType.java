package com.hm.enums;

import com.hm.model.player.BaseTips;
import com.hm.model.player.GuildDelTips;

public enum TipsType {
    KickGuild(1,"踢出部落"){
        @Override
        public BaseTips createTips(Object... params) {
            return new GuildDelTips((String) params[0], (String) params[1]);
        }
    },

    ;

    private int type;
    private String desc;

    TipsType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public abstract BaseTips createTips(Object... params);
}
