package com.hm.action.trade;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.trade.biz.TradeStockBiz;
import com.hm.action.trade.vo.TradePlayerAllVo;
import com.hm.action.trade.vo.TradePlayerRankVo;
import com.hm.action.trade.vo.TradePlayerVo;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.TradeConfig;
import com.hm.config.excel.temlate.SeaTradeBuildingTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.PlayerFunctionType;
import com.hm.message.MessageComm;
import com.hm.model.player.Player;
import com.hm.model.tank.TankVo;
import com.hm.redis.trade.PlayerStock;
import com.hm.redis.trade.PlayerStockRes;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.WarResult;
import lombok.extern.slf4j.Slf4j;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Slf4j
@Action
public class TradeStockAction extends AbstractPlayerAction {
    @Resource
    private TradeStockBiz tradeStockBiz;
    @Resource
    private TradeConfig tradeConfig;

    //设置防守部队
    @MsgMethod(MessageComm.C2S_TradeStock_EditorTroop)
    public void editorTroop(Player player, JsonMsg msg) {
        ClientTroop clientTroop = ClientTroop.build(player, "armys", msg);

        long combat = clientTroop.getArmyList().stream().map(e -> player.playerTank().getTank(e.getId()))
                        .filter(Objects::nonNull).mapToLong(e -> e.getCombat()).sum();
        PlayerStock.getPlayerStock(player.getId()).setDefTroops(clientTroop.toTroopStr(),combat);

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TradeStock_EditorTroop);
        retMsg.addProperty("combat",combat);
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_TradeStock_Open)
    public void getStock(Player player, JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        playerId = playerId <= 0?player.getId():playerId;

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TradeStock_Open);
        retMsg.addProperty("playerId",playerId);
        PlayerStock playerStock = PlayerStock.getPlayerStockOrNull(playerId);
        if(playerStock == null) {
            player.sendMsg(retMsg);
            return;
        }

        TradePlayerAllVo tradePlayerAllVo = new TradePlayerAllVo(playerStock);
        retMsg.addProperty("info",tradePlayerAllVo);
        if(playerId == player.getId()) {
            retMsg.addProperty("defList",playerStock.getDefTroops());
        }
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_TradeStock_GetOwner)
    public void GetOwner(Player player, JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        playerId = playerId <= 0?player.getId():playerId;

        PlayerStock playerStock = PlayerStock.getPlayerStock(playerId);

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TradeStock_GetOwner);
        if(playerStock.getOwnerId() > 0) {
            retMsg.addProperty("ownerInfo",new TradePlayerVo(playerStock.getOwnerId()));
        }
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_TradeStock_GetDefTroop)
    public void getDefTroops(Player player, JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        playerId = playerId <= 0?player.getId():playerId;

        PlayerStock playerStock = PlayerStock.getPlayerStock(playerId);

        Player defPlayer = PlayerUtils.getPlayerFromKF(playerId);
        if(defPlayer == null) {
            return;
        }
        List<TankVo> defList = playerStock.getDefTankList(defPlayer).stream().map(e -> e.createTankVo()).collect(Collectors.toList());
        //更新战力
        long combat = defList.stream().mapToLong(e -> e.getCombat()).sum();
        if(combat != playerStock.getCombat()) {
            playerStock.setCombat(combat);
            playerStock.saveDB();
        }

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TradeStock_GetDefTroop);
        retMsg.addProperty("defList",defList);
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_TradeStock_WarOwner)
    public void warOwner(Player player, JsonMsg msg) {
        PlayerStock playerStock = PlayerStock.getPlayerStock(player.getId());
        if(playerStock.getOwnerId() <= 0) {
            return;
        }
        ClientTroop clientTroop = ClientTroop.buildFull(player, "armys", msg);
        WarResult warResult = tradeStockBiz.warTradeStock(player,clientTroop,playerStock.getOwnerId());

        if(warResult.isAtkWin()) {
            log.error(playerStock.getOwnerId()+"股东丢失:"+playerStock.getPlayerId());
            PlayerStock.getPlayerStock(playerStock.getOwnerId()).removeSubList(playerStock.getPlayerId());
            tradeStockBiz.sendOwnerChangeMail(playerStock.getOwnerId(),player.getId(),player.getId());

            playerStock.changeOwnerId(0);//胜利
        }
        //记录
        saveRecord(player,warResult);

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TradeStock_WarOwner);
        retMsg.addProperty("warResult",warResult);
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_TradeStock_WarOther)
    public void warOther(Player player, JsonMsg msg) {
        long playerId = msg.getInt("playerId");
        //判断数量
        PlayerStock myStock = PlayerStock.getPlayerStock(player.getId());
        SeaTradeBuildingTemplate config = tradeConfig.getBuildingConfig(player.playerShipTrade().getCompany().getLv());
        if(myStock.getSubList().size() >= config.getMax_stock()) {
            player.sendErrorMsg(SysConstant.Trade_Max_Stock);
            return;
        }

        PlayerStock playerStock = PlayerStock.getPlayerStock(playerId);
        ClientTroop clientTroop = ClientTroop.buildFull(player, "armys", msg);

        //战斗对手id
        long defPlayerId = playerStock.getOwnerId() > 0?playerStock.getOwnerId():playerStock.getPlayerId();
        if(player.getId() == defPlayerId) {
            return;
        }
        WarResult warResult = tradeStockBiz.warTradeStock(player,clientTroop,defPlayerId);

        if(warResult.isAtkWin()) {
            if(playerStock.getOwnerId() > 0) {//原来的大股东删除
                log.error(playerStock.getOwnerId()+"股东被抢:"+playerStock.getPlayerId());
                PlayerStock.getPlayerStock(playerStock.getOwnerId()).removeSubList(playerStock.getPlayerId());
                tradeStockBiz.sendOwnerChangeMail(defPlayerId,player.getId(),playerId);
            }else {
                tradeStockBiz.sendMyTradeStockChange(playerStock.getPlayerId(),player.getId());
            }

            if(player.getId() == playerId) {
                //和我自己的股东战斗
                playerStock.changeOwnerId(0);//胜利
            }else{
                playerStock.changeOwnerId(player.getId());//胜利
                PlayerStock.getPlayerStock(player.getId()).addSubList(playerId);//添加到自己的下属列表
                //记录最后一次时间
                PlayerStockRes playerStockRes = PlayerStockRes.getPlayerStockRes(player.getId());
                playerStockRes.updatePushTime(playerId);
            }
        }
        //战斗记录
        saveRecord(player,warResult);

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TradeStock_WarOther);
        retMsg.addProperty("warResult",warResult);
        player.sendMsg(retMsg);
    }

    @MsgMethod(MessageComm.C2S_TradeStock_GiveUpOwner)
    public void GiveUpOwner(Player player, JsonMsg msg) {
        long playerId = msg.getInt("subId");
        PlayerStock playerStock = PlayerStock.getPlayerStock(playerId);
        if(playerStock.getOwnerId() == player.getId()) {
            log.error(player.getId()+"股东主动放弃:"+playerId);
            PlayerStock.getPlayerStock(player.getId()).removeSubList(playerId);
            playerStock.changeOwnerId(0);
        }

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TradeStock_GiveUpOwner);
        retMsg.addProperty("subId",playerId);
        player.sendMsg(retMsg);
    }
    @MsgMethod(MessageComm.C2S_TradeStock_Rank)
    public void rank(Player player, JsonMsg msg) {
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TradeStock)) {
            player.sendErrorMsg(SysConstant.Function_Lock);
            return;//已经解锁了
        }
        int type = msg.getInt("type");//0-本服 1-跨服
        List<TradePlayerRankVo> rankList = tradeStockBiz.getRankPlayerVoList(player,type);

        JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_TradeStock_Rank);
        retMsg.addProperty("rankList",rankList);
        retMsg.addProperty("type",type);
        player.sendMsg(retMsg);
    }

    @MsgMethod (MessageComm.C2S_TradeStock_Record)
    public void recordList(Player player, JsonMsg msg){
//        List<BattleRecord> battRecordList = player.playerTradeStock().getRecordList()
//                .stream().map(e -> WarResultUtils.getBattleRecord(player.getServerId(), e))
//                .filter(Objects::nonNull).collect(Collectors.toList());
//
//        JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_TradeStock_Record);
//        serverMsg.addProperty("recordList", battRecordList);
//        player.sendMsg(serverMsg);
    }

    private void saveRecord(Player player,WarResult warResult) {
        //记录
//        BattleRecord record = new BattleRecord(player.getServerId(), warResult);
//        warResult.setWarResultType(WarResultType.Stock);
//        WarResultUtils.saveOrUpdate(record);
//
//        player.playerTradeStock().addRecordLog(record.getId());
//        player.saveDB();
    }

}
