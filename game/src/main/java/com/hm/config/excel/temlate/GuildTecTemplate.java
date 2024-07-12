package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_tec")
public class GuildTecTemplate {
	private Integer id;
	private Integer type;
	private Integer tec_func;
	private Integer position;
	private Integer default_lock;
	private String next_tec;
	private Integer max_level;
	private Integer effective;
	private Integer tec_value;
	private String value;
	private String tec_upgrade;
	private String tec_icon;
	private String tec_name;
	private String tec_desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getTec_func() {
		return tec_func;
	}

	public void setTec_func(Integer tec_func) {
		this.tec_func = tec_func;
	}
	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
	public Integer getDefault_lock() {
		return default_lock;
	}

	public void setDefault_lock(Integer default_lock) {
		this.default_lock = default_lock;
	}
	public String getNext_tec() {
		return next_tec;
	}

	public void setNext_tec(String next_tec) {
		this.next_tec = next_tec;
	}
	public Integer getMax_level() {
		return max_level;
	}

	public void setMax_level(Integer max_level) {
		this.max_level = max_level;
	}
	public Integer getEffective() {
		return effective;
	}

	public void setEffective(Integer effective) {
		this.effective = effective;
	}
	public Integer getTec_value() {
		return tec_value;
	}

	public void setTec_value(Integer tec_value) {
		this.tec_value = tec_value;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	public String getTec_upgrade() {
		return tec_upgrade;
	}

	public void setTec_upgrade(String tec_upgrade) {
		this.tec_upgrade = tec_upgrade;
	}
	public String getTec_icon() {
		return tec_icon;
	}

	public void setTec_icon(String tec_icon) {
		this.tec_icon = tec_icon;
	}
	public String getTec_name() {
		return tec_name;
	}

	public void setTec_name(String tec_name) {
		this.tec_name = tec_name;
	}
	public String getTec_desc() {
		return tec_desc;
	}

	public void setTec_desc(String tec_desc) {
		this.tec_desc = tec_desc;
	}
}
