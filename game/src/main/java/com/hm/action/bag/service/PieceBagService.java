package com.hm.action.bag.service;

import com.google.common.collect.Lists;
import com.hm.config.PartsConfig;
import com.hm.config.excel.temlate.PartsTemplate;
import com.hm.config.excel.templaextra.PartsExtraTemplate;
import com.hm.model.item.Items;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class PieceBagService extends AbstractBagService {
	@Resource
	private PartsConfig partsConfig;

	@Override
	public boolean isCanSell(int itemId) {
		PartsTemplate template = partsConfig.getParts(itemId);
		if(template==null){
			return false;
		}
		//1，是卖，2是兑换
		return template.getSale()==1 ||template.getSale()==2;
	}

	@Override
	public List<Items> sellReward(int itemId,long count) {
		PartsExtraTemplate template = partsConfig.getParts(itemId);
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
