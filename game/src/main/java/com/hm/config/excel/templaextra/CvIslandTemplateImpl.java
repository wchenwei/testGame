package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CvIslandTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CvIslandTemplateImpl
 * @Deacription 舰岛
 * @Author zxj
 * @Date 2021/11/2 11:41
 * @Version 1.0
 **/
@FileConfig("cv_island")
public class CvIslandTemplateImpl extends CvIslandTemplate {
    private List<Items> costItems = Lists.newArrayList();
    private Map<TankAttrType,Double> tankAttrMap = Maps.newConcurrentMap();

    private Map<TankAttrType,Double> tankAttrExtraMap = Maps.newConcurrentMap();

    public void init(){
        if (StrUtil.isNotEmpty(getCost())){
            costItems = ItemUtils.str2DefaultItemList(getCost());
        }
        if (StrUtil.isNotEmpty(getAttri())){
            tankAttrMap = TankAttrUtils.str2TankAttrMap(getAttri(),",",":");
        }
        if (StrUtil.isNotEmpty(getAttri_extra())){
            tankAttrExtraMap = TankAttrUtils.str2TankAttrMap(getAttri_extra(),",",":");
        }
    }

    public List<Items> getCostItems() {
        return costItems;
    }

    public Map<TankAttrType,Double> getTankAttrMap() {
        return tankAttrMap;
    }

    public Map<TankAttrType, Double> getTankAttrExtraMap() {
        return tankAttrExtraMap;
    }
}
