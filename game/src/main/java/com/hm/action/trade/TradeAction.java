package com.hm.action.trade;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.trade.biz.TradeBiz;
import com.hm.action.trade.biz.TradeStockBiz;
import com.hm.action.vip.VipBiz;
import com.hm.config.GameConstants;
import com.hm.config.TradeConfig;
import com.hm.config.excel.temlate.SeaTradeBuildingTemplate;
import com.hm.config.excel.temlate.SeaTradeShipTemplate;
import com.hm.enums.LogType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.enums.VipPowType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.model.trade.Company;
import com.hm.model.trade.Ship;
import com.hm.observer.ObservableEnum;
import com.hm.sysConstant.SysConstant;
import com.hm.util.MathUtils;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-01-08
 */
@Action
public class TradeAction extends AbstractPlayerAction {
    @Resource
    private PlayerBiz playerBiz;
    @Resource
    private TradeBiz tradeBiz;
    @Resource
    private TradeConfig tradeConfig;
    @Resource
    private ItemBiz itemBiz;
    @Resource
    private TradeStockBiz tradeStockBiz;
    @Resource
    private VipBiz vipBiz;
    /**
     * 改造
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Trade_Reform)
    public void reform(Player player, JsonMsg msg) {
        int shipId = msg.getInt("id");
        Ship ship = player.playerShipTrade().getShip(shipId);
        if (ship == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        if (ship.isOnReform()) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        // 已经是最大等级了
        int maxBoatLv = tradeConfig.getMaxBoatLv();
        int cLv = player.playerShipTrade().getCompany().getLv();
        maxBoatLv = Math.min(maxBoatLv, cLv);
        if (ship.getLv() >= maxBoatLv) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        SeaTradeShipTemplate shipConfig = tradeConfig.getShipConfig(ship.getLv());
        if (shipConfig == null) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        Integer reformCost = shipConfig.getReform_cost();
        if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Cash, reformCost, LogType.TradeReform)) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        ship.setOnReform(true);
        ship.setReformEndTime(System.currentTimeMillis() + shipConfig.getReform_time() * GameConstants.SECOND);
        player.playerShipTrade().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Trade_Reform);
    }

    @MsgMethod(MessageComm.C2S_Trade_ReformGold)
    public void reformGold(Player player, JsonMsg msg) {
        int shipId = msg.getInt("id");
        Ship ship = player.playerShipTrade().getShip(shipId);
        long now = System.currentTimeMillis();
        if (null == ship || !ship.isOnReform() || ship.getReformEndTime() < now) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        SeaTradeShipTemplate config = tradeConfig.getShipConfig(ship.getLv());
        // 剩余时长
        long timeRemain = ship.getReformEndTime() - now;
        double div = MathUtils.div(timeRemain * config.getGold_time(), config.getReform_time() * GameConstants.SECOND);
        int cost = (int) Math.ceil(div);
        if (cost <= 0) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }
        if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, cost, LogType.TradeReform.value(ship.getId()))) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        ship.lvUp();
        ship.setOnReform(false);
        ship.setReformEndTime(0);
        player.playerShipTrade().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Trade_ReformGold);
    }
    /**
     * 航运公司升级
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Trade_Upgrade)
    public void upgrade(Player player, JsonMsg msg) {
        Company company = player.playerShipTrade().getCompany();
        long now = System.currentTimeMillis();
        if (company == null || company.getLvUpEndTime() >= now) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        // 已经是最大等级了
        int maxBuildingLv = tradeConfig.getMaxBuildingLv();
        if (company.getLv() >= maxBuildingLv) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        SeaTradeBuildingTemplate config = tradeConfig.getBuildingConfig(company.getLv());
        // 扣资源
        if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Cash, config.getReform_cost(),
                LogType.TradeBuilding.value(company.getLv()))) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }

        company.setLvUpEndTime(now + config.getReform_time() * GameConstants.SECOND);
        player.playerShipTrade().SetChanged();
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Trade_Upgrade);
    }

    /**
     * 升级立即完成
     *
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_Trade_UpgradeGold)
    public void upgradeCompanyNow(Player player, JsonMsg msg) {
        Company company = player.playerShipTrade().getCompany();
        long now = System.currentTimeMillis();
        // 必须处于正在升级中
        if (company == null || company.getLvUpEndTime() < now) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        SeaTradeBuildingTemplate config = tradeConfig.getBuildingConfig(company.getLv());
        // 剩余时长
        long timeRemain = company.getLvUpEndTime() - now;
        double div = MathUtils.div(timeRemain * config.getGold_time(), config.getReform_time() * GameConstants.SECOND);

        int cost = (int) Math.ceil(div);
        if (cost <= 0) {
            player.sendErrorMsg(SysConstant.PARAM_ERROR);
            return;
        }

        if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, cost, LogType.TradeBuilding.value(company.getLv()))) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        company.incLv();
        company.setLvUpEndTime(0);
        player.playerShipTrade().SetChanged();
        player.notifyObservers(ObservableEnum.TradeCompanyLvUp);

        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Trade_UpgradeGold);
    }

    @MsgMethod(MessageComm.C2S_Trade_Exchange)
    public void exchange(Player player, JsonMsg msg) {
        long num = msg.getInt("num");
        if (num <= 0) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        double rate = Math.min(Convert.toDouble(msg.getString("oilRate"), 0d), 1);
        if(rate < 0){
            return;
        }
        // 扣资源
        if (!playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Shipping, num, LogType.TradeExchange)) {
            player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
            return;
        }
        //计算股东分红
        num = tradeStockBiz.calPlayerStock(player,num);

        // 航运币 相当于 1石油+ 1钞票
        num *= 2;
        long oilNum = (long) (num * rate);
        long cashNum = (long) (num * (1-rate));
        List<Items> rewards = Lists.newArrayList();
        tradeBiz.exchange(player, oilNum, CurrencyKind.Oil,PlayerAssetEnum.Oil, rewards);
        tradeBiz.exchange(player, cashNum, CurrencyKind.Cash, PlayerAssetEnum.Cash, rewards);

        itemBiz.addItem(player, rewards, LogType.TradeExchange);
        player.notifyObservers(ObservableEnum.TradeExchange);
        player.sendUserUpdateMsg();
        player.sendMsg(MessageComm.S2C_Trade_Exchange, rewards);
    }

    @MsgMethod(MessageComm.C2S_Trade_Buy_Boat)
    public void buyBoat(Player player, JsonMsg msg) {
        boolean vip = msg.getBoolean("vip");
        if (vip) {
            // vip 特权上限
            int limit = vipBiz.getVipPow(player, VipPowType.Trade);
            // 目前已拥有的vip船只数量
            long count = player.playerShipTrade().getShips().stream().filter(Ship::isVip).count();
            if (count >= limit) {
                return;
            }
            tradeBiz.addVipShip(player);
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_Trade_Buy_Boat);
        } else if (tradeBiz.addShip(player)) {
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_Trade_Buy_Boat);
        }
    }

    @MsgMethod(MessageComm.C2S_Trade_Sync)
    public void sync(Player player, JsonMsg msg) {
        if (player.playerShipTrade().check()) {
            player.sendUserUpdateMsg();
            player.sendMsg(MessageComm.S2C_Trade_Sync);
        }
    }
}
