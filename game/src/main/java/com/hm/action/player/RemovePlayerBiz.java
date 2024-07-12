package com.hm.action.player;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.date.DateUtil;
import com.hm.db.PlayerUtils;
import com.hm.model.player.Player;
import com.hm.redis.type.RedisTypeEnum;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.idcode.IdCodeContainer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.Map;

@Slf4j
@Biz
public class RemovePlayerBiz {
    // 注销冷静期
    private static final int Cool_Off_Hour = 72;
    // 待删除 用户id-- 注销时间
    private static Map<Long, Long> playerIdAndClearTime = Maps.newConcurrentMap();

    public static void init(){
        playerIdAndClearTime.clear();
        Map<String, String> allVal = RedisTypeEnum.RemovePlayer.getAllVal();
        allVal.entrySet().forEach(entry->{
            long playerId = Convert.toInt(entry.getKey(), 0);
            if(GameServerManager.getInstance().isServerMachinePlayer(playerId)){
                Long clearTime = Convert.toLong(entry.getValue(), 0L);
                playerIdAndClearTime.put(playerId, clearTime);
            }
        });
    }

    // 添加注销用户
    public long addClearPlayer(Player player){
        long clearTime = DateUtil.offsetHour(DateUtil.date(), Cool_Off_Hour).getTime();
        // 添加redis 数据
        RedisTypeEnum.RemovePlayer.put(player.getId(), String.valueOf(clearTime));
        playerIdAndClearTime.put(player.getId(), clearTime);
        return clearTime;
    }

    // 登录 检查注销用户
    public void doLogin(Player player){
        if(!playerIdAndClearTime.containsKey(player.getId())){
            return;
        }
        // 删除 redis
        RedisTypeEnum.RemovePlayer.del(player.getId());
        // 删除缓存
        playerIdAndClearTime.remove(player.getId());
    }

    // 检查 删除用户数据
    public void checkRemovePlayer(){
        if(CollUtil.isEmpty(playerIdAndClearTime)){
            return;
        }
        playerIdAndClearTime.entrySet().forEach(entry->{
            Long endTime = entry.getValue();
            if(System.currentTimeMillis() < endTime){
                return;
            }
            long playerId = entry.getKey();
            Player player = PlayerUtils.getPlayer(playerId);
            if(player == null){
                return;
            }
            boolean removePlayer = this.removePlayer(player);
            if(!removePlayer){
                return;
            }
            // 清除 redis 数据
            RedisTypeEnum.RemovePlayer.del(entry.getKey());
            playerIdAndClearTime.remove(playerId);
        });
    }

    private boolean removePlayer(Player player){
        try {
            IdCodeContainer.of(player).unBindIdCode(player);
            player.unBindIdCode();//清空玩家身上的唯一码
            player.setUidForCreate(-player.getUid(), player.getCreateServerId());
            player.saveNowDB();
            log.info("System remove player->"+player.getId());
        }catch (Exception e){
            log.info("player remove err->" + player.getId() + e.getMessage());
            return false;
        }
        return true;
    }
}
