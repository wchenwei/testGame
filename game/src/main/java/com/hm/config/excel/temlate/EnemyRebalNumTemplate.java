package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("enemy_rebal_num")
public class EnemyRebalNumTemplate {
	private Integer num_total;
	private String am_num;
	private String pm_num;

	public Integer getNum_total() {
		return num_total;
	}

	public void setNum_total(Integer num_total) {
		this.num_total = num_total;
	}
	public String getAm_num() {
		return am_num;
	}

	public void setAm_num(String am_num) {
		this.am_num = am_num;
	}
	public String getPm_num() {
		return pm_num;
	}

	public void setPm_num(String pm_num) {
		this.pm_num = pm_num;
	}
}
