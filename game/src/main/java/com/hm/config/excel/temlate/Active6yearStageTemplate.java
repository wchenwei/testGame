package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_6year_stage")
public class Active6yearStageTemplate {
	private Integer id;
	private String cost_circle;
	private Integer base_recharge_vip;
	private String reward_recharge_vip;
	private String rank_type;
	private Integer rank_mail;
	private String icon_resource;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCost_circle() {
		return cost_circle;
	}

	public void setCost_circle(String cost_circle) {
		this.cost_circle = cost_circle;
	}
	public Integer getBase_recharge_vip() {
		return base_recharge_vip;
	}

	public void setBase_recharge_vip(Integer base_recharge_vip) {
		this.base_recharge_vip = base_recharge_vip;
	}
	public String getReward_recharge_vip() {
		return reward_recharge_vip;
	}

	public void setReward_recharge_vip(String reward_recharge_vip) {
		this.reward_recharge_vip = reward_recharge_vip;
	}
	public String getRank_type() {
		return rank_type;
	}

	public void setRank_type(String rank_type) {
		this.rank_type = rank_type;
	}
	public Integer getRank_mail() {
		return rank_mail;
	}

	public void setRank_mail(Integer rank_mail) {
		this.rank_mail = rank_mail;
	}
	public String getIcon_resource() {
		return icon_resource;
	}

	public void setIcon_resource(String icon_resource) {
		this.icon_resource = icon_resource;
	}
}
