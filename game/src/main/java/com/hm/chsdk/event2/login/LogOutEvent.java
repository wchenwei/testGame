package com.hm.chsdk.event2.login;

import com.hm.libcore.util.TimeUtils;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;

import java.util.Date;

/**
 * @ClassName LogOutEvent
 * @Deacription LogOutEvent
 * @Author zxj
 * @Date 2022/3/3 14:02
 * @Version 1.0
 **/
@EventMsg(obserEnum = ObservableEnum.PlayerLoginOut)
public class LogOutEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Login, 2002, "logout");

        this.online_time = TimeUtils.getDifferSecs(player.playerBaseInfo().getLastLoginDate(), new Date());

    }

    private long online_time;

}
