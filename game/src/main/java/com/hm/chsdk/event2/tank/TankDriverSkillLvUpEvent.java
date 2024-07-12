package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克车长-车长技能等级
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHTankDriverSkillLvUp)
public class TankDriverSkillLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        skillId = Convert.toInt(argv[1]);
        lv = Convert.toInt(argv[2]);
        costId = Convert.toInt(argv[3]);
        costCnt = Convert.toInt(argv[4]);
        remain = Convert.toLong(argv[5]);

        event_id = "8010";
        event_name = "坦克车长-车长技能等级";
    }

    /*
    坦克id
车长技能位置id
变更强化等级
消耗道具id
消耗道具数量
剩余道具数量
    * */
    private int skillId;
    private int lv;
    private int  costId;
    private int costCnt;
    private long remain;
}
