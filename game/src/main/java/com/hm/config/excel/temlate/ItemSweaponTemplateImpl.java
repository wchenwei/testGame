/**  
 * Project Name:SLG_GameHot.
 * File Name:ItemSweaponTemplateImpl.java  
 * Package Name:com.hm.config.excel.temlate  
 * Date:2018年4月26日上午11:06:05  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */


package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;
import org.apache.commons.lang.StringUtils;

/**  
 * ClassName: ItemSweaponTemplateImpl. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年4月26日 上午11:06:05 <br/>  
 *  
 * @author zxj  
 * @version   
 */
@FileConfig("item_sweapon")
public class ItemSweaponTemplateImpl extends ItemSweaponTemplate {
	private Items items = new Items();
	private Items itemsBuy = new Items();
	private Integer[] randomArr = null;
	private Integer[] randomArrFree = null;

	public void init() {
		items = ItemUtils.str2Item(this.getPart_product(),":");
		itemsBuy = ItemUtils.str2Item(this.getPiece_price(),":");
		String weightCrit = this.getWeight_crit();
		if(!StringUtils.isEmpty(weightCrit)) {
			String[] tempStrArr = this.getWeight_crit().split(":");
			//循环放入到map中，key，跟SweaponEnum对应
			randomArr = new Integer[tempStrArr.length];
			for(int i=0; i<tempStrArr.length; i++) {
				randomArr[i] = Integer.parseInt(tempStrArr[i]);
			} 
		}
		String weightCritFree = this.getWeight_crit2();
		if(!StringUtils.isEmpty(weightCritFree)) {
			String[] tempStrArrFree = this.getWeight_crit2().split(":");
			//循环放入到map中，key，跟SweaponEnum对应
			randomArrFree = new Integer[tempStrArrFree.length];
			for(int i=0; i<tempStrArrFree.length; i++) {
				randomArrFree[i] = Integer.parseInt(tempStrArrFree[i]);
			} 
		}
	}
	
	public Items getItems() {
		return items;
	}
	public Integer[] getRandomArr() {
		return randomArr;
	}
	public Items getItemsBuy() {
		return itemsBuy;
	}
	public Integer[] getRandomArrFree() {
		return randomArrFree;
	}
}











