package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("odds")
public class OddsTemplate {
	private Integer index;
	private Float power_proportion;
	private Float strong_odds;
	private Float weak_odds;

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	public Float getPower_proportion() {
		return power_proportion;
	}

	public void setPower_proportion(Float power_proportion) {
		this.power_proportion = power_proportion;
	}
	public Float getStrong_odds() {
		return strong_odds;
	}

	public void setStrong_odds(Float strong_odds) {
		this.strong_odds = strong_odds;
	}
	public Float getWeak_odds() {
		return weak_odds;
	}

	public void setWeak_odds(Float weak_odds) {
		this.weak_odds = weak_odds;
	}
}
