package com.hm.model.cityworld;

import cn.hutool.core.util.StrUtil;
import com.hm.war.sg.WarResult;

public class Pvp1v1 {
	private String atk;
	private String def;
	private WarResult warResult;
	
	public Pvp1v1(WarResult warResult) {
		this.warResult = warResult;
		this.atk = this.warResult.getAtk().getFightTroop().getId();
		this.def = this.warResult.getDef().getFightTroop().getId();
	}
	
	public boolean isContain(String id) {
		return StrUtil.equals(atk, id) || StrUtil.equals(def, id);
	}

	public WarResult getWarResult() {
		return warResult;
	}
	
	
}
