package com.hm.chsdk.event2.commander;

import com.hm.model.player.Player;

import java.util.List;

/**
 * @author wyp
 * @description 指挥官航母飞机升级
 * @date 2022/3/11 15:54
 */
//@EventMsg(obserEnum = ObservableEnum.AircraftStarUp)
public class AircraftStarUpEvent extends CommanderEvent {
    @Override
    public void initCommanderData(Player player, Object... argv) {
        this.event_id = "9035";
        this.event_name = "指挥官航母飞机升级";
        this.id = (String) argv[0];
        this.currentStar = (int) argv[2];
        this.afterStar = (int) argv[3];
        this.costIds = (List<String>) argv[4];
    }

    private String id;
    private int currentStar;
    private int afterStar;
    private List<String> costIds;
}
