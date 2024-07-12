package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.util.StringUtil;

import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-03
 */
public class PlayerMilitaryLineup extends PlayerDataContext {
    /**
     * MilitaryLineupTypeTemplateImpl::getId : {grid index : tank id}
     */
    private Map<Integer, MilitaryLineup> rec = Maps.newConcurrentMap();

    public Map<Integer, MilitaryLineup> getRec() {
        return rec;
    }

    public MilitaryLineup getMilitaryLineup(int id) {
        if (!rec.containsKey(id)) {
            MilitaryLineup militaryLineup = new MilitaryLineup();
            rec.put(id, militaryLineup);
        }
        return rec.get(id);
    }

    public String getMilitartLineMsg(Player player) {
        StringBuffer result = new StringBuffer();
        rec.forEach((key, value)->{
            if(result.length()>0){
                result.append("@");
            }
            MilitaryLineup data = rec.get(key);
            String tempMilitar = data.getMilitartString(player);
            if(StringUtil.isNullOrEmpty(tempMilitar)) {
                return;
            }
            result.append(key);
            result.append("&");

            result.append(tempMilitar);
        });
        return result.toString();
    }

    @Override
    protected void fillMsg(JsonMsg msg) {
        msg.addProperty("militaryLineup", this);
    }
}
