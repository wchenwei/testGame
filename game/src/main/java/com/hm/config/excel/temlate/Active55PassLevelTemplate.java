package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_55_pass_level")
public class Active55PassLevelTemplate {
	private Integer id;
	private Integer zongzi;
	private Integer zongzi_total;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getZongzi() {
		return zongzi;
	}

	public void setZongzi(Integer zongzi) {
		this.zongzi = zongzi;
	}
	public Integer getZongzi_total() {
		return zongzi_total;
	}

	public void setZongzi_total(Integer zongzi_total) {
		this.zongzi_total = zongzi_total;
	}
}
