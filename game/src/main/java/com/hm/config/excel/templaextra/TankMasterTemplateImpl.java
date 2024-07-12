package com.hm.config.excel.templaextra;

import com.google.common.collect.Maps;
import com.hm.config.excel.temlate.TankMasterTemplate;
import com.hm.enums.TankAttrType;
import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.util.TankAttrUtils;
import lombok.Getter;

import java.util.Map;

@Getter
@FileConfig("tank_master")
public class TankMasterTemplateImpl extends TankMasterTemplate {

    private Map<TankAttrType, Double> attAttrMap = Maps.newHashMap();

    @ConfigInit
    public void init(){
        this.attAttrMap = TankAttrUtils.str2TankAttrMap(getAttr_add(), ",", ":");
    }

}
