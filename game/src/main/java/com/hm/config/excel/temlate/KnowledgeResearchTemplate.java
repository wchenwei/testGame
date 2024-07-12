package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("knowledge_research")
public class KnowledgeResearchTemplate {
	private Integer id;
	private String name;
	private String des;
	private String icon;
	private Integer pre;
	private Integer gold;
	private Integer diamonds;

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
	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getPre() {
		return pre;
	}

	public void setPre(Integer pre) {
		this.pre = pre;
	}
	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}
	public Integer getDiamonds() {
		return diamonds;
	}

	public void setDiamonds(Integer diamonds) {
		this.diamonds = diamonds;
	}
}
