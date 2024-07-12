package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_discount_shop")
public class ActiveDiscountShopTemplate {
	private Integer id;
	private Integer player_lv_down;
	private Integer player_lv_up;
	private String goods;
	private String price;
	private Integer num;
	private String refresh_price_base;
	private String refresh_price_add;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	public String getRefresh_price_base() {
		return refresh_price_base;
	}

	public void setRefresh_price_base(String refresh_price_base) {
		this.refresh_price_base = refresh_price_base;
	}
	public String getRefresh_price_add() {
		return refresh_price_add;
	}

	public void setRefresh_price_add(String refresh_price_add) {
		this.refresh_price_add = refresh_price_add;
	}
}
