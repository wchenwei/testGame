package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.CvDrawConfigTemplate;
import com.hm.model.item.Items;
import com.hm.model.weight.RandomRatio;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * @description:
 * @author: chenwei
 * @create: 2020-12-15 11:48
 **/
@FileConfig("cv_draw_config")
public class CvDrawConfigTemplateImpl extends CvDrawConfigTemplate {
	private RandomRatio onceLibRatio;//单抽库
	private RandomRatio tenLibRatio;//十连库
	private RandomRatio smallLuckRatio;//十次必出紫橙库
	private List<Items> onceCosts = Lists.newArrayList();
	private List<Items> tenCosts = Lists.newArrayList();
    public void init(){
    	onceCosts = ItemUtils.str2ItemList(this.getCost_once(), ",", ":");
    	tenCosts = ItemUtils.str2ItemList(this.getCost_ten(), ",", ":");
    	this.onceLibRatio = new RandomRatio(this.getLibrary_once());
    	this.tenLibRatio = new RandomRatio(this.getLibrary_ten_normal());
    	this.smallLuckRatio = new RandomRatio(this.getLibrary_ten_spe());
    }
    
    public int randomOnceLib() {
    	return onceLibRatio.randomRatio();
    }
    
    public int randomTenLib() {
    	return tenLibRatio.randomRatio();
    }
    public int randomSmallLuckLib() {
    	return smallLuckRatio.randomRatio();
    }

	public List<Items> getOnceCosts() {
		return onceCosts;
	}

	public List<Items> getTenCosts() {
		return tenCosts;
	}
    
    
}
