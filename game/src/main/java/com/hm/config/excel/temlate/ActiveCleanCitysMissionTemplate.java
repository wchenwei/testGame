package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_clean_citys_mission")
public class ActiveCleanCitysMissionTemplate {
	private Integer city_type;
	private Integer enemy_config;
	private Integer army_num;
	private Integer refresh_limit;

	public Integer getCity_type() {
		return city_type;
	}

	public void setCity_type(Integer city_type) {
		this.city_type = city_type;
	}
	public Integer getEnemy_config() {
		return enemy_config;
	}

	public void setEnemy_config(Integer enemy_config) {
		this.enemy_config = enemy_config;
	}
	public Integer getArmy_num() {
		return army_num;
	}

	public void setArmy_num(Integer army_num) {
		this.army_num = army_num;
	}
	public Integer getRefresh_limit() {
		return refresh_limit;
	}

	public void setRefresh_limit(Integer refresh_limit) {
		this.refresh_limit = refresh_limit;
	}
}
