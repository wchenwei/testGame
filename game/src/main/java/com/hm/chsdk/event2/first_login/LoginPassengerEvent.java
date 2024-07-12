package com.hm.chsdk.event2.first_login;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.chsdk.annotation.EventMsg;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.config.excel.TankConfig;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerTank;
import com.hm.model.tank.Tank;
import com.hm.observer.ObservableEnum;

import java.util.Arrays;
import java.util.List;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家成员数据
 * @date 2023/4/7 16:55
 */
@EventMsg(obserEnum = ObservableEnum.DayFirstLogin)
public class LoginPassengerEvent extends CommonParamEvent {

    @Override
    public void init(Player player, Object... argv) {
        if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Passenger)){
            return;
        }
        PlayerTank playerTank = player.playerTank();
        this.crewa_list = Lists.newArrayList();
        for (int tankId : TankConfig.SSTankIdList) {
            Tank tank = playerTank.getTank(tankId);
            if(tank != null) {
                String[] passengers = tank.getTankPassenger().getpassengers();
                if(Arrays.stream(passengers).anyMatch(e -> StrUtil.isNotEmpty(e))) {
                    this.crewa_list.add(new CHPassenger(tank,player));
                }
            }
        }
        loadEventType(CHEventType.Cultivate, 7005, "crew_cult");
    }

    private List<CHPassenger> crewa_list;
}
