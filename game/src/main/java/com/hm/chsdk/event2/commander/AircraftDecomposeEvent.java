package com.hm.chsdk.event2.commander;

import com.hm.model.item.Items;
import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description 指挥官航母飞机拆解
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.AircraftDecomposeEvent)
public class AircraftDecomposeEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9033";
        this.event_name = "指挥官航母飞机拆解";
        this.uids = (List<String>) argv[0];
        List<Items> rewards = (List<Items>) argv[1];
        this.itemIds = ItemCountUtil.getIds(rewards);
        this.itemCount = ItemCountUtil.getCounts(rewards);
    }

    private List<String> uids;
    private String itemIds;
    private String itemCount;
}
