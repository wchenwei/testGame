package com.hm.action.kfgame;

import com.hm.enums.TroopState;
import com.hm.enums.WorldType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.serverConfig.GameServerMachine;
import com.hm.libcore.serverConfig.ServerInfoCache;
import com.hm.message.InnerMessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.player.KFPServerUrl;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.rpc.GameRpcManager;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.GameIpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DKFServerBiz {
    /**
     * @author siyunlong
     * @version V1.0
     * @Description: 进入跨服服务器
     * @date 2024/6/21 9:31
     */
    public void intoKFWorld(Player player,int kfServerId) {
        if(GameServerManager.getInstance().isDbServer(kfServerId)) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        Guild guild = GuildContainer.of(player).getGuild(player.getGuildId());
        if(guild == null) {
            log.error(player.getId()+" 没有部落");
            player.sendErrorMsg(SysConstant.Guild_Noin);
            return;
        }
        List<WorldTroop> troopList = TroopServerContainer.of(player.getServerId())
                .getWorldTroopByPlayer(player).stream().filter(e -> TroopState.isIdleState(e.getState()))
                .collect(Collectors.toList());
        for (WorldTroop worldTroop : troopList) {
            worldTroop.setWorldType(WorldType.KF);
        }
        String kfurl = getServerRpcUrl(kfServerId);
        String nowUrl = getServerRpcUrl(player.getServerId());


        JsonMsg jsonMsg = JsonMsg.create(InnerMessageComm.G2G_EnterKFGame);
        jsonMsg.addProperty("playerId",player.getId());
        jsonMsg.addProperty("troopList",troopList);
        jsonMsg.addProperty("serverId",kfServerId);
        jsonMsg.addProperty("guild",guild);
        jsonMsg.addProperty("serverUrl",nowUrl);

        KFPServerUrl kfpServerUrl = new KFPServerUrl(kfServerId,kfurl,0);
        player.playerTemp().setKfpServerUrl(kfpServerUrl);

        GameRpcManager.sendInnerMsg(kfurl,jsonMsg);
    }

    public String getServerRpcUrl(int serverId) {
        GameServerMachine serverMachine = ServerInfoCache.getInstance().getServerInfo(serverId).getServerMachine();
        return GameIpUtils.getGameRpcUrl(serverMachine);
    }
}
