package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("campaign_leaf_display")
public class CampaignLeafDisplayTemplate {
	private Integer index;
	private Integer campaign_id;
	private String name;
	private String icon;
	private String reward_tips;
	private String reward_icon;
	private String open_day;
	private Integer limit_country;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getCampaign_id() {
		return campaign_id;
	}

	public void setCampaign_id(Integer campaign_id) {
		this.campaign_id = campaign_id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getReward_tips() {
		return reward_tips;
	}

	public void setReward_tips(String reward_tips) {
		this.reward_tips = reward_tips;
	}
	public String getReward_icon() {
		return reward_icon;
	}

	public void setReward_icon(String reward_icon) {
		this.reward_icon = reward_icon;
	}
	public String getOpen_day() {
		return open_day;
	}

	public void setOpen_day(String open_day) {
		this.open_day = open_day;
	}
	public Integer getLimit_country() {
		return limit_country;
	}

	public void setLimit_country(Integer limit_country) {
		this.limit_country = limit_country;
	}
}
