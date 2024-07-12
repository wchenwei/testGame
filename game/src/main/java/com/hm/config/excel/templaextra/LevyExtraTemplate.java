package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.LevyTemplate;
import com.hm.model.weight.RandomRatio;
@FileConfig("levy")
public class LevyExtraTemplate extends LevyTemplate {
	private RandomRatio oilRatio;
	private RandomRatio cashRatio;
	public void init(){
		this.oilRatio = new RandomRatio(this.getOil_crit_rate());
		this.cashRatio = new RandomRatio(this.getCash_crit_rate());
	}
	public RandomRatio getOilRatio() {
		return oilRatio;
	}
	public void setOilRatio(RandomRatio oilRatio) {
		this.oilRatio = oilRatio;
	}
	public RandomRatio getCashRatio() {
		return cashRatio;
	}
	public void setCashRatio(RandomRatio cashRatio) {
		this.cashRatio = cashRatio;
	}
	

}
