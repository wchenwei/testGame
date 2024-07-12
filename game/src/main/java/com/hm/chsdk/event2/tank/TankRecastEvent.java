package com.hm.chsdk.event2.tank;

import cn.hutool.core.convert.Convert;
import com.hm.chsdk.event2.commander.ItemCountUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * 坦克改造-重铸
 *
 * @author xb
 */
//@EventMsg(obserEnum = ObservableEnum.CHTankRecast)
public class TankRecastEvent extends TankEvent {
    @Override
    public void init(Player player, Object... argv) {
        super.init(player, argv);

        type = Convert.toInt(argv[1]);
        cost = Convert.toInt(argv[2]);
        remain = Convert.toLong(argv[3]);

        List<Items> item = (List<Items>) argv[4];
        this.itemid = ItemCountUtil.getIds(item);
        this.count = ItemCountUtil.getCounts(item);

        event_id = "8005";
        event_name = "坦克改造-重铸";
    }

    /*
坦克id
重铸类型
消耗重铸工具id
剩余重铸工具数量
    * */
    private int type;
    private long cost;
    private long remain;
    private String itemid;
    private String count;

}
