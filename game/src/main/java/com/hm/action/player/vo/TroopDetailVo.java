package com.hm.action.player.vo;

import com.google.common.collect.Lists;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.war.sg.troop.TankArmy;
import lombok.Data;

import java.util.List;

@Data
public class TroopDetailVo {
    private int id;
    private List<TankSimpleVo> tankList = Lists.newArrayList();

    public void loadTroop(Player player, WorldTroop worldTroop) {
        for (TankArmy tankArmy : worldTroop.getTroopArmy().getTankList()) {
            Tank tank = player.playerTank().getTank(tankArmy.getId());
            this.tankList.add(new TankSimpleVo(tankArmy.getIndex(),tank));
        }
    }
}
