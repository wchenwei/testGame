package com.hm.chsdk.event2.battle;

import com.hm.enums.BattleType;
import com.hm.model.player.Player;

/**
 * @author wyp
 * @description 参与战役
 * @date 2022/3/9 11:01
 */
//@EventMsg(obserEnum = ObservableEnum.BattleEvent)
public class TakeBattleEvent extends BattleEvent {

    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);
        BattleType battleType = (BattleType) argv[0];
        int missionId = (int) argv[1];
        int star = (int) argv[2];
        String tankIds = (String) argv[3];
        long combat = (long) argv[4];
        switch (battleType) {
            case ExperimentBattle:
                this.event_id = "11001";
                this.event_name = "突击战参与";
                this.battleId = missionId;
                this.result = star >= 0;
                break;

            case TowerBattle:
                super.event_id = "11007";
                super.event_name = "使命之路参与";
                this.tankCombat = combat;
                this.battleId = missionId;
                this.result = star >= 0;
                break;


        }

    }

    private int battleId;
    private long tankCombat;
    private int checkpoint;
    private int lastCheckpoint;
    private int oppoLv;
    private boolean result;
    private String tankId;

}
