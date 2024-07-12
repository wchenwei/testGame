package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.date.DateUtil;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.MissionTypeTemplate;
import com.hm.enums.TankLimitType;
import com.hm.model.player.BasePlayer;
import com.hm.model.tankLimitAdapter.TankLimitAdapterGroup;
import com.hm.model.tankLimitAdapter.TankLimitFactory;

import java.util.Date;
import java.util.List;

@FileConfig("mission_type")
public class MissionTypeTemplateImpl extends MissionTypeTemplate{
	private List<Integer> openWeek = Lists.newArrayList();
	private TankLimitAdapterGroup tankLimitGroup = new TankLimitAdapterGroup();
	public void init() {
		if(StrUtil.isNotEmpty(getOpen_day())) {
			this.openWeek = StringUtil.splitStr2IntegerList(getOpen_day(), ",");
			tankLimitGroup.add(TankLimitFactory.createProhibitAdapter(TankLimitType.TankCampProhibit, this.getCountry_limit()));
		}
	}
	
	public boolean isOpen(BasePlayer player) {
		if(openWeek.isEmpty()) {
			return true;
		}
		return openWeek.contains(DateUtil.getCsWeek());
	}
	
	public boolean isOpen(BasePlayer player,Date date) {
		if(player.playerLevel().getLv() < getUnlock_level()) {
			return false;
		}
		if(openWeek.isEmpty()) {
			return true;
		}
		return openWeek.contains(DateUtil.getWeek(date));
	}

	public TankLimitAdapterGroup getTankLimitGroup() {
		return tankLimitGroup;
	}
}
