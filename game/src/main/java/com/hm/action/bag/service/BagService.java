package com.hm.action.bag.service;

import com.google.common.collect.Lists;
import com.hm.config.excel.ItemConfig;
import com.hm.config.excel.temlate.ItemTemplate;
import com.hm.model.item.Items;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class BagService extends AbstractBagService {
	@Resource
	private ItemConfig itemConfig;

	@Override
	public boolean isCanSell(int itemId) {
		ItemTemplate template = itemConfig.getItemTemplateById(itemId);
		if(template==null){
			return false;
		}
		//1，是卖，2是兑换
		return template.getSale()==1 || template.getSale()==2;
	}

	@Override
	public List<Items> sellReward(int itemId,long count) {
		ItemTemplate template = itemConfig.getItemTemplateById(itemId);
		if(template==null){
			return Lists.newArrayList();
		}
		return template.getSellReward().stream().map(t ->{
			Items item = t.clone();
			item.setCount(item.getCount()*count);
			return item;
		}).collect(Collectors.toList());
	}

}
