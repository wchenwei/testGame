package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("mission_event_buff")
public class MissionEventBuffTemplate {
	private Integer id;
	private Integer type;
	private String name;
	private String desc;
    private String icon;
	private Integer skill_id;
	private String para;

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
	public Integer getSkill_id() {
		return skill_id;
	}

	public void setSkill_id(Integer skill_id) {
		this.skill_id = skill_id;
	}
	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}
}
