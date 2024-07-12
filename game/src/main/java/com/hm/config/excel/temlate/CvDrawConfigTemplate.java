package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("cv_draw_config")
public class CvDrawConfigTemplate {
	private Integer id;
	private String name;
	private Integer type;
	private Integer free_cd;
	private String cost_once;
	private String cost_ten;
	private Integer extra_reward_once;
	private Integer extra_reward_ten;
	private String library_once;
	private String library_ten_normal;
	private String library_ten_spe;
	private String library_lucky;
	private Integer library_sscar;
	private String show;
	private Integer lucky_value;

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
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getFree_cd() {
		return free_cd;
	}

	public void setFree_cd(Integer free_cd) {
		this.free_cd = free_cd;
	}
	public String getCost_once() {
		return cost_once;
	}

	public void setCost_once(String cost_once) {
		this.cost_once = cost_once;
	}
	public String getCost_ten() {
		return cost_ten;
	}

	public void setCost_ten(String cost_ten) {
		this.cost_ten = cost_ten;
	}
	public Integer getExtra_reward_once() {
		return extra_reward_once;
	}

	public void setExtra_reward_once(Integer extra_reward_once) {
		this.extra_reward_once = extra_reward_once;
	}
	public Integer getExtra_reward_ten() {
		return extra_reward_ten;
	}

	public void setExtra_reward_ten(Integer extra_reward_ten) {
		this.extra_reward_ten = extra_reward_ten;
	}
	public String getLibrary_once() {
		return library_once;
	}

	public void setLibrary_once(String library_once) {
		this.library_once = library_once;
	}
	public String getLibrary_ten_normal() {
		return library_ten_normal;
	}

	public void setLibrary_ten_normal(String library_ten_normal) {
		this.library_ten_normal = library_ten_normal;
	}
	public String getLibrary_ten_spe() {
		return library_ten_spe;
	}

	public void setLibrary_ten_spe(String library_ten_spe) {
		this.library_ten_spe = library_ten_spe;
	}
	public String getLibrary_lucky() {
		return library_lucky;
	}

	public void setLibrary_lucky(String library_lucky) {
		this.library_lucky = library_lucky;
	}
	public Integer getLibrary_sscar() {
		return library_sscar;
	}

	public void setLibrary_sscar(Integer library_sscar) {
		this.library_sscar = library_sscar;
	}
	public String getShow() {
		return show;
	}

	public void setShow(String show) {
		this.show = show;
	}
	public Integer getLucky_value() {
		return lucky_value;
	}

	public void setLucky_value(Integer lucky_value) {
		this.lucky_value = lucky_value;
	}
}
