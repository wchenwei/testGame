package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ShopTypeTemplate;
import com.hm.model.item.Items;
import com.hm.util.ItemUtils;

import java.util.List;
import java.util.Map;
@FileConfig("shop_type")
public class ShopTypeExtraTemplate extends ShopTypeTemplate {
	private List<Items> costItemList = Lists.newArrayList();
	private Map<Integer,Integer> limitMap = Maps.newConcurrentMap();
	
	public void init(){
		this.costItemList = ItemUtils.str2ItemList(this.getRefresh_cost(), ",",":");
		if(!StrUtil.isBlank(this.getRefresh_limit())){
			for(String strs:this.getRefresh_limit().split(",")){
				String[] str = strs.split(":");
				int goodsId = Integer.parseInt(str[0]);
				int count = Integer.parseInt(str[1]);
				this.limitMap.put(goodsId, count);
			}
		}
	}
	
	public Items getCostItem(int refershCount){
		if (costItemList.isEmpty()){
			return null;
		}
		return costItemList.get(Math.min(refershCount, costItemList.size()-1));
	}

	public List<Items> getCostItemList() {
		return costItemList;
	}
	
	public int getGoodsLimit(int goodsId){
		return limitMap.getOrDefault(goodsId,0);
	}
	

}
