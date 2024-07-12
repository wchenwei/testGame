package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.templaextra.BetRateTemplate;
import com.hm.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

@Config
public class BetConfig extends ExcleConfig{
	private List<BetRateTemplate> oddsList;
	
	@Override
	public void loadConfig() {
		loadOddTemplate();
	}
	
	private void loadOddTemplate() {
		List<BetRateTemplate> tempList = JSONUtil.fromJson(getJson(BetRateTemplate.class), new TypeReference<ArrayList<BetRateTemplate>>(){});
		tempList.forEach(e -> e.init());
		this.oddsList = ImmutableList.copyOf(tempList);
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(BetRateTemplate.class);
	}
	
	public double[] calBetRate(long max,long min) {
		double rate = min <= 0 ? 1:MathUtils.div(max, min);
		for (int i = oddsList.size()-1; i >= 0; i--) {
			if(rate >= oddsList.get(i).getPower_proportion()) {
				return oddsList.get(i).getBetRate();
			}
		}
		return oddsList.get(0).getBetRate();
	}
}
