package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.MasteryAuraConnectTemplate;

import java.util.List;

@FileConfig("mastery_aura_connect")
public class MasteryConnectTemplate extends MasteryAuraConnectTemplate{
	private List<Integer> points = Lists.newArrayList();
	public void init(){
		this.points = StringUtil.splitStr2IntegerList(this.getConnect(), ",");
	}
	public List<Integer> getPoints() {
		return points;
	}
	
}
