package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克精铸-配件升级
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHTankJingzhuLvup)
public class TankJingZhuLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        position = Convert.toInt(argv[1]);
        Strengthen_Before = Convert.toInt(argv[2]);
        Strengthen_After = Convert.toInt(argv[3]);
        partIds = Convert.toStr(argv[4]);
        partCnts = Convert.toStr(argv[5]);
        exp = Convert.toInt(argv[6]);

        event_id = "8006";
        event_name = "坦克精铸-配件升级";
    }

    /*
    坦克id
配件位置id
分解前配件等级
分解后配件等级
消耗配件碎片id
消耗配件碎片数量
分解后精铸经验

    * */
    private int position;
    private int Strengthen_Before;
    private int Strengthen_After;
    private String partIds;
    private String partCnts;
    private int exp;

}
