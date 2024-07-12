package com.hm.action.kfgame;

import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.inner.InnerAction;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.InnerMessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.player.KFPServerUrl;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.rpc.RpcSession;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.guild.GuildItemContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Action
public class InnerKFGameAction extends InnerAction {

    @MsgMethod(InnerMessageComm.G2G_EnterKFGame)
    public void enterKFGame(JsonMsg msg) {
        long playerId = msg.getLong("playerId");
        int serverId = msg.getInt("serverId");
        String serverUrl = msg.getString("serverUrl");
        Guild guild = msg.getObject("guild");

        log.error(playerId+" start into kf:"+serverId);
        Player player = PlayerUtils.getPlayer(playerId);
        int fromServerId = player.getServerId();

        GuildItemContainer guildItemContainer = GuildContainer.of(fromServerId);
        if(guildItemContainer == null) {
            guildItemContainer = new GuildItemContainer(fromServerId);
            GuildContainer.getServerMap().putItem(fromServerId,guildItemContainer);
        }
        guildItemContainer.addGuild(guild);
        GuildContainer.of(serverId).addKFGuild(guild);

        KFPServerUrl kfpServerUrl = new KFPServerUrl(serverId,serverUrl,1);


        List<WorldTroop> troopList = msg.getObject("troopList");
        for (WorldTroop worldTroop : troopList) {
            if(TroopServerContainer.of(serverId).getWorldTroop(worldTroop.getId()) != null) {
                continue;
            }
            worldTroop.getTroopTemp().setKfpServerUrl(kfpServerUrl);
            TroopServerContainer.of(serverId).addWorldTroop(worldTroop);
        }

        player.playerTemp().setKfpServerUrl(kfpServerUrl);

        player.setSession(new RpcSession(serverUrl,playerId));
        PlayerContainer.addPlayer2Map(player);

        log.error(playerId+" end into kf:"+serverId);
    }


}
