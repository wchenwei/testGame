package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MilitaryLineupAssistanceTemplate;
import com.hm.enums.TankAttrType;
import com.hm.util.TankAttrUtils;

import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2019-09-03
 */
@FileConfig("military_lineup_assistance")
public class MilitaryLineupAssistanceTemplateImpl extends MilitaryLineupAssistanceTemplate {
    private Map<TankAttrType, Double> tankAttrMap = Maps.newConcurrentMap();

    public void init() {
        this.tankAttrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ",", ":");
    }

    public Map<TankAttrType, Double> getTankAttrMap() {
        return tankAttrMap;
    }
}
