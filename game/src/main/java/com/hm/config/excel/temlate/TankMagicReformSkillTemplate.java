package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_magic_reform_skill")
public class TankMagicReformSkillTemplate {
	private Integer id;
	private String attri;
	private Integer upgrade;
	private String name;
	private String desc;
	private String icon;
	private String des_indexes;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getAttri() {
		return attri;
	}

	public void setAttri(String attri) {
		this.attri = attri;
	}
	public Integer getUpgrade() {
		return upgrade;
	}

	public void setUpgrade(Integer upgrade) {
		this.upgrade = upgrade;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDes_indexes() {
		return des_indexes;
	}

	public void setDes_indexes(String des_indexes) {
		this.des_indexes = des_indexes;
	}
}
