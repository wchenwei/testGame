package com.hm.config.excel.temlate;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import com.hm.util.StringUtil;

import java.util.Map;

/**
 * Description:
 * User: yang xb
 * Date: 2018-08-22
 */
@FileConfig("item_tank_skill_type")
public class ItemTankSkillTypeTemplateImpl extends ItemTankSkillTypeTemplate {
    private Items changeItem;
    private Map<Integer, String> addParameter = Maps.newHashMap();

	public void init() {
        changeItem = ItemUtils.str2Item(this.getChange(), ":");
        
        if(!StringUtil.isNullOrEmpty(this.getParameters())) {
        	initParameter();
        }
    }

    public Items getChangeItem() {
        return changeItem;
    }
    
    
    public String getAddParameterByLv(int lv) {
		return addParameter.get(lv);
	}

    private void initParameter() {
    	for(String str :this.getParameters().split(",")) {
    		String[] dataArr = str.split("=");
    		addParameter.put(Integer.parseInt(dataArr[0].replaceAll("[\\[\\]]", "")), dataArr[1]);
    	}
    }
    
}










