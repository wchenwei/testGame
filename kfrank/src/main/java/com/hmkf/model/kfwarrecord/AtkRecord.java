package com.hmkf.model.kfwarrecord;

import cn.hutool.core.convert.Convert;
import com.hm.action.troop.client.ClientTroop;
import com.hmkf.config.KFLevelConstants;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AtkRecord {
    private String id;
    private String[] troopInfos;

    public AtkRecord(String id, List<ClientTroop> clientTroopList) {
        this.id = id;
        if(!KFLevelConstants.isNpc(this.id)) {
            this.troopInfos = new String[clientTroopList.size()];
            for (int i = 0; i < this.troopInfos.length; i++) {
                this.troopInfos[i] = clientTroopList.get(i).toTroopStr();
            }
        }
    }

    public int getIntId() {
        return Convert.toInt(this.id,0);
    }
}
