package com.hm.redis.trade;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.redis.mode.AbstractRedisHashMode;
import com.hm.util.RandomUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 大股东分的资源
 * @date 2023/8/28 13:44
 */
@Getter
@NoArgsConstructor
public class PlayerStockRes extends AbstractRedisHashMode {
    private long playerId;
    private String mark;

    private long yesRes;
    private long nowRes;

    private HashMap<Long,Long> playerMap = Maps.newHashMap();
    private HashMap<Long,Long> timeMap = Maps.newHashMap();
    private HashMap<Long,Integer> countMap = Maps.newHashMap();


    public PlayerStockRes(long playerId) {
        this.playerId = playerId;
        this.mark = TimeUtils.formatSimpeTime2(new Date());
    }

    public void addRes(long subId,long pushNum) {
        String mark = TimeUtils.formatSimpeTime2(new Date());
        //检查跨天
        checkDay(mark);
        //添加
        this.nowRes += pushNum;

        this.playerMap.put(subId,this.playerMap.getOrDefault(subId,0L)+pushNum);
        this.timeMap.put(subId,System.currentTimeMillis());
        this.countMap.remove(subId);
        this.mark = mark;
        saveDB();
    }

    public void updatePushTime(long subId) {
        this.timeMap.put(subId,System.currentTimeMillis());
        saveDB();
    }

    public void checkDay(String mark) {
        if(!StrUtil.equals(mark,this.mark)) {
            this.yesRes += this.nowRes;
            this.nowRes = 0L;
            this.playerMap.clear();
            this.mark = mark;
        }
    }

    public long getTotalRes(String mark) {
        checkDay(mark);

        long result = this.yesRes;
        this.yesRes = 0L;
        return result;
    }

    public void delAndSave() {
        if(isEmpty()) {
            del();
        }else{
            saveDB();
        }
    }

    public boolean isEmpty() {
        return this.yesRes <= 0 && this.nowRes <= 0 && this.timeMap.isEmpty()
                    && this.countMap.isEmpty();
    }


    @Override
    public String buildFiledKey() {
        return playerId+"";
    }

    @Override
    public String buildHashKey() {
        return "PlayerStockRes";
    }

    public static PlayerStockRes getPlayerStockRes(long playerId) {
        PlayerStockRes playerStock = PlayerStockRes.getHashVal("PlayerStockRes",playerId+"", PlayerStockRes.class);
        if(playerStock == null) {
            playerStock = new PlayerStockRes(playerId);
        }
        return playerStock;
    }

    public void doOfflinePlayer(CommValueConfig commValueConfig) {
        if(this.timeMap.isEmpty()) {
            return;
        }
        int maxCount = commValueConfig.getCommValue(CommonValueType.TradeGDMaxCount);
        double rate = commValueConfig.getDoubleCommValue(CommonValueType.TradeGDLeaveRate);
        int[] randomVal = commValueConfig.getCommonValueByInts(CommonValueType.TradeGDRandom);
        PlayerStock playerStock = PlayerStock.getPlayerStock(this.playerId);
        List<Long> subList = playerStock.getSubList();

        Date now = new Date();
        for (long subId : subList) {
            int count = this.countMap.getOrDefault(subId,0);
            if(count < maxCount
                    && RandomUtils.randomIsRate(rate)
                    && !DateUtil.isSameDay(now,new Date(this.timeMap.getOrDefault(subId,now.getTime())))) {
                //作弊
                int pushNum = RandomUtils.randomIntForEnd(randomVal[0],randomVal[1]);

                System.out.println(playerId+"->"+subId+" 离线收益:"+pushNum);
                this.nowRes += pushNum;
                this.playerMap.put(subId,this.playerMap.getOrDefault(subId,0L)+pushNum);
                this.timeMap.put(subId,System.currentTimeMillis());
                this.countMap.put(subId,count+1);
            }
        }

        //离线超过20天
        for (long id : Lists.newArrayList(this.timeMap.keySet())) {
            if(!CollUtil.contains(subList,id)
                    && System.currentTimeMillis() - this.timeMap.getOrDefault(id,0L) > 20* GameConstants.DAY
                ) {
                this.timeMap.remove(id);
                this.countMap.remove(id);
            }
        }
    }
}
