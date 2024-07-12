package com.hm.model.activity;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.mongodb.ServerInfo;
import lombok.Data;

import java.util.List;

@Data
public class ActivityConfItem {
	private String id;//活动id，表的主键
	private int activityType;//活动类型
	private long startTime;//开始时间
	private long calTime;//结算时间
	private long endTime;//结束时间
	private long viewTime;//展示时间
	private String extend;//扩展字段
	private int status;//状态 0-未审核 1-审核通过
	private int openType;//0-新服前7日不开启  1-全部开启
	
	private List<Integer> serverTypes = Lists.newArrayList();
	
	public boolean isFitServerInfo(ServerInfo serverInfo) {
		if(serverInfo != null) {
			return CollUtil.isEmpty(serverTypes) || serverTypes.contains(-1) || serverTypes.contains(serverInfo.getType());
		}
		return false;
	}
}
