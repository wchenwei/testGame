package com.hm.model.serverpublic;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.util.TimeUtils;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import lombok.Data;

import java.util.Date;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 玩家阵营荣誉数据
 * @date 2023/5/5 14:34
 */
@Data
public class ServerCampData extends ServerPublicContext{
	public static final int CampHonorDay = 3;

	private String mark;//标识
	private long honorCalTime;//阵营排行荣誉结算

	public void loadFirst() {
		if(this.honorCalTime <= System.currentTimeMillis()) {
			loadNewMarkAndSave();
		}
	}

	public boolean isCalHonorRank() {
		return StrUtil.equals(TimeUtils.formatSimpeTime2(new Date()),this.mark);
	}

	public void loadNewMark() {
		this.honorCalTime = DateUtil.beginOfDay(new Date()).getTime() + CampHonorDay*GameConstants.DAY;
		this.mark = TimeUtils.formatSimpeTime2(new Date(this.honorCalTime));
	}
	public void loadNewMarkAndSave() {
		loadNewMark();
		save();
	}

	public static void main(String[] args) {
		long hotime = DateUtil.beginOfDay(new Date()).getTime() + CampHonorDay*GameConstants.DAY;
		System.out.println(TimeUtils.formatSimpeTime2(new Date(hotime)));
	}
}
