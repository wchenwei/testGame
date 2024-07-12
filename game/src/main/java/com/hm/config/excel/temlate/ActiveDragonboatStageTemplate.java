package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_dragonBoat_stage")
public class ActiveDragonboatStageTemplate {
	private Integer id;
	private String rank_type;
	private String mail_id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getRank_type() {
		return rank_type;
	}

	public void setRank_type(String rank_type) {
		this.rank_type = rank_type;
	}
	public String getMail_id() {
		return mail_id;
	}

	public void setMail_id(String mail_id) {
		this.mail_id = mail_id;
	}
}
