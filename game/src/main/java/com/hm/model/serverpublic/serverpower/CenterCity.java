package com.hm.model.serverpublic.serverpower;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CenterCity {
	private List<Integer> centerCityIds = Lists.newArrayList();
	private long endTime;//结束时间
	
	public boolean isInTime() {
		return endTime>System.currentTimeMillis();
	}

	public void clearData() {
		this.centerCityIds.clear();
		this.endTime = 0;
	}

}
