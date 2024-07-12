package com.hm.action.http;

import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.action.login.biz.LoginBiz;
import com.hm.action.sys.SysFacade;
import com.hm.cache.PlayerCacheManager;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.LeaveOnlineType;
import com.hm.model.player.Player;
import com.hm.libcore.mongodb.MongoUtils;
import com.hm.libcore.mongodb.MongodDB;
import com.hm.util.PubFunc;
import com.hm.util.StringUtil;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.hm.libcore.annotation.Action;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("player.del")
public class PlayerAction {
    @Resource
    private SysFacade sysFacade;
    @Resource
    private LoginBiz loginBiz;


    public void server(HttpSession session) {
        int serverId = PubFunc.parseInt(session.getParams("sid"));
        if(!ServerConfig.getInstance().getUseGmOrder(serverId)){//能否使用Gm命令
            session.Write("当前服务器无法清档");
            return;
        }
        MongodDB mongodDB = MongoUtils.getMongodDB(serverId);
        if (mongodDB == null) {
            session.Write("serverId not find");
            return;
        }
        List<Player> players = PlayerContainer.getOnlinePlayersByServerId(serverId);
        for (Player player : players) {
            sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
        }
        PlayerCacheManager.getInstance().clearAllCache();

        MongoCollection<Document> collPlayer = mongodDB.getMongoTemplate().getCollection("player");
        collPlayer.updateMany(new Document("uid", new Document("$ne", -1)), new Document("$set", new Document("uid", -1)));
        session.Write("1");
    }

    public void player(HttpSession session) {
        for (Integer pid : StringUtil.splitStr2IntegerList(session.getParams("pid"),",")) {
            doClearPlayer(pid);
        }
        session.Write("成功");
    }

    private void doClearPlayer(long playerId) {
        Player player = PlayerUtils.getPlayer(playerId);
        if (player == null) {
            return;
        }
        if(!ServerConfig.getInstance().getUseGmOrder(player.getServerId())){//能否使用Gm命令
            return;
        }
        player.setUid(-1);
        player.saveDB();
        PlayerCacheManager.getInstance().removePlayerCache(playerId);
        if (player.isOnline()) {
            sysFacade.sendLeavePlayer(player, LeaveOnlineType.SERVER);
        }
    }
}
