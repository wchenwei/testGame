package com.hmkf.info;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.tank.biz.TankBiz;
import com.hm.action.tank.enums.TankDetailMsg;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.player.Player;
import com.hm.action.player.vo.PlayerDetailVo;
import com.hm.model.tank.Tank;
import com.hm.sysConstant.SysConstant;
import com.hmkf.action.KFAbstractPlayerAction;
import com.hmkf.db.KfPlayerUtils;
import com.hmkf.guild.KfGuildUtils;
import com.hm.message.KFRankMessageComm;
import com.hmkf.model.KFPlayer;

@Service
public class KfPlayerServerAction extends KFAbstractPlayerAction {
    @Resource
    private TankBiz tankBiz;

    @MsgMethod(KFRankMessageComm.C2S_Player_Vo)
    public void getPlayerVo(KFPlayer player, JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        Player tarPlayer = KfPlayerUtils.getPlayerFromDB(playerId);


        PlayerDetailVo playerVo = new KFPlayerVo(tarPlayer);

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Player_Vo);
        retMsg.addProperty("playerVo",playerVo);
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_TarTankMsg)
    public void getTargetPlayerTank(KFPlayer player, JsonMsg msg) {
        int tankId = msg.getInt("tankId");
        int tarPlayerId = msg.getInt("tarPlayerId");

        Player tarPlayer = KfPlayerUtils.getPlayerFromDB(tarPlayerId);
        if (null == tarPlayer) {
            player.sendErrorMsg(SysConstant.PLAYER_NOT_EXIST);
            return;
        }
        if (!tarPlayer.playerTank().isHaveTank(tankId)) {
            player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
            return;
        }
        Map<Integer, Object> resultMap = Maps.newHashMap();
        for (TankDetailMsg temp : TankDetailMsg.values()) {
            resultMap.put(temp.getType(), temp.getData(tarPlayer, tankId));
        }
        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TarTankMsg);
        retMsg.addProperty("resultMap",resultMap);
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_TarTank)
    public void getTargetTank(KFPlayer player, JsonMsg msg) {
        int tankId = msg.getInt("tankId");
        long tarPlayerId = msg.getLong("tarPlayerId");

        Player tarPlayer = KfPlayerUtils.getPlayerFromDB(tarPlayerId);
        if (null == tarPlayer) {
            player.sendErrorMsg(SysConstant.PLAYER_NOT_EXIST);
            return;
        }
        if (!tarPlayer.playerTank().isHaveTank(tankId)) {
            player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
            return;
        }
        Map<Long, Map<Integer, Object>> playerId2Tank = Maps.newHashMap();

        Map<Integer, Object> tankMsg = tankBiz.getTankMsg(tarPlayer, tankId);
        PlayerRedisData redisData = RedisUtil.getPlayerRedisData(tarPlayerId);
        tankMsg.put(-1, redisData);
        playerId2Tank.put(tarPlayerId, tankMsg);

        Player sourcePlayer = KfPlayerUtils.getPlayerFromDB(player.getId());
        if (sourcePlayer.playerTank().isHaveTank(tankId)) {
            Map<Integer, Object> selfTankMsg = tankBiz.getTankMsg(sourcePlayer, tankId);
            playerId2Tank.put(player.getId(), selfTankMsg);
        }

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TarTank);
        retMsg.addProperty("playerId2Tank",playerId2Tank);
        player.sendMsg(retMsg);
    }
}