package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guild_employ")
public class GuildEmployTemplate {
	private Integer id;
	private String name;
	private Integer npcid;
	private String desc;
	private String tips;
	private String points_count;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Integer getNpcid() {
		return npcid;
	}

	public void setNpcid(Integer npcid) {
		this.npcid = npcid;
	}
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}
	public String getPoints_count() {
		return points_count;
	}

	public void setPoints_count(String points_count) {
		this.points_count = points_count;
	}
}
