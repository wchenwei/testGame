package com.hm.chsdk.event2.login;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.enums.StatisticsType;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

/**
 * @ClassName LoginEvent
 * @Deacription LoginEvent
 * @Author zxj
 * @Date 2022/3/3 13:56
 * @Version 1.0
 **/
@EventMsg(obserEnum = ObservableEnum.PlayerLoginSuc)
public class LoginEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Login, 2001, "login");
        this.login_days = player.getPlayerStatistics().getLifeStatistics(StatisticsType.LOGIN_DAYS);
        this.military_rank = player.playerCommander().getMilitaryLv();
        this.car_level = player.playerCommander().getCarLv();
    }

    private long login_days;
    int military_rank;
    int car_level;
}
