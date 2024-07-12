package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.FileConfig;

@FileConfig("capital_fight_drawing_shop")
public class CapitalFightDrawingShopTemplate {
	private Integer parts_drawing;
	private Integer diamond;

	public Integer getParts_drawing() {
		return parts_drawing;
	}

	public void setParts_drawing(Integer parts_drawing) {
		this.parts_drawing = parts_drawing;
	}
	public Integer getDiamond() {
		return diamond;
	}

	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
	}
}
