package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_0909_card")
public class Active0909CardTemplate {
	private Integer id;
	private Integer stage;
	private Integer id_sub;
	private String compose;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}
	public Integer getId_sub() {
		return id_sub;
	}

	public void setId_sub(Integer id_sub) {
		this.id_sub = id_sub;
	}
	public String getCompose() {
		return compose;
	}

	public void setCompose(String compose) {
		this.compose = compose;
	}
}
