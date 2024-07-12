package com.hm.model.guild.bean;

import com.hm.model.player.CentreArms;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class GuildPlayerVoWithCenterInfo extends GuildPlayerVo {
    @Setter
    private CentreArms arms;

    public GuildPlayerVoWithCenterInfo(GuildPlayer guildP) {
        super(guildP);
    }

    public static GuildPlayerVoWithCenterInfo buildVo(GuildPlayer guildP){
        return new GuildPlayerVoWithCenterInfo(guildP);
    }
}
