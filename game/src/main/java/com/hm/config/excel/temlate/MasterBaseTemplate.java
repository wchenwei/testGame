package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("master_base")
public class MasterBaseTemplate {
	private Integer level_base;
	private Integer lv_master;
	private Integer lv_blacksmith;
	private Integer lv_researcher;

	public Integer getLevel_base() {
		return level_base;
	}

	public void setLevel_base(Integer level_base) {
		this.level_base = level_base;
	}
	public Integer getLv_master() {
		return lv_master;
	}

	public void setLv_master(Integer lv_master) {
		this.lv_master = lv_master;
	}
	public Integer getLv_blacksmith() {
		return lv_blacksmith;
	}

	public void setLv_blacksmith(Integer lv_blacksmith) {
		this.lv_blacksmith = lv_blacksmith;
	}
	public Integer getLv_researcher() {
		return lv_researcher;
	}

	public void setLv_researcher(Integer lv_researcher) {
		this.lv_researcher = lv_researcher;
	}
}
