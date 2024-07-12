package com.hm.chsdk.event2.tank;

import com.hm.model.player.Player;
import com.hm.model.tank.Tank;

/**
 * @ClassName TankLvUpEvent
 * @Deacription TankLvUpEvent
 * @Author zxj
 * @Date 2022/3/4 13:50
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.TankAddExp)
public class TankLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);
        //tank.getId(), itemId, spendCount, beforeLv
        int itemId = Integer.parseInt(argv[1].toString());
        int spendCount = Integer.parseInt(argv[2].toString());
        int beforeLv = Integer.parseInt(argv[3].toString());
        Tank tank = player.playerTank().getTank(tankId);
        this.Level_Before = beforeLv;
        this.lv = tank.getLv();
        this.exp = tank.getExp();
        this.itemid = itemId;
        this.itemcount = spendCount;

        event_id = "8001";
        event_name = "坦克升级";
    }

    private int Level_Before; //当前坦克等级
    private int lv; //改变后坦克等级
    private long exp;//经验
    private int itemid;//消耗升级工具id
    private int itemcount;//消耗升级工具数量
}
