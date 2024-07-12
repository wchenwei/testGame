package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_base")
public class ItemBaseTemplate {
	private Integer id;
	private String name;
	private String desc;
	private String icon;
	private Integer item_type;
	private Integer quality;
	private Integer player_lv;
	private Integer use;
	private Integer use_number;
	private Integer sale;
	private String price;
	private Integer value;
	private String player_arm;
	private Integer drop;
	private String pick_drop;
	private Integer rank;
	private Integer redpoint;
	private String from_shop;
	private String effect;
	private Integer tab_type;
	private String date_effective;

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
	public Integer getItem_type() {
		return item_type;
	}

	public void setItem_type(Integer item_type) {
		this.item_type = item_type;
	}
	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getPlayer_lv() {
		return player_lv;
	}

	public void setPlayer_lv(Integer player_lv) {
		this.player_lv = player_lv;
	}
	public Integer getUse() {
		return use;
	}

	public void setUse(Integer use) {
		this.use = use;
	}
	public Integer getUse_number() {
		return use_number;
	}

	public void setUse_number(Integer use_number) {
		this.use_number = use_number;
	}
	public Integer getSale() {
		return sale;
	}

	public void setSale(Integer sale) {
		this.sale = sale;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	public String getPlayer_arm() {
		return player_arm;
	}

	public void setPlayer_arm(String player_arm) {
		this.player_arm = player_arm;
	}
	public Integer getDrop() {
		return drop;
	}

	public void setDrop(Integer drop) {
		this.drop = drop;
	}
	public String getPick_drop() {
		return pick_drop;
	}

	public void setPick_drop(String pick_drop) {
		this.pick_drop = pick_drop;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getRedpoint() {
		return redpoint;
	}

	public void setRedpoint(Integer redpoint) {
		this.redpoint = redpoint;
	}
	public String getFrom_shop() {
		return from_shop;
	}

	public void setFrom_shop(String from_shop) {
		this.from_shop = from_shop;
	}
	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}
	public Integer getTab_type() {
		return tab_type;
	}

	public void setTab_type(Integer tab_type) {
		this.tab_type = tab_type;
	}

	public String getDate_effective() {
		return date_effective;
	}

	public void setDate_effective(String date_effective) {
		this.date_effective = date_effective;
	}
}
