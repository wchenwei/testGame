package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("cd")
public class CdTemplate {
	private Integer id;
	private Integer maxCount;
	private Integer cdSecond;
	private Integer dayReset;
	private Integer baseCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
	public Integer getCdSecond() {
		return cdSecond;
	}

	public void setCdSecond(Integer cdSecond) {
		this.cdSecond = cdSecond;
	}
	public Integer getDayReset() {
		return dayReset;
	}

	public void setDayReset(Integer dayReset) {
		this.dayReset = dayReset;
	}
	public Integer getBaseCount() {
		return baseCount;
	}

	public void setBaseCount(Integer baseCount) {
		this.baseCount = baseCount;
	}
}
