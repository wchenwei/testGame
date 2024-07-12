
package com.hm.action.player.vo;
/**
 * Title: WarShopVO.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年8月4日 下午2:18:07
 * @version 1.0
 */
public class WarShopVO {

	private int tankId;
	
	private int count;//兑换数量
	
	private int allcount;//总量
	
	private int lastcount;//剩余数量
	
	private int buycount;//购买次数

	public int getTankId() {
		return tankId;
	}

	public void setTankId(int tankId) {
		this.tankId = tankId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getAllcount() {
		return allcount;
	}

	public void setAllcount(int allcount) {
		this.allcount = allcount;
	}

	public int getLastcount() {
		return lastcount;
	}

	public void setLastcount(int lastcount) {
		this.lastcount = lastcount;
	}

	public int getBuycount() {
		return buycount;
	}

	public void setBuycount(int buycount) {
		this.buycount = buycount;
	}
	
	
	
	
}

