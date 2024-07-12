package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克专精
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.TankSpecialLvUp)
public class TankSpecialLvUpEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        type = Convert.toInt(argv[1]);
        lv = Convert.toInt(argv[2]);

        event_id = "8014";
        event_name = "坦克专精";
    }

    /*
    专精类型
专精等级
     */
    private int type;
    private int lv;
}
