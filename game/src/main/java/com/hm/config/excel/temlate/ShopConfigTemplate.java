package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("shop_config")
public class ShopConfigTemplate {
	private Integer id;
	private String dec;
	private Integer label;
	private Integer type;
	private Integer item;
	private Integer num_type;
	private String num;
	private Integer price_now;
	private Integer price_base;
	private String discount_times;
	private Integer buff_type;
	private Integer item_free_type;
	private Integer item_free;
	private Integer lv_down;
	private Integer lv_up;
	private Integer lv_vip_down;
	private Integer lv_vip_up;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}
	public Integer getLabel() {
		return label;
	}

	public void setLabel(Integer label) {
		this.label = label;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
	}
	public Integer getNum_type() {
		return num_type;
	}

	public void setNum_type(Integer num_type) {
		this.num_type = num_type;
	}
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	public Integer getPrice_now() {
		return price_now;
	}

	public void setPrice_now(Integer price_now) {
		this.price_now = price_now;
	}
	public Integer getPrice_base() {
		return price_base;
	}

	public void setPrice_base(Integer price_base) {
		this.price_base = price_base;
	}
	public String getDiscount_times() {
		return discount_times;
	}

	public void setDiscount_times(String discount_times) {
		this.discount_times = discount_times;
	}
	public Integer getBuff_type() {
		return buff_type;
	}

	public void setBuff_type(Integer buff_type) {
		this.buff_type = buff_type;
	}
	public Integer getItem_free_type() {
		return item_free_type;
	}

	public void setItem_free_type(Integer item_free_type) {
		this.item_free_type = item_free_type;
	}
	public Integer getItem_free() {
		return item_free;
	}

	public void setItem_free(Integer item_free) {
		this.item_free = item_free;
	}
	public Integer getLv_down() {
		return lv_down;
	}

	public void setLv_down(Integer lv_down) {
		this.lv_down = lv_down;
	}
	public Integer getLv_up() {
		return lv_up;
	}

	public void setLv_up(Integer lv_up) {
		this.lv_up = lv_up;
	}
	public Integer getLv_vip_down() {
		return lv_vip_down;
	}

	public void setLv_vip_down(Integer lv_vip_down) {
		this.lv_vip_down = lv_vip_down;
	}
	public Integer getLv_vip_up() {
		return lv_vip_up;
	}

	public void setLv_vip_up(Integer lv_vip_up) {
		this.lv_vip_up = lv_vip_up;
	}
}
