package com.hm.chsdk.event2.battle;

import com.google.common.collect.Lists;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.chsdk.event2.battle.vo.MissionBattleTankVo;
import com.hm.model.battle.BaseBattle;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;

import java.util.List;

/**
 * @ClassName BattleMaxLvEvent
 * @Deacription 玩家当前玩法最高关卡出现结果触发
 * @Author zxj
 * @Date 2023-03-06 13:39
 * @Version 1.0
 **/
@EventMsg(obserEnum = ObservableEnum.ChBattleMaxLvEvent)
public class BattleMaxLvEvent extends CommonParamEvent {
    private int method_id;
    private int pass_id;
    private List<MissionBattleTankVo> pass_tank = Lists.newArrayList();
    ;
    private boolean fight_result;
    //突击战 3; 使命之路 8; 战场寻宝 10;  夺宝奇兵 9; 决胜千里 13;  现代战争 12;  巅峰挑战 15
    //ExperimentBattle(3,"英雄试炼(闪击战)") TowerBattle(8,"爬塔(使命之路)") TreasureBattle(9,"夺宝奇兵") RecoveryBattle(10,"战场寻宝")
    //ModernBattle(12,"现代战争") FarWinBattle(13,"决胜千里") PeakChallengeBattle(15,"巅峰挑战")
    private static List<Integer> sendBattleType = Lists.newArrayList();

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.PVE, 5003, "pass_result");
        BaseBattle battle = (BaseBattle) argv[0];
        if (!sendBattleType.contains(battle.getBattleId())) {
            return;
        }
        int missionId = (int) argv[1];
        if (missionId < battle.getMaxHistory()) {
            return;
        }
        int star = (int) argv[2];
        List<Integer> tankIds = (List<Integer>) argv[3];
        this.method_id = battle.getBattleId();
        this.pass_id = missionId;
        this.fight_result = star > 0;

        tankIds.forEach(e -> {
            Tank tempTank = player.playerTank().getTank(e);
            if (null != tempTank) {
                pass_tank.add(new MissionBattleTankVo(tempTank));
            }
        });
    }
}
