package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.model.player.Player;

/**
 * 坦克精铸-配件精铸
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHTankJingzhu)
public class TankJingZhuEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        position = Convert.toInt(argv[1]);
        oldWashIds = Convert.toStr(argv[2]);
        newWashIds = Convert.toStr(argv[3]);
        lockStr = Convert.toStr(argv[4]);
        cost = Convert.toLong(argv[5]);
        remain = Convert.toLong(argv[6]);

        event_id = "8007";
        event_name = "坦克精铸-配件精铸";
    }

    /*
    坦克id
配件位置id
当前精铸属性id
变更后精铸属性
是否锁定
消耗金砖数量
剩余金砖数量

    * */
    private int position;
    private String oldWashIds, newWashIds, lockStr;
    private long cost;
    private long remain;

}
