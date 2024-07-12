package com.hm.chsdk.event2.guide;

import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerRedisDataUtils;
import com.hm.observer.ObservableEnum;

/**
 * 用户成功创角触发
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2022/11/14 16:45
 */
@EventMsg(obserEnum = ObservableEnum.PlayerCreate)
public class GuideCompletedStartEvent extends CommonParamEvent {
    private String ser_td;

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.Task, 3001, "create_role_complete");
        this.ser_td = ServerRedisDataUtils.getServerRedisData(player.getId()).getOpenDate();
    }
}
