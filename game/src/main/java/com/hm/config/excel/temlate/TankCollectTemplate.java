package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tank_collect")
public class TankCollectTemplate {
	private Integer index;
	private Integer tank_id;
	private Integer get_map;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Integer getTank_id() {
		return tank_id;
	}

	public void setTank_id(Integer tank_id) {
		this.tank_id = tank_id;
	}
	public Integer getGet_map() {
		return get_map;
	}

	public void setGet_map(Integer get_map) {
		this.get_map = get_map;
	}
}
