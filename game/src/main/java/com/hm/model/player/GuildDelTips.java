package com.hm.model.player;

import com.hm.enums.TipsType;

public class GuildDelTips extends BaseTips{
    private String leader;
    private String guildName;

    public GuildDelTips(String leader, String guildName) {
        super(TipsType.KickGuild);
        this.leader = leader;
        this.guildName = guildName;
    }
}
