package com.hm.action.item;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.ItemNameConfig;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.config.excel.temlate.MoneyTypeTemplate;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.ItemType;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class ItemConfigBiz {
	@Resource
	private ItemConfig itemConfig;
	@Resource
	private ItemNameConfig itemNameConfig;
	@Resource
	private RechargeConfig rechargeConfig;
	@Resource
	private LanguageCnTemplateConfig languageCnTemplateConfig;
	
	
	public List<ItemName> createItemName() {
		List<ItemName> list = Lists.newArrayList();
		//加载资产名
		for (MoneyTypeTemplate template : itemNameConfig.getMoneyTypeList()) {
			list.add(new ItemName(ItemType.CURRENCY, template.getId(), template.getName()));
		}
		//背包道具
		for (ItemTemplate template : itemConfig.getItemTemplateList()) {
			list.add(new ItemName(ItemType.ITEM, template.getId(), template.getName()));
		}
		for (ItemName itemName : list) {
			String trueName = languageCnTemplateConfig.getValue(itemName.getName());
			if(trueName == null) {
				trueName = "未知";
			}
			itemName.setName(trueName);
		}
		return list;
	}
	public List<ItemName> createRechargeName() {
		List<ItemName> list = Lists.newArrayList();
		for (RechargeGiftTempImpl template : rechargeConfig.getRechargeTemplateList()) {
			list.add(new ItemName(template.getId(), template.getName()));
		}
		for (ItemName itemName : list) {
			String trueName = languageCnTemplateConfig.getValue(itemName.getName());
			if(trueName == null) {
				trueName = "未知";
			}
			itemName.setName(trueName);
		}
		return list;
	}
	
}
