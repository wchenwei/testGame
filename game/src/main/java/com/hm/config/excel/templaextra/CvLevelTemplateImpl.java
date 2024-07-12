package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CvLevelTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-12-15 11:45
 **/
@FileConfig("cv_level")
public class CvLevelTemplateImpl extends CvLevelTemplate {
    private List<Items> costItems = Lists.newArrayList();
    private Map<TankAttrType,Double> tankAttrMap = Maps.newConcurrentMap();

    public void init(){
        if (StrUtil.isNotEmpty(getCost())){
            costItems = ItemUtils.str2DefaultItemList(getCost());
        }
        if (StrUtil.isNotEmpty(getAttri())){
            tankAttrMap = TankAttrUtils.str2TankAttrMap(getAttri(),",",":");
        }
    }

    public List<Items> getCostItems() {
        return costItems;
    }

    public Map<TankAttrType, Double> getTankAttrMap() {
        return tankAttrMap;
    }
}
