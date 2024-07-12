package com.hm.chsdk.event2.commander;

import com.hm.model.player.Player;

/**
 * @author wyp
 * @description 指挥官航母上阵飞机
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.AircraftCarrierEitEvent)
public class AircraftCarrierEitEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9034";
        this.event_name = "指挥官航母上阵飞机";
        this.index = (int) argv[0];
        this.uid = (String) argv[1];
        this.afterUid = (String) argv[2];
    }

    private int index;
    private String uid;
    private String afterUid;
}
