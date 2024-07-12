package com.hm.chsdk.event2.commander;

import com.hm.model.player.Player;

/**
 * @author wyp
 * @description 指挥官更换勋章
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.MedalIdChange)
public class MedalIdChangeEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9024";
        this.event_name = "指挥官更换勋章";
        this.currentId = (int) argv[0];
        this.afterId = (int) argv[1];
    }

    private int currentId;
    private int afterId;
}
