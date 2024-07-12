package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("skill_effect")
public class SkillEffectTemplate {
	private Integer id;
	private String icon;
	private String tx;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTx() {
		return tx;
	}

	public void setTx(String tx) {
		this.tx = tx;
	}
}
