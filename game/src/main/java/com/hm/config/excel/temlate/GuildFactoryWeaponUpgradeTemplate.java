package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_factory_weapon_upgrade")
public class GuildFactoryWeaponUpgradeTemplate {
	private Integer id;
	private Integer weapon_id;
	private Integer weapon_level;
	private Integer exp;
	private Integer exp_total;
	private String attri;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getWeapon_id() {
		return weapon_id;
	}

	public void setWeapon_id(Integer weapon_id) {
		this.weapon_id = weapon_id;
	}
	public Integer getWeapon_level() {
		return weapon_level;
	}

	public void setWeapon_level(Integer weapon_level) {
		this.weapon_level = weapon_level;
	}
	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}
	public Integer getExp_total() {
		return exp_total;
	}

	public void setExp_total(Integer exp_total) {
		this.exp_total = exp_total;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
}
