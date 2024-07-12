package com.hmkf.guild;

import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.guild.GuildItemContainer;
import com.hmkf.db.KfPlayerUtils;

public class KfGuildUtils {
    public static Guild getGuild(Player player) {
        int guildId = player.getGuildId();
        if (guildId <= 0) {
            return null;
        }
        GuildItemContainer guildItemContainer = GuildContainer.of(player);
        if (guildItemContainer == null) {
            guildItemContainer = new GuildItemContainer(player.getServerId());
            GuildContainer.getServerMap().putItem(player.getServerId(), guildItemContainer);
        }
        Guild guild = guildItemContainer.getGuild(guildId);
        if (guild == null) {
            guild = KfPlayerUtils.getGuildFromDB(player);
            if (guild != null) {
                guildItemContainer.addGuild(guild);
            }
        }
        return guild;
    }

    public static void clearServerGuild() {
        GuildContainer.getServerMap().clear();
    }
}
