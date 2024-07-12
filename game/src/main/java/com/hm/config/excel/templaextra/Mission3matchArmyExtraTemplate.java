package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.Mission3matchArmyTemplate;
import com.hm.war.sg.troop.TankArmy;

import java.util.List;

@FileConfig("mission_3match_army")
public class Mission3matchArmyExtraTemplate extends Mission3matchArmyTemplate {
    public List<TankArmy> armyList = Lists.newArrayList();

    public void init() {
        List<Integer> npcIdList = StringUtil.splitStr2IntegerList(getEnemy_config(), ",");
        List<Integer> npcIndexList = StringUtil.splitStr2IntegerList(getEnemy_pos(), ",");
        armyList = createNpcArmy(npcIdList, npcIndexList);
    }

    public List<TankArmy> createNpcArmy(List<Integer> npcIdList, List<Integer> npcIndexList) {
        List<TankArmy> armyList = Lists.newArrayList();
        for (int i = 0; i < npcIdList.size(); i++) {
            TankArmy tankArmy = new TankArmy(npcIndexList.get(i), npcIdList.get(i));
            armyList.add(tankArmy);
        }
        return armyList;
    }

    public List<TankArmy> getTankArmy() {
        return armyList;
    }
}
