package com.hm.chsdk.event2.battle;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.chsdk.event2.CHEventType;
import com.hm.chsdk.event2.CommonParamEvent;
import com.hm.chsdk.event2.battle.vo.MissionBattleTankVo;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;

import java.util.List;

/**
 * @ClassName MissionBattleEvent
 * @Deacription 主线副本
 * @Author zxj
 * @Date 2023-02-28 10:15
 * @Version 1.0
 **/
public abstract class MissionBattleEvent extends CommonParamEvent {
    private int fight_id = 0;
    private List<MissionBattleTankVo> fight_tank = Lists.newArrayList();
    private boolean fight_result = false;

    @Override
    public void init(Player player, Object... argv) {
        loadEventType(CHEventType.PVE, 5001, "camp_result");
        this.fight_id = (int) argv[0];
        List<Integer> tankIdList = (List<Integer>) argv[1];
        this.initTankMsg(player, tankIdList);
    }

    private void initTankMsg(Player player, List<Integer> tankIdList) {
        if (CollUtil.isEmpty(tankIdList)) {
            return;
        }
        tankIdList.forEach(e -> {
            Tank tempTank = player.playerTank().getTank(e);
            if (null != tempTank) {
                fight_tank.add(new MissionBattleTankVo(tempTank));
            }
        });
    }

    public void setFightResult(boolean fightResult) {
        this.fight_result = fightResult;
    }
}
