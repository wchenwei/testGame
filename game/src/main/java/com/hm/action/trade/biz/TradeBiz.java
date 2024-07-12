package com.hm.action.trade.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.action.player.PlayerBiz;
import com.hm.config.GameConstants;
import com.hm.config.TradeConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.temlate.SeaTradeBuildingTemplate;
import com.hm.config.excel.temlate.SeaTradeRoadNewTemplate;
import com.hm.config.excel.temlate.SeaTradeShipTemplate;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.*;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.model.trade.Company;
import com.hm.model.trade.Ship;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.util.CalculationUtil;
import com.hm.util.MathUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Description:
 * User: yang xb
 * Date: 2019-01-08
 */
@Biz
public class TradeBiz implements IObserver {
    @Resource
    private PlayerBiz playerBiz;
    @Resource
    private TradeConfig tradeConfig;
    @Resource
    private CommValueConfig commonValueConfig;
    @Resource
    private RechargeConfig rechargeConfig;


    /**
     * 航运公司结算
     *
     * @param player
     * @return true:updated
     */
    public boolean checkout(BasePlayer player) {
        // 功能是否开启
        if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Trade)) {
            return false;
        }

        long now = System.currentTimeMillis();
        Company company = player.playerShipTrade().getCompany();
        // 合法性验证
        if (company == null || now <= company.getLastCalcTime()) {
            return false;
        }

        // 航运币上限
        Integer limit = this.getCompanyLimit(player);
        long own = player.playerCurrency().get(CurrencyKind.Shipping);
        // 超上限后禁止交货
        if (own >= limit) {
            return false;
        }

        boolean updated = false;
        long intervalMillis = now - company.getLastCalcTime();
        if (intervalMillis > 0) {
            produce(player, company.getLv(), intervalMillis);
            company.setLastCalcTime(now);
            updated = true;
            player.playerShipTrade().SetChanged();
        }
        return updated;
    }


    /**
     * 增加航船
     *
     * @param player
     */
    public boolean addShip(Player player) {
        // 已拥有的船只
        List<Ship> ships = player.playerShipTrade().getShips();
        // 非vip船只数量
        final int shipSize = (int) ships.stream().filter(s-> !s.isVip()).count();
        // 增加航船时的价格,同时数组长度也是可拥有船只的上限
        int[] ints = commonValueConfig.getCommonValueByInts(CommonValueType.TradeBoatPrice);
        if (shipSize >= ints.length) {
            return false;
        }

        int cost = ints[shipSize];
        if (cost > 0 && !playerBiz.checkAndSpendPlayerCurrency(player, CurrencyKind.Gold, cost, LogType.TradeBuyBoat)) {
            return false;
        }

        Ship ship = new Ship();
        ship.setId(shipSize);
        ship.setLv(1); // 默认1级
        // 船只添加后立马就开始
        long now = System.currentTimeMillis();
        Integer pickItemId = tradeConfig.pickItemId();
        if (pickItemId != null) {
            ship.setItemId(pickItemId);
        }
        ship.setBeginTime(now);
        SeaTradeRoadNewTemplate city = tradeConfig.pickCity();
        if (city != null) {
            ship.setEndTime(now + GameConstants.MINUTE * city.getTime());
            ship.setTargetId(city.getEnd_city());
        }

        ships.add(ship);
        player.playerShipTrade().SetChanged();
        return true;
    }

    public boolean addVipShip(Player player) {
        int count = (int) player.playerShipTrade().getShips().stream().filter(Ship::isVip).count();
        Ship ship = new Ship();
        // vip船只id以100开始
        ship.setId(100 + count);
        ship.setLv(1); // 默认1级
        ship.setVip(true);
        // 船只添加后立马就开始
        long now = System.currentTimeMillis();
        Integer pickItemId = tradeConfig.pickItemId();
        if (pickItemId != null) {
            ship.setItemId(pickItemId);
        }
        ship.setBeginTime(now);
        SeaTradeRoadNewTemplate city = tradeConfig.pickCity();
        if (city != null) {
            ship.setEndTime(now + GameConstants.MINUTE * city.getTime());
            ship.setTargetId(city.getEnd_city());
        }

        player.playerShipTrade().getShips().add(ship);
        player.playerShipTrade().SetChanged();
        return true;
    }

    /**
     * 航运公司产出
     *
     * @param player
     * @param companyLv
     * @param intervalMillis
     */
    private void produce(BasePlayer player, int companyLv, long intervalMillis) {
        Company company = player.playerShipTrade().getCompany();
        SeaTradeBuildingTemplate cfg = tradeConfig.getBuildingConfig(companyLv);
        if (cfg == null || company == null) {
            return;
        }

        // 20190924,改：航运公司产出最多补满玩家上限，多余的扔掉

        // 产出
        double product = MathUtils.div(intervalMillis, GameConstants.HOUR) * cfg.getHour_point();

        // 航运币上限
        Integer limit = this.getCompanyLimit(player);
        // 目前拥有
        long own = player.playerCurrency().get(CurrencyKind.Shipping);
        // 最多可添加
        long sub = limit - own;
        double min = Math.min(sub, product);
        if (min > 0) {
            playerBiz.addPlayerCurrency(player, CurrencyKind.Shipping, (long) min, LogType.TradeBuilding);
        }
    }

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.CostRes, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch (observableEnum){
            case FunctionUnlock:
                int funcId = Integer.parseInt(argv[0].toString());
                if (funcId != PlayerFunctionType.Trade.getType()) {
                    return;
                }
                if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Trade)) {
                    return;
                }
//                init(player);
                return;
            case CostRes:
                CurrencyKind kind = CurrencyKind.getCurrencyByIndex(Integer.parseInt(argv[0].toString()));
                if (kind != CurrencyKind.Shipping) {
                    return;
                }
                // 航运币上限
                Integer limit = this.getCompanyLimit(player);
                // 目前拥有
                long own = player.playerCurrency().get(CurrencyKind.Shipping);
                if (own >= limit) {
                    return;
                }
                break;
            case Recharge:
                int giftId = (int)argv[3];
                RechargeGiftTempImpl rechargeGift = rechargeConfig.getRechargeGift(giftId);
                if(RechargeType.SeasonVipCard.getType() != rechargeGift.getType()){
                    return;
                }
                break;
            default:
                return;
        }
        boolean bChanged = false;
        for (Ship ship : player.playerShipTrade().getShips()) {
            bChanged = checkoutShip(player, ship) || bChanged;
        }
        if (bChanged) {
            player.playerShipTrade().SetChanged();
        }
    }

    private void init(Player player) {
        if (player.playerShipTrade().getShips().size() > 0) {
            return;
        }
        // 航运公司
        long now = System.currentTimeMillis();
        Company company = player.playerShipTrade().getCompany();
        company.setLv(1);
        company.setLastCalcTime(now);

        // 送两艘船
        addShip(player);
        addShip(player);

        player.playerShipTrade().SetChanged();
    }


    /**
     * 结算一条船
     * 0:如果正在航行中(endTime>=now)，不结算
     * 1:溢出上限的直接丢掉，同建筑
     * 2:已经满了的，不交单，等用的低于上限后再交单
     *
     * @param player
     * @param ship
     * @return 数据是否修改
     */
    public boolean checkoutShip(BasePlayer player, Ship ship) {
        if (!ship.isOver()) {
            return false;
        }

        // 航运币上限
        Integer limit = this.getCompanyLimit(player);
        // 目前拥有
        long own = player.playerCurrency().get(CurrencyKind.Shipping);
        // 最多可添加
        long sub = limit - own;
        // 已经满了，交不了单
        if (sub <= 0) {
            ship.setFull(true);
            return true;
        }

        long now = System.currentTimeMillis();
        double s;
        // 如果待交货、则只计算最后一趟的
        // 否则、计算从开始到现在这段时间
        if (ship.isFull()) {
            s = MathUtils.div(ship.getEndTime() - ship.getBeginTime(), GameConstants.MINUTE);
        } else {
            s = MathUtils.div(now - ship.getBeginTime(), GameConstants.MINUTE);
        }
        if (s <= 0) {
            return false;
        }
        SeaTradeShipTemplate config = tradeConfig.getShipConfig(ship.getLv());
        // per minute
        double get = config.getProduct_speed() * s;

        double min = Math.min(sub, get);
        if (min > 0) {
            playerBiz.addPlayerCurrency(player, CurrencyKind.Shipping, (long) min, LogType.TradeDelivery.value(ship.getId()));
        }
        SeaTradeRoadNewTemplate city = tradeConfig.pickCity();
        if (city != null) {
            ship.setEndTime(now + city.getTime() * GameConstants.MINUTE);
            ship.setTargetId(city.getEnd_city());
        }
        Integer pickItemId = tradeConfig.pickItemId();
        if (pickItemId != null) {
            ship.setItemId(pickItemId);
        }
        ship.setBeginTime(now);
        ship.setFull(false);
        return true;
    }

    /**
     *  获取航运币上限
     * @param player
     * @return
     */
    public int getCompanyLimit(BasePlayer player){
        int companyLv = player.playerShipTrade().getCompany().getLv();
        SeaTradeBuildingTemplate buildingConfig = tradeConfig.getBuildingConfig(companyLv);
        if(buildingConfig == null){
            return 0;
        }
        Integer store = buildingConfig.getStore();
        if(player.getPlayerRecharge().haveSeasonVip()){
            // 至尊季卡 航运翻倍
           return store * commonValueConfig.getCommValue(CommonValueType.SeasonVipCard_Buf2);
        }
        return store;
    }

    public void exchange(Player player, long num, CurrencyKind kind, PlayerAssetEnum playerAssetEnum, List<Items> rewards){
        if(num <= 0){
            return;
        }

        String rate = "1";
        long add = (long) Double.parseDouble(CalculationUtil.multiply(String.valueOf(num), rate));
        Items items = new Items(playerAssetEnum.getTypeId(), num + add, ItemType.CURRENCY);
        rewards.add(items);
    }

}
