package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.OddsTemplate;

@FileConfig("odds")
public class BetRateTemplate extends OddsTemplate{
	private double[] betRate;
	
	public void init() {
		this.betRate = new double[]{getStrong_odds(),getWeak_odds()};
	}
	public double[] getBetRate() {
		return betRate;
	}
	
	
}
