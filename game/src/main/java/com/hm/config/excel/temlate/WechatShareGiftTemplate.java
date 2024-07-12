package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("wechat_share_gift")
public class WechatShareGiftTemplate {
	private Integer id;
	private String count_zone;
	private String reward;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCount_zone() {
		return count_zone;
	}

	public void setCount_zone(String count_zone) {
		this.count_zone = count_zone;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
