package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("guide_book")
public class GuideBookTemplate {
	private Integer id;
	private Integer type;
	private Integer show;
	private String chapter_name;
	private String target_city;
	private String target_desc;
	private String dialogue;
	private String reward;

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
	public Integer getShow() {
		return show;
	}

	public void setShow(Integer show) {
		this.show = show;
	}
	public String getChapter_name() {
		return chapter_name;
	}

	public void setChapter_name(String chapter_name) {
		this.chapter_name = chapter_name;
	}
	public String getTarget_city() {
		return target_city;
	}

	public void setTarget_city(String target_city) {
		this.target_city = target_city;
	}
	public String getTarget_desc() {
		return target_desc;
	}

	public void setTarget_desc(String target_desc) {
		this.target_desc = target_desc;
	}
	public String getDialogue() {
		return dialogue;
	}

	public void setDialogue(String dialogue) {
		this.dialogue = dialogue;
	}
	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
