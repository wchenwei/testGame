package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CvEngineLevelTemplate;
import com.hm.enums.TankAttrType;
import com.hm.enums.TankType;
import com.hm.model.item.Items;
import com.hm.model.tank.TankAttr;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-12-15 11:45
 **/
@FileConfig("cv_engine_level")
public class CvEngineLevelTemplateImpl extends CvEngineLevelTemplate {
    private Map<TankAttrType, Double> tankAttrMap = Maps.newConcurrentMap();
    private Map<Integer, TankAttr> totalTankTypeAttrMap = Maps.newConcurrentMap();
    private List<Items> costItems = Lists.newArrayList();

    public void init(){
        if (StrUtil.isNotEmpty(getAttri())){
            tankAttrMap = TankAttrUtils.str2TankAttrMap(getAttri(), ":");
        }
        if (StrUtil.isNotBlank(getCost())){
            costItems = ItemUtils.str2DefaultItemList(getCost());
        }
    }

    public void initAttr(List<CvEngineLevelTemplateImpl> templateList){
        templateList.stream().filter(template -> template.getId() <= getId()).forEach(template -> {
            if (template.getAttri_type() == 0){
                for (TankType tankType : TankType.values()){
                   addAttr(tankType.getType(), template.getTankAttrMap());
                }
            }else {
                addAttr(template.getAttri_type(),template.getTankAttrMap());
            }
        });
    }

    private void addAttr(Integer tankType,  Map<TankAttrType, Double> attrMap){
        TankAttr tankAttr = totalTankTypeAttrMap.get(tankType);
        if (tankAttr == null){
            tankAttr = new TankAttr();
            tankAttr.addAttr(attrMap);
            totalTankTypeAttrMap.put(tankType, tankAttr);
        }else {
            tankAttr.addAttr(attrMap);
        }
    }

    public Map<TankAttrType, Double> getTankAttrMap() {
        return tankAttrMap;
    }

    public Map<Integer, TankAttr> getTotalTankTypeAttrMap() {
        return totalTankTypeAttrMap;
    }
    
    public List<Items> getCostItems() {
        return costItems;
    }
}
