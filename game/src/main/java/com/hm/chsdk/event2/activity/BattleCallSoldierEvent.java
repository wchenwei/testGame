package com.hm.chsdk.event2.activity;

import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;

/**
 * @ClassName BattleCallSoldierEvent
 * @Deacription 沙场点兵
 * @Author zxj
 * @Date 2023-03-02 10:38
 * @Version 1.0
 **/
//运营确认不需要次统计
//@EventMsg(obserEnum = ObservableEnum.BattleCallSoldier)
public class BattleCallSoldierEvent extends CommonParamEvent {
    private int barrier_id; //关卡id
    private int barrier_star;//通关时星极
    private int version;

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Activity, 4015, "desert_bar");
        this.barrier_id = (int) argv[0];
        this.barrier_star = (int) argv[1];
        this.version = (int) argv[2];
    }
}
