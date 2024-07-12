package com.hm.model.player;

import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.spring.SpringUtil;
import com.hm.action.trade.biz.TradeBiz;
import com.hm.config.GameConstants;
import com.hm.config.TradeConfig;
import com.hm.config.excel.temlate.SeaTradeRoadNewTemplate;
import com.hm.enums.BoatState;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.trade.Company;
import com.hm.model.trade.Ship;

import java.util.List;

public class PlayerShipTrade extends PlayerDataContext {
    // 航运公司
    private Company company = new Company();
    private List<Ship> ships = Lists.newArrayList();

    public Company getCompany() {
        return company;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public Ship getShip(int id) {
        return ships.stream().filter(ship -> ship.getId() == id).findFirst().orElse(null);
    }
    private boolean isOpen(BasePlayer player) {
        return player != null && player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Trade);
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        if (!isOpen(Context())) {
            return;
        }

        msg.addProperty("playerShipTrade", this);
    }

    public boolean check() {
        BasePlayer basePlayer = Context();
        if (!isOpen(basePlayer)) {
            return false;
        }

        boolean isChanged = false;

        TradeBiz tradeBiz = SpringUtil.getBean(TradeBiz.class);
        long now = System.currentTimeMillis();

        // 航运公司结算
        if (tradeBiz.checkout(basePlayer)) {
            isChanged = true;
        }

        // 检测建筑是否到升级结束时间
        if (company.getLvUpEndTime() > 0 && company.getLvUpEndTime() < now) {
            company.incLv();
            company.setLvUpEndTime(0);
            isChanged = true;
        }

        for (Ship ship : ships) {
            // 升级是否完成
            if (ship.isOnReform() && ship.getReformEndTime() < now && ship.getReformEndTime() != 0) {
                ship.lvUp();
                ship.setOnReform(false);
                ship.setReformEndTime(0);

                isChanged = true;
            }
            // 是否到交货时间
            if (tradeBiz.checkoutShip(basePlayer, ship)) {
                isChanged = true;
            }
        }

        if (isChanged) {
            SetChanged();
        }
        return isChanged;
    }

}
