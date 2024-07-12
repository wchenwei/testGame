package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_private_gift")
public class ActivePrivateGiftTemplate {
	private Integer id;
	private Integer lv_down;
	private Integer lv_up;
	private Integer recharge_gift;
	private Integer goods_base;
	private String library;
	private Integer num;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public Integer getRecharge_gift() {
		return recharge_gift;
	}

	public void setRecharge_gift(Integer recharge_gift) {
		this.recharge_gift = recharge_gift;
	}
	public Integer getGoods_base() {
		return goods_base;
	}

	public void setGoods_base(Integer goods_base) {
		this.goods_base = goods_base;
	}
	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}
	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}
