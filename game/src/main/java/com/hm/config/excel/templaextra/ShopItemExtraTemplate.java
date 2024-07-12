package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ShopItemTemplate;
import com.hm.model.weight.GoodsDrop;
import com.hm.util.StringUtil;

@FileConfig("shop_item")
public class ShopItemExtraTemplate extends ShopItemTemplate {
	private GoodsDrop drop;
	private int minLv;
	private int maxLv;
	public void init(){
		this.drop = new GoodsDrop(this.getGoods(),this.getRate());
		this.minLv = StringUtil.strToIntArray(getLevel_zone(), ",")[0];
		this.maxLv = StringUtil.strToIntArray(getLevel_zone(), ",")[1];
	}
	
	public GoodsDrop getDrop() {
		return drop;
	}

	public void setDrop(GoodsDrop drop) {
		this.drop = drop;
	}

	public int getMinLv() {
		return minLv;
	}
	public void setMinLv(int minLv) {
		this.minLv = minLv;
	}
	public int getMaxLv() {
		return maxLv;
	}
	public void setMaxLv(int maxLv) {
		this.maxLv = maxLv;
	}
	

}
