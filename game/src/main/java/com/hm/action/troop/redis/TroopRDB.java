package com.hm.action.troop.redis;

import com.google.common.collect.Lists;
import com.hm.model.player.Player;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.war.sg.troop.TankArmy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class TroopRDB {
    private ArrayList<TankPos> tankList = Lists.newArrayList();
    private int aircraftId;
    private long combat;

    public TroopRDB(Player player,WorldTroop worldTroop) {
        for (TankArmy tankArmy : worldTroop.getTroopArmy().getTankList()) {
            this.tankList.add(new TankPos(tankArmy.getId(),tankArmy.getIndex()));
        }
        this.combat = worldTroop.getTroopCombat(player);
    }

    public List<TankArmy> buildTankArmy() {
        return tankList.stream().map(e -> new TankArmy(e.getIndex(),e.getId())).collect(Collectors.toList());
    }
}
