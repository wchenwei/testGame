/**
 * Project Name:SLG_GameFuture.
 * File Name:ItemBiz.java
 * Package Name:com.hm.action.item
 * Date:2017年12月28日下午5:10:22
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.
 *
*/
package com.hm.action.item;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.ItemNameConfig;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.config.excel.temlate.MoneyTypeTemplate;
import com.hm.enums.ItemType;
import com.hm.model.item.Items;

import javax.annotation.Resource;

/**
 * xjt 2019年11月19日10:21:04
 */
@Biz
public class ItemNameBiz {
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private LanguageCnTemplateConfig langeConfig;
	@Resource
	private ItemNameConfig itemNameConfig;

	/**
	 * 获取物品名称(暂时只有ItemType.ITEM类型，有需要自行添加)
	 * @param item
	 * @return
	 */
	public String getItemName(Items item){
		try {
			ItemType itemType= ItemType.getType(item.getType());
			if(itemType==null){
				return item.getId()+"";
			}
			switch(itemType){
				case CURRENCY:
					MoneyTypeTemplate moneyTypeTemplate = itemNameConfig.getMoneyType(item.getId());
					return langeConfig.getValue(moneyTypeTemplate.getName());
				case ITEM:
					ItemTemplate itemTemplate = itemConfig.getItemTemplateById(item.getId());
					return langeConfig.getValue(itemTemplate.getName());
			}
			return item.getId()+"";
		} catch (Exception e) {
			return item.getId()+"";
		}
	}
	
	

	
}













