package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("active_cvout_stage")
public class ActiveCvoutStageTemplate {
	private Integer stage;
	private Integer rank_type;
	private Integer rank_base;
	private String cost_id;
	private String reward_final;
	private String icon_resource;
	private Integer help_type;
	private String cost_refresh;
	private String cost_piece;

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getRank_type() {
		return rank_type;
	}

	public void setRank_type(Integer rank_type) {
		this.rank_type = rank_type;
	}
	public Integer getRank_base() {
		return rank_base;
	}

	public void setRank_base(Integer rank_base) {
		this.rank_base = rank_base;
	}
	public String getCost_id() {
		return cost_id;
	}

	public void setCost_id(String cost_id) {
		this.cost_id = cost_id;
	}
	public String getReward_final() {
		return reward_final;
	}

	public void setReward_final(String reward_final) {
		this.reward_final = reward_final;
	}
	public String getIcon_resource() {
		return icon_resource;
	}

	public void setIcon_resource(String icon_resource) {
		this.icon_resource = icon_resource;
	}
	public Integer getHelp_type() {
		return help_type;
	}

	public void setHelp_type(Integer help_type) {
		this.help_type = help_type;
	}
	public String getCost_refresh() {
		return cost_refresh;
	}

	public void setCost_refresh(String cost_refresh) {
		this.cost_refresh = cost_refresh;
	}
	public String getCost_piece() {
		return cost_piece;
	}

	public void setCost_piece(String cost_piece) {
		this.cost_piece = cost_piece;
	}
}
