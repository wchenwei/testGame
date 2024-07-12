package com.hm.config.excel.templaextra;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.ActiveSpring7RoadTemplate;
import com.hm.util.StringUtil;

import java.util.List;

@FileConfig("active_spring7_road")
public class ActiveSpring7RoadImpl extends ActiveSpring7RoadTemplate{
	private List<Integer> taskIds = Lists.newArrayList();
	
	public void init(){
		this.taskIds = StringUtil.splitStr2IntegerList(this.getTask(), ",");
	}
	
	public boolean isContains(int id) {
		if(CollectionUtil.isEmpty(this.taskIds) || !this.taskIds.contains(id)) {
			return false;
		}
		return true;
	}
	//是否可以跳过
	public boolean isCanSkip() {
		return this.getCan_pass()==1;
	}
}
