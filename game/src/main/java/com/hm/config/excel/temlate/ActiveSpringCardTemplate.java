package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("active_spring_card")
public class ActiveSpringCardTemplate {
	private Integer id;
	private String compose;
	private Integer points;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public String getCompose() {
		return compose;
	}

	public void setCompose(String compose) {
		this.compose = compose;
	}
	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
}
