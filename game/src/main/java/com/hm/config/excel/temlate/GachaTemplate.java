package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("gacha")
public class GachaTemplate {
	private Integer gacha_id;
	private String box_show;
	private Integer one_need_diamond;
	private Integer ten_need_diamond;
	private Integer first_special_num;
	private Integer normal_special_num;
	private String normal_box;
	private Integer chip;

	public Integer getGacha_id() {
		return gacha_id;
	}

	public void setGacha_id(Integer gacha_id) {
		this.gacha_id = gacha_id;
	}
	public String getBox_show() {
		return box_show;
	}

	public void setBox_show(String box_show) {
		this.box_show = box_show;
	}
	public Integer getOne_need_diamond() {
		return one_need_diamond;
	}

	public void setOne_need_diamond(Integer one_need_diamond) {
		this.one_need_diamond = one_need_diamond;
	}
	public Integer getTen_need_diamond() {
		return ten_need_diamond;
	}

	public void setTen_need_diamond(Integer ten_need_diamond) {
		this.ten_need_diamond = ten_need_diamond;
	}
	public Integer getFirst_special_num() {
		return first_special_num;
	}

	public void setFirst_special_num(Integer first_special_num) {
		this.first_special_num = first_special_num;
	}
	public Integer getNormal_special_num() {
		return normal_special_num;
	}

	public void setNormal_special_num(Integer normal_special_num) {
		this.normal_special_num = normal_special_num;
	}
	public String getNormal_box() {
		return normal_box;
	}

	public void setNormal_box(String normal_box) {
		this.normal_box = normal_box;
	}
	public Integer getChip() {
		return chip;
	}

	public void setChip(Integer chip) {
		this.chip = chip;
	}
}
