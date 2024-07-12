package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克中控-原件装配
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHTankCtrlChange)
public class TankCtrlChangeEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        idx = Convert.toInt(argv[1]);
        oldEle = Convert.toInt(argv[2]);
        newEle = Convert.toInt(argv[3]);
        isAdd = newEle > 0;

        event_id = "8017";
        event_name = "坦克中控-原件装配";
    }

    /*
    坦克id
中控原件位置id
是否装配原件
当前装配原件id
改变后装配原件id
     */
    private int idx;
    private int oldEle;
    private int newEle;
    private boolean isAdd;
}
