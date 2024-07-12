package com.hmkf.model;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.hm.action.troop.client.ClientTroop;
import com.hm.war.sg.troop.TankArmy;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KfTroops extends KFPlayerDataContext {
    private List<TankArmy> tanks = Lists.newArrayList();
    private int aircraftId;
    private long combat;


    public List<TankArmy> getTanks() {
        return tanks;
    }


    public long getCombat() {
        return combat;
    }

    public void setCombat(long combat) {
        this.combat = combat;
    }

}
