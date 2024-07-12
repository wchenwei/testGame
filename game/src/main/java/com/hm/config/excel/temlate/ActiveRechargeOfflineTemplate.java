package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_recharge_offline")
public class ActiveRechargeOfflineTemplate {
	private Integer id;
	private String goods;
	private String desc;
	private Integer is_broadcast;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getIs_broadcast() {
		return is_broadcast;
	}

	public void setIs_broadcast(Integer is_broadcast) {
		this.is_broadcast = is_broadcast;
	}
}
