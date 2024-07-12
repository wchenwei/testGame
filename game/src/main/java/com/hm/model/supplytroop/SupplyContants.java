package com.hm.model.supplytroop;

import com.hm.util.MathUtils;
import com.hm.util.RandomUtils;

public class SupplyContants {
	public static final int SupplyEnemyNum = 5;
	
	public static double randomRate() {
		return MathUtils.round(RandomUtils.randomDouble(0.33, 0.5), 2);
	}
	
}
