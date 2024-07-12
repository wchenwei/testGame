package com.hm.chsdk.event2.commander;

import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description 指挥官勋章升级
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.MedalMaxLvUpEvent)
public class MedalMaxLvUpEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9025";
        this.event_name = "指挥官勋章升级";
        this.medalId = (int) argv[0];
        this.lv = (String) argv[1];
        this.afterLv = (String) argv[2];
        List<Items> cost = (List<Items>) argv[3];
        this.costItemsId = ItemCountUtil.getIds(cost);
        this.residueItems = ItemCountUtil.getRemain(player, cost);
        this.costItemsCount = ItemCountUtil.getCounts(cost);
    }

    private int medalId;
    private String lv;
    private String afterLv;
    private String costItemsId;
    private String costItemsCount;
    private String residueItems;
}
