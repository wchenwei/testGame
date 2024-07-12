package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("dialogue_npc")
public class DialogueNpcTemplate {
	private Integer id;
	private String name;
	private String icon_big;
	private String animation_name;
	private String denomination;
	private Integer scale;
	private Integer enemy;

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
	public String getIcon_big() {
		return icon_big;
	}

	public void setIcon_big(String icon_big) {
		this.icon_big = icon_big;
	}
	public String getAnimation_name() {
		return animation_name;
	}

	public void setAnimation_name(String animation_name) {
		this.animation_name = animation_name;
	}
	public String getDenomination() {
		return denomination;
	}

	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public Integer getEnemy() {
		return enemy;
	}

	public void setEnemy(Integer enemy) {
		this.enemy = enemy;
	}
}
