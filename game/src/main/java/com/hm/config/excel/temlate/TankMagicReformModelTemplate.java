package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_magic_reform_model")
public class TankMagicReformModelTemplate {
	private Integer id;
	private String icon_b_gai;
	private String icon_l_gai;
	private String dead_effect_gai;
	private String csbPro_gai;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getIcon_b_gai() {
		return icon_b_gai;
	}

	public void setIcon_b_gai(String icon_b_gai) {
		this.icon_b_gai = icon_b_gai;
	}
	public String getIcon_l_gai() {
		return icon_l_gai;
	}

	public void setIcon_l_gai(String icon_l_gai) {
		this.icon_l_gai = icon_l_gai;
	}
	public String getDead_effect_gai() {
		return dead_effect_gai;
	}

	public void setDead_effect_gai(String dead_effect_gai) {
		this.dead_effect_gai = dead_effect_gai;
	}
	public String getCsbPro_gai() {
		return csbPro_gai;
	}

	public void setCsbPro_gai(String csbPro_gai) {
		this.csbPro_gai = csbPro_gai;
	}
}
