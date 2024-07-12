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
 * @Description: 玩家特工数据
 * @date 2023/4/7 16:55
 */
@EventMsg(obserEnum = ObservableEnum.DayFirstLogin)
public class LoginAgentEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Agent)){
            return;
        }
        this.secret_list = player.playerAgent().getAllAgent()
                        .stream().map(e -> new CHAgent(e)).collect(Collectors.toList());
        loadEventType(CHEventType.Cultivate, 7003, "secret_cult");
    }

    private List<CHAgent> secret_list;

}
