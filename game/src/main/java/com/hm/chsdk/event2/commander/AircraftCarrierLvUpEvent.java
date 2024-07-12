package com.hm.chsdk.event2.commander;

import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description 指挥官航母动力舱升级
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.AircraftCarrierLvUpEvent)
public class AircraftCarrierLvUpEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9031";
        this.event_name = "指挥官航母动力舱升级";
        this.lv = (int) argv[0];
        this.afterLv = (int) argv[1];
        List<Items> cost = (List<Items>) argv[2];
        this.costItemsId = ItemCountUtil.getIds(cost);
        this.residueItems = ItemCountUtil.getRemain(player, cost);
        this.costItemsCount = ItemCountUtil.getCounts(cost);
    }

    private int lv;
    private int afterLv;
    private String costItemsId;
    private String costItemsCount;
    private String residueItems;
}
