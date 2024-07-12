package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.MasteryAuraComboTemplate;

import java.util.List;

@FileConfig("mastery_aura_combo")
public class MasteryLineTemplate extends MasteryAuraComboTemplate{
	private List<Integer> lines = Lists.newArrayList();
	public void init(){
		this.lines = StringUtil.splitStr2IntegerList(this.getCombo(), ",");
	}
	public List<Integer> getLines() {
		return lines;
	}
	
	
}
