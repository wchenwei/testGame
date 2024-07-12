package com.hm.model.player;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-03
 */
@Data
public class MilitaryLineup {
    /**
     * pos index : tank id
     */
    private Map<Integer, Integer> tankMap = Maps.newConcurrentMap();
    /**
     * 军阵等级
     */
    private int lv;
    /**
     * 助力阵容等级
     */
    private transient int assistanceLv;
    private long assistanceValue;

    public void setTankMap(Map<Integer, Integer> tankMap) {
        this.tankMap.clear();
        tankMap.forEach((key, value) -> this.tankMap.put(key, value));
    }

    public void incLv() {
        incLv(1);
    }

    public void incLv(int n) {
        lv += n;
    }

    public String getMilitartString(Player player) {
        StringBuffer result = new StringBuffer();
        tankMap.forEach((key, value)->{
            if(result.length()>0){
                result.append(",");
            }
            result.append(String.format("%s:%s:%s", key, value, player.playerTank().getTank(value).getCombat()));
        });
        return result.toString();
    }

    /**
     * 重置，保留lv
     */
    public void reset() {
        tankMap.clear();
        assistanceLv = 0;
        assistanceValue = 0;
    }
}
