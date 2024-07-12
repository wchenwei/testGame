package com.hm.config.excel.templaextra;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.MissionEventLinkTemplate;

import java.util.List;

@FileConfig("mission_event_link")
public class LevelEventLinkTemplate extends MissionEventLinkTemplate{
	private List<Integer> points = Lists.newArrayList();
	
	public void init() {
		this.points = ImmutableList.copyOf(StringUtil.splitStr2IntegerList(this.getMission_id_link(), ":"));
	}

	public boolean isFit(int star,int end) {
		return this.points.contains(star) && this.points.contains(end) & star != end;
	}
}
