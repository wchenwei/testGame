package com.hm.action.player.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.DailyTaskConfig;
import com.hm.config.excel.temlate.DailyTaskConfigTemplateImpl;
import com.hm.enums.ShopType;
import com.hm.enums.StatisticsType;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerCurrency;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;

/**
 * @Description: 玩家统计监听
 * @author siyunlong
 * @date 2018年12月14日 上午11:33:57
 * @version V1.0
 */
@Biz
public class PlayerStatisticsObserver implements IObserver {
	@Resource
    private DailyTaskConfig dailyTaskConfig;
	
    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.CostRes, this, 0);
        ObserverRouter.getInstance().registObserver(ObservableEnum.ShopBuy, this, 0);
        ObserverRouter.getInstance().registObserver(ObservableEnum.EliminateBandits, this, 0);
        ObserverRouter.getInstance().registObserver(ObservableEnum.DailyTaskComplete, this, 0);
        ObserverRouter.getInstance().registObserver(ObservableEnum.CityBattleResult, this, 0);
        ObserverRouter.getInstance().registObserver(ObservableEnum.ArenaFight, this, 0);
    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        // 资产消耗时,记录累计消耗数量
        if (observableEnum == ObservableEnum.CostRes) {
            int currencyKindIndex = Integer.parseInt(argv[0].toString());
            long count = Long.parseLong(argv[1].toString());
            if (CurrencyKind.Oil.getIndex() == currencyKindIndex) {
                player.getPlayerStatistics().addLifeStatistics(StatisticsType.OilUsed, count);
            } else if (CurrencyKind.Gold.getIndex() == currencyKindIndex) {
                player.getPlayerStatistics().addLifeStatistics(StatisticsType.GoldUsed, count);
            } else if (CurrencyKind.SysGold.getIndex() == currencyKindIndex) {
                player.getPlayerStatistics().addLifeStatistics(StatisticsType.GoldUsed, count);
            }
        }

        // 商店购买时,记录购买次数(限1,2号商店)
        if (observableEnum == ObservableEnum.ShopBuy) {
            int shopId = Integer.parseInt(argv[0].toString());
            if (shopId == ShopType.RestricteShop.getType() || shopId == ShopType.PartShop.getType()) {
                player.getPlayerStatistics().addLifeStatistics(StatisticsType.ArmsBuyTimes);
            }
        }

        if (observableEnum == ObservableEnum.EliminateBandits) {
            int times = Integer.parseInt(argv[0].toString());
            player.getPlayerStatistics().addLifeStatistics(StatisticsType.DepotTimes, times);
        }

        // 每日任务提交次数
        if (observableEnum == ObservableEnum.DailyTaskComplete) {
        	doDailyTaskComplete(player,(int)argv[0]);
        }

        // 统计城战升级次数
        if (observableEnum == ObservableEnum.CityBattleResult) {
            boolean isWin = Boolean.parseBoolean(argv[2].toString());
            if (isWin) {
                player.getPlayerStatistics().addLifeStatistics(StatisticsType.CityWarWinTimes);
            }
            //记录累计杀敌数
            player.getPlayerStatistics().addLifeStatistics(StatisticsType.CityWarKillTankNum,(int)argv[0]);
            //记录累计城战数
            player.getPlayerStatistics().addLifeStatistics(StatisticsType.CityWarTotalTimes);
        }
        if (observableEnum == ObservableEnum.ArenaFight) {
            boolean isWin = Boolean.parseBoolean(argv[0].toString());
            if (isWin) {
                player.getPlayerStatistics().addLifeStatistics(StatisticsType.ArenaWinTimes);
            }
        }
    }
    
    public void doDailyTaskComplete(Player player,int taskId) {
    	DailyTaskConfigTemplateImpl cfg = dailyTaskConfig.getDailyTaskCfg(taskId);
        if (cfg != null && cfg.getActive_id() == 0) {
        	player.getPlayerStatistics().addLifeStatistics(StatisticsType.DailyTaskComplete);
        }
    }
}
