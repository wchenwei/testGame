package com.hm.action.guild.vo;

import com.hm.config.excel.GuildConfig;
import com.hm.enums.WorldType;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.guild.Guild;
import com.hm.servercontainer.world.WorldServerContainer;

public class GuildDetailVo {
    private int guildId;
    private String guildName;
    private String flagName;
    private int count;
    private int maxCount;
    private int lv;
    private long combat;
    private long cityCount;//城池数量
    private String leaderName;
    private long leaderId;

    public GuildDetailVo(Guild guild) {
        this.guildId = guild.getId();
        this.guildName = guild.getGuildInfo().getGuildName();
        this.count = guild.getGuildMembers().getNum();
        this.lv = guild.guildLevelInfo().getLv();
        this.combat = guild.getGuildMembers().getTotalCombat();
        this.flagName = guild.getGuildFlag().getFlagName();

        this.leaderId = guild.getGuildInfo().getLeaderId();
        this.leaderName = guild.getGuildInfo().getLeaderName();
        this.cityCount = WorldServerContainer.of(guild).getWorldCitys(WorldType.Normal)
                .stream().filter(e -> e.getBelongGuildId() == guildId).count();
        GuildConfig guildConfig = SpringUtil.getBean(GuildConfig.class);
        this.maxCount = guildConfig.getGuildMemberNum(this.lv);
    }

    public static GuildDetailVo buildVo(Guild guild){
        return new GuildDetailVo(guild);
    }
}
