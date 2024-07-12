package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.CurrencyKind;
import com.hm.model.player.Player;

/**
 * @ClassName TankSkillEvent
 * @Deacription TankSkillEvent
 * @Author zxj
 * @Date 2022/3/8 9:25
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHTankSkillLVUp)
public class TankSkillEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        // tankid, skillid,lvbefore,lvafter,cashcost
        super.init(player, argv);
        skillid = Convert.toInt(argv[1]);
        Level_Before = Convert.toInt(argv[2]);
        Level_After = Convert.toInt(argv[3]);
        itemcost = Convert.toInt(argv[4]);
        costafter = player.playerCurrency().get(CurrencyKind.Cash);

        event_id = "8002";
        event_name = "坦克技能升级";
    }

    private int skillid;//技能编号
    private int Level_Before; //当前技能等级
    private int Level_After; //改变后技能等级
    private int itemcost;//消耗钞票数量
    private long costafter;//剩余钞票数量
}
