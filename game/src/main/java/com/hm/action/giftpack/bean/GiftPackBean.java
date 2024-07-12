package com.hm.action.giftpack.bean;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 新手礼包信息
 * @date 2024/5/13 19:41
 */
@Data
@NoArgsConstructor
public class GiftPackBean {
	//礼包id recharge_gift表里的id  301,302,303
	private int id;
	//礼包购买次数 //购买到第几天了
	private int count;
	//结束时间 和当前时间不是一天才可以购买
	private long time;

	public GiftPackBean(int id) {
		this.id = id;
		this.count = 1;
		this.time = System.currentTimeMillis();
	}

	public void setCount(int count) {
		this.count = count;
		this.time = System.currentTimeMillis();
	}
}



