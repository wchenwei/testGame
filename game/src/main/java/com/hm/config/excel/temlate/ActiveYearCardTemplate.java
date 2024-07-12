package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_year_card")
public class ActiveYearCardTemplate {
	private Integer id;
	private String name;
	private String desc;
	private String icon;
	private Integer quality;
	private Integer type;
	private String effect;
	private String item_unlock;
	private Integer card_limit;

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
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}
	public String getItem_unlock() {
		return item_unlock;
	}

	public void setItem_unlock(String item_unlock) {
		this.item_unlock = item_unlock;
	}
	public Integer getCard_limit() {
		return card_limit;
	}

	public void setCard_limit(Integer card_limit) {
		this.card_limit = card_limit;
	}
}
