package com.hm.log;

import com.hm.action.commander.biz.EquipmentBiz;
import com.hm.chsdk.event2.CHPushUtil;
import com.hm.config.excel.TankConfig;
import com.hm.enums.ActionType;
import com.hm.enums.GameTaskType;
import com.hm.enums.KfType;
import com.hm.enums.TankRareType;
import com.hm.libcore.annotation.Biz;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.troop.TroopServerContainer;
import com.hm.util.MathUtils;
import com.hm.war.sg.setting.TankSetting;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AliLogActionBiz
 * @Deacription 玩家，行为日志
 * @Author zxj
 * @Date 2022/3/8 16:31
 * @Version 1.0
 **/
@Biz
public class AliLogActionBiz implements IObserver {
    @Resource
    private LogBiz logBiz;
    @Resource
    private EquipmentBiz equipmentBiz;
    @Resource
    private TankConfig tankConfig;

    @Override
    public void registObserverEnum() {
        ObserverRouter.getInstance().registObserver(ObservableEnum.TroopEnterCity, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.KfScuffleSignup, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.KfKingSignup, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.MainFbFail, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.MilitaryLvUp, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.CarLv, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.MainTaskAdd, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.MainTaskComplete, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.DailyTaskComplete, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.ActivityTaskComplate, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.StoneLvUp, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.ChangeStone, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.StrengthenEqu, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.TankLv, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.TankStarUp, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.DriverLv, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.TroopChange, this);
        ObserverRouter.getInstance().registObserver(ObservableEnum.ComposeEqu, this);

    }

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
        switch(observableEnum) {
            case KfKingSignup:
            case KfScuffleSignup:
                KfType kfType = (KfType) argv[0];
                logBiz.addPlayerActionLog(player, ActionType.KFSingUp, String.valueOf(kfType.getType()));
                CHPushUtil.playerKfJoin(player, kfType);
                break;
            case MainFbFail:
                int missionId = (int) argv[0];
                logBiz.addPlayerActionLog(player, ActionType.MainFbFail, String.valueOf(missionId));
                break;
            case MilitaryLvUp:
                logBiz.addPlayerActionLog(player, ActionType.MilitaryLv, player.playerCommander().getMilitaryLv()+"");
                break;
            case CarLv:
                if ((boolean)argv[0]){
                    logBiz.addPlayerActionLog(player, ActionType.CarLv, player.playerCommander().getCarLv()+"");
                }
                break;
            case MainTaskAdd:
                logBiz.addTaskAddLog(player, GameTaskType.T1, (int)argv[0]);
                break;
            case MainTaskComplete:
                logBiz.addTaskCompleteLog(player, GameTaskType.T1, (int)argv[0], (long) argv[1]);
                break;
            case DailyTaskComplete:
                logBiz.addTaskCompleteLog(player, GameTaskType.T2,(int) argv[0]);
                break;
            case ActivityTaskComplate:
                logBiz.addTaskCompleteLog(player, GameTaskType.T3, (int) argv[0]);
                break;
            case StoneLvUp:
            case ChangeStone:
                logBiz.addPlayerEquipLog(player, ActionType.Stone.getCode(), equipmentBiz.getStoneAvgLvs(player));
                break;
            case StrengthenEqu:
            case ComposeEqu:
                logBiz.addPlayerEquipLog(player, ActionType.StrengthLv.getCode(), equipmentBiz.getEquipStrengthLvs(player));
                break;
            case TankLv:
            case TankStarUp:
            case DriverLv:
                int tankId = (int)argv[0];
                List<Integer> troopTankList = TroopServerContainer.of(player).getPlayerTroopTankList(player);
                if (troopTankList.contains(tankId)){
                    logBiz.addPlayerTroopLog(player);
                }
                break;
            case TroopChange:
                logBiz.addPlayerTroopLog(player);
                break;
        }



    }

    public void buildPlayerTroopsMsg(BasePlayer player, SlgAliLog log){
        List<WorldTroop> troops = TroopServerContainer.of(player).getWorldTroopByPlayer(player);
        for (WorldTroop troop : troops) {
            buildTroopMsg(player, log, troop);
        }
    }

    private void buildTroopMsg(BasePlayer player, SlgAliLog log, WorldTroop troop) {
        int index = troop.getTroopInfo().getIndex();
        List<Integer> tankIdList = troop.getTroopArmy().getTankIdList();
        List<Tank> tankList = player.playerTank().getTankByIds(tankIdList);
        int total = tankList.size();
        log.put("lv"+index,  total > 0 ? MathUtils.div(tankList.stream().mapToInt(Tank::getLv).sum(), total) : 0);
        if (index <= 3){
            int fTotal = 0;
            int fStar = 0;
            int fDriverLv = 0;
            for (Tank tank : tankList) {
                TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
                if (tankSetting.getRare() > TankRareType.N.getType()){
                    fTotal ++;
                    fStar += tank.getStar();
                    fDriverLv += tank.getDriver().getLv();
                }
            }
            log.put("fStar"+index ,fTotal > 0 ? MathUtils.div(fStar, fTotal) : 0);
            log.put("fDriverLv"+index , fTotal > 0 ?MathUtils.div(fDriverLv, fTotal): 0);
        }
    }

}
