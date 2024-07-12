package com.hm.chsdk.event2.commander;

import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @ClassName EquStoneDeComposeEvent
 * @Deacription 分级宝石
 * @Author zxj
 * @Date 2022/3/11 14:55
 * @Version 1.0
 **/
//@EventMsg(obserEnum = ObservableEnum.CHEquStoneDeCompose)
public class EquStoneDeComposeEvent extends CommanderEvent{
    @Override
    public void initCommanderData(Player player, Object... argv) {
        event_id = "9014";
        event_name = "指挥官装备回收";
        //stoneId, count, player.playerStone().getItemCount(stoneId), nextId, num
        List<Items> cost = (List<Items>) argv[0];
        this.equid = ItemCountUtil.getIds(cost);
        this.equcount = ItemCountUtil.getCounts(cost);
        List<Items> get = (List<Items>) argv[1];
        this.gettype = ItemCountUtil.getTypes(get);
        this.getid = ItemCountUtil.getIds(get);
        this.getcount = ItemCountUtil.getCounts(get);
    }

    private String equid;
    private String equcount;
    private String gettype;
    private String getid;
    private String getcount;

}
