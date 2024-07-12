
package com.hm.action.player.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: OpendWarShopVO.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年8月4日 下午2:17:51
 * @version 1.0
 */
public class OpendWarShopVO {

	
	private long warcon;//当前军功（军功币）
	
	private List<WarShopVO> common = new ArrayList<WarShopVO>();//普通
	
	private List<WarShopVO> gem = new ArrayList<WarShopVO>();//珍宝

	public long getWarcon() {
		return warcon;
	}

	public void setWarcon(long warcon) {
		this.warcon = warcon;
	}

	public List<WarShopVO> getCommon() {
		return common;
	}

	public void setCommon(List<WarShopVO> common) {
		this.common = common;
	}

	public List<WarShopVO> getGem() {
		return gem;
	}

	public void setGem(List<WarShopVO> gem) {
		this.gem = gem;
	}
	
	
	
}

