package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("item_sweapon")
public class ItemSweaponTemplate {
	private Integer id;
	private String name;
	private String dec;
	private String icon;
	private Integer item_type;
	private Integer quality;
	private Integer rank;
	private Integer lv_unlock;
	private String part_product;
	private Integer time_product;
	private String weight_crit;
	private String weight_crit2;
	private Integer battle_id;
	private String piece_price;

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
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
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
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public Integer getLv_unlock() {
		return lv_unlock;
	}

	public void setLv_unlock(Integer lv_unlock) {
		this.lv_unlock = lv_unlock;
	}
	public String getPart_product() {
		return part_product;
	}

	public void setPart_product(String part_product) {
		this.part_product = part_product;
	}
	public Integer getTime_product() {
		return time_product;
	}

	public void setTime_product(Integer time_product) {
		this.time_product = time_product;
	}
	public String getWeight_crit() {
		return weight_crit;
	}

	public void setWeight_crit(String weight_crit) {
		this.weight_crit = weight_crit;
	}
	public String getWeight_crit2() {
		return weight_crit2;
	}

	public void setWeight_crit2(String weight_crit2) {
		this.weight_crit2 = weight_crit2;
	}
	public Integer getBattle_id() {
		return battle_id;
	}

	public void setBattle_id(Integer battle_id) {
		this.battle_id = battle_id;
	}
	public String getPiece_price() {
		return piece_price;
	}

	public void setPiece_price(String piece_price) {
		this.piece_price = piece_price;
	}
}
