package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("materials_config")
public class MaterialsConfigTemplate {
	private Integer quality;
	private Integer lv_building;
	private Integer cost_1;
	private Integer cost_2;
	private Integer weight1;
	private Integer weight2;
	private String product_list;
	private String num_random1;
	private String num_random2;
	private String num_random3;
	private String num_random4;
	private Integer product_work;

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}
	public Integer getLv_building() {
		return lv_building;
	}

	public void setLv_building(Integer lv_building) {
		this.lv_building = lv_building;
	}
	public Integer getCost_1() {
		return cost_1;
	}

	public void setCost_1(Integer cost_1) {
		this.cost_1 = cost_1;
	}
	public Integer getCost_2() {
		return cost_2;
	}

	public void setCost_2(Integer cost_2) {
		this.cost_2 = cost_2;
	}
	public Integer getWeight1() {
		return weight1;
	}

	public void setWeight1(Integer weight1) {
		this.weight1 = weight1;
	}
	public Integer getWeight2() {
		return weight2;
	}

	public void setWeight2(Integer weight2) {
		this.weight2 = weight2;
	}
	public String getProduct_list() {
		return product_list;
	}

	public void setProduct_list(String product_list) {
		this.product_list = product_list;
	}
	public String getNum_random1() {
		return num_random1;
	}

	public void setNum_random1(String num_random1) {
		this.num_random1 = num_random1;
	}
	public String getNum_random2() {
		return num_random2;
	}

	public void setNum_random2(String num_random2) {
		this.num_random2 = num_random2;
	}
	public String getNum_random3() {
		return num_random3;
	}

	public void setNum_random3(String num_random3) {
		this.num_random3 = num_random3;
	}
	public String getNum_random4() {
		return num_random4;
	}

	public void setNum_random4(String num_random4) {
		this.num_random4 = num_random4;
	}
	public Integer getProduct_work() {
		return product_work;
	}

	public void setProduct_work(Integer product_work) {
		this.product_work = product_work;
	}
}
