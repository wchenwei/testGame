package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克改造-配件合成
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHTankPartsCompose)
public class TankPartsComposeEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);
        position = Convert.toInt(argv[1]);
        cnt_before = Convert.toInt(argv[2]);
        cnt_after = Convert.toInt(argv[3]);

        event_id = "8003";
        event_name = "坦克改造-配件合成";
    }

    /*
    * 坦克id	tankId
配件位置id	position
配件是否合成		Ture or False
当前配件碎片数量
改变后配件碎片数量

    * */
    private int position;
    private int cnt_before;
    private int cnt_after;

}
