package com.hm.chsdk.event2.first_login;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家航母数据
 * @date 2023/4/7 16:55
 */
@EventMsg(obserEnum = ObservableEnum.DayFirstLogin)
public class LoginAircraftEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.AircraftCarrier)){
            return;
        }
        this.cabin_level = player.playerAircraftCarrier().getLv();
        this.cabin_build = player.playerAircraftCarrier().getEngineLv();
        this.air_list = player.playerAircraft().getAircraftMap().values()
                        .stream().map(e -> new CHIdLv(e.getId(),e.getStar())).collect(Collectors.toList());
        this.island_list = player.playerAircraftCarrier().getIsland().entrySet()
                        .stream().map(e -> new CHIdLv(e.getKey(),e.getValue())).collect(Collectors.toList());
        loadEventType(CHEventType.Cultivate, 7004, "cabin_cult");
    }

    private int cabin_level;
    private int cabin_build;

    private List<CHIdLv> air_list;
    private List<CHIdLv> island_list;

}
