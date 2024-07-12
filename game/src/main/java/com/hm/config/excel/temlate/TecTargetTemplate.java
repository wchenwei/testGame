package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("tec_target")
public class TecTargetTemplate {
	private Integer level;
	private Integer tec_id;

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getTec_id() {
		return tec_id;
	}

	public void setTec_id(Integer tec_id) {
		this.tec_id = tec_id;
	}
}
