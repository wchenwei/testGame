package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;
@FileConfig("kf_recharge_stage")
public class KfRechargeStageTemplate {
	private Integer id;
	private Integer rank_type;
	private Integer rank_mail;
	private String icon_resource;
	private Integer help;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRank_type() {
		return rank_type;
	}

	public void setRank_type(Integer rank_type) {
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
	public Integer getHelp() {
		return help;
	}

	public void setHelp(Integer help) {
		this.help = help;
	}
}
