package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_shop")
public class ActiveShopTemplate {
	private Integer id;
	private Integer shop_id;
	private Integer active_id;
	private Integer round;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private Integer location;
	private String goods;
	private String price;
	private Integer limit;
	private String price_base;
	private Float discount;
	private String price_other;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getShop_id() {
		return shop_id;
	}

	public void setShop_id(Integer shop_id) {
		this.shop_id = shop_id;
	}
	public Integer getActive_id() {
		return active_id;
	}

	public void setActive_id(Integer active_id) {
		this.active_id = active_id;
	}
	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}
	public Integer getPlayer_lv_down() {
		return player_lv_down;
	}

	public void setPlayer_lv_down(Integer player_lv_down) {
		this.player_lv_down = player_lv_down;
	}
	public Integer getPlayer_lv_up() {
		return player_lv_up;
	}

	public void setPlayer_lv_up(Integer player_lv_up) {
		this.player_lv_up = player_lv_up;
	}
	public Integer getLocation() {
		return location;
	}

	public void setLocation(Integer location) {
		this.location = location;
	}
	public String getGoods() {
		return goods;
	}

	public void setGoods(String goods) {
		this.goods = goods;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String getPrice_base() {
		return price_base;
	}

	public void setPrice_base(String price_base) {
		this.price_base = price_base;
	}
	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public String getPrice_other() {
		return price_other;
	}

	public void setPrice_other(String price_other) {
		this.price_other = price_other;
	}
}
