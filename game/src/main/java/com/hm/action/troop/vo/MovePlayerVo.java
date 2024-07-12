package com.hm.action.troop.vo;

import com.hm.model.guild.Guild;
import com.hm.model.player.BasePlayer;
import com.hm.servercontainer.guild.GuildContainer;

public class MovePlayerVo {
    private long playerId;
    public String name;
    public int lv;
    public String icon;
    public int frameIcon;
    public int titleId;
    public int guildId;
    public String flag;


    public void load(BasePlayer player) {
        this.playerId = player.getId();
        this.lv = player.playerLevel().getLv();
        this.icon = player.playerHead().getIcon();
        this.name = player.getName();
        this.frameIcon = player.playerHead().getFrameIcon();
        this.titleId = player.playerTitle().getUsingTitleId();
        this.guildId = player.playerGuild().getGuildId();

        if(this.guildId > 0) {
            Guild guild = GuildContainer.of(player.getServerId()).getGuild(this.guildId);
            if(guild != null) {
                this.flag = guild.getGuildFlag().getFlagName();
            }
        }
    }
}
