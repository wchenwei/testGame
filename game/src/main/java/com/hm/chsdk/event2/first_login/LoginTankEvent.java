package com.hm.chsdk.event2.first_login;

import cn.hutool.core.collection.CollUtil;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.config.excel.TankConfig;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerTank;
import com.hm.observer.ObservableEnum;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家坦克数据
 * @date 2023/4/7 16:55
 */
@EventMsg(obserEnum = ObservableEnum.DayFirstLogin)
public class LoginTankEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        PlayerTank playerTank = player.playerTank();
        this.tank_list = TankConfig.SSTankIdList
                        .stream().map(e -> playerTank.getTank(e)).filter(Objects::nonNull)
                        .map(e -> new CHSSTank(e)).collect(Collectors.toList());
        if(CollUtil.isEmpty(tank_list)) {
            return;
        }
        loadEventType(CHEventType.Cultivate, 7001, "tank_cult");
    }

    private List<CHSSTank> tank_list;
}
