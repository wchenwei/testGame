package com.hm.chsdk.event2.battle;

import com.hm.enums.BattleType;
import com.hm.model.player.Player;

/**
 * @author wyp
 * @description
 * @date 2022/3/10 9:58
 */
//@EventMsg(obserEnum = ObservableEnum.BattleSweepEvent)
public class BattleSweepEvent extends BattleEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);
        int type = (int) argv[0];
        int id = (int) argv[1];
        BattleType battleType = BattleType.getBattleType(type);
        switch (battleType) {
            case TowerBattle:
                // 精英扫荡
                this.event_id = "11008";
                this.event_name = "使命之路精英扫荡";
                this.battleId = id;
                break;
        }
    }

    private int battleId;
    private int itemId;
    private long num;
    private long residueNum;

}
