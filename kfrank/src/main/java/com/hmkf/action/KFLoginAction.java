package com.hmkf.action;

import com.hm.enums.ActionType;
import com.hm.enums.KfType;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.inner.InnerAction;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.rpc.GameRpcClientUtils;
import com.hm.log.LogBiz;
import com.hm.message.KFRankMessageComm;
import com.hm.message.MessageComm;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.util.ServerUtils;
import com.hmkf.action.level.KfLevelBiz;
import com.hmkf.action.level.KfTopBiz;
import com.hmkf.config.KFLevelConstants;
import com.hmkf.container.KFPlayerContainer;
import com.hmkf.db.KFPlayerDBCache;
import com.hmkf.db.KfDBUtils;
import com.hmkf.gametype.KfGroupContainer;
import com.hmkf.level.LevelGroupContainer;
import com.hmkf.level.LevelWorldGroup;
import com.hmkf.model.KFPlayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class KFLoginAction extends InnerAction {
    @Resource
    private KFPlayerContainer kFPlayerContainer;
    @Resource
    private KfGroupContainer groupContainer;
    @Resource
    private LevelGroupContainer levelGroupContainer;
    @Resource
    private KfLevelBiz kfLevelBiz;
    @Resource
    private LogBiz logBiz;
    @Resource
    private KfTopBiz kfTopBiz;


    @MsgMethod(MessageComm.G2C_SyncRpcServer)
    public void syncServer(JsonMsg msg) {
        List<Integer> serverIdList = (List<Integer>)msg.getObject("serverIds");
        String rpcUrl = msg.getString("rpcUrl");
        for (int serverId : serverIdList) {
            GameRpcClientUtils.changeServerRpc(serverId,rpcUrl);
        }
    }

    @MsgMethod(KFRankMessageComm.C2S_OpenKf)
    public void initSports(JsonMsg msg) {
        long playerId = msg.getPlayerId();
        if (playerId <= 0) {
            log.error(playerId+"playerId is 0");
            return;
        }
        int groupId = groupContainer.getGroupId(playerId);
        LevelWorldGroup levelWorldGroup = levelGroupContainer.getLevelWorldGroup(groupId);
        if (levelWorldGroup == null) {
            log.error(playerId+"报名出错:找不到世界组=" + groupId);
            return;
        }

        log.error("初始化:" + playerId);
        //判断是可以参加此活动
        PlayerRedisData playerRedisData = RedisUtil.getPlayerRedisData(playerId);
        if (playerRedisData.getFbId() < KFLevelConstants.MinFBId) {
            log.error(playerId+"fb id is min" + playerRedisData.getFbId());
            return;
        }
        KFPlayer player = KfDBUtils.getPlayerSports(playerId);
        if (player == null) {
            player = new KFPlayer();
            player.setId(playerId);
            player.setServerId(playerRedisData.getServerid());
            player.setGameTypeId(groupContainer.getGroupId(playerId));
            KFPlayerDBCache.getInstance().addPlayerToCache(player);
//			player.save(); 不保存
        }
        player.setServerId(ServerUtils.getServerId(playerId));
        player.getPlayerTemp().setCombat(playerRedisData.getTroopCombat());

        kfLevelBiz.doDayZero(player);

        kfLevelBiz.doEnterKF(player);
        //随机地方部队
        kfLevelBiz.checkRandomMatchUser(player);
        //top检查
        kfTopBiz.doPlayerLogin(player);

        JsonMsg serverMsg = JsonMsg.create(KFRankMessageComm.S2C_OpenKf);
        kfTopBiz.fillPlayerOppoInfo(player,serverMsg);
        player.sendMsg(serverMsg);

        kFPlayerContainer.addPlayer2Map(player);
        player.sendPlayerUpdate(true);

        if(player.getLevelPlayer().clearBeforeLvType()) {
            player.save();//只展示一次
        }
        log.error("初始化完成:" + playerId);
        logBiz.addPlayerActionLog(playerRedisData, ActionType.KFJoin, String.valueOf(KfType.PKLevel.getType()));
    }
}
