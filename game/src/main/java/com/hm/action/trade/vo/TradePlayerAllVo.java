package com.hm.action.trade.vo;

import cn.hutool.core.collection.CollUtil;
import com.hm.redis.trade.PlayerStock;
import com.hm.redis.trade.PlayerStockRes;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家的航运股东信息
 * @date 2023/8/28 13:58
 */
@Data
public class TradePlayerAllVo {
    private long id;
    private TradePlayerVo owner;//大股东信息 不存在时代表么有大股东
    private List<TradePlayerVo> subList; //子公司列表
    private long combat;


    public TradePlayerAllVo(PlayerStock playerStock) {
        this.id = playerStock.getPlayerId();
        this.combat = playerStock.getCombat();
        if(playerStock.getOwnerId() > 0) {
            this.owner = new TradePlayerVo(playerStock.getOwnerId());
            this.owner.setVal(playerStock.getVal());
        }
        this.subList = playerStock.getSubList().stream().map(e -> new TradePlayerVo(e)).collect(Collectors.toList());
        if(CollUtil.isNotEmpty(this.subList)) {
            PlayerStockRes playerStockRes = PlayerStockRes.getPlayerStockRes(id);
            Map<Long,Long> playerMap = playerStockRes.getPlayerMap();
            for (TradePlayerVo tradePlayerVo : subList) {
                tradePlayerVo.setVal(playerMap.getOrDefault(tradePlayerVo.getPlayerId(),0L));
            }
        }
    }

}
