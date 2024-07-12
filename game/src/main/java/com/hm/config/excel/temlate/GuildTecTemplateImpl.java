package com.hm.config.excel.temlate;

import com.hm.libcore.annotation.ConfigInit;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.guild.Guild;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;

@FileConfig("guild_tec")
public class GuildTecTemplateImpl extends GuildTecTemplate{
	private double[] tecAddArr = null;
	private int[] tecCostArr = null;

	@ConfigInit
	public void init() {
		tecAddArr = StringUtil.strToDoubleArray(this.getValue(), ",");
		tecCostArr = StringUtil.strToIntArray(this.getTec_upgrade(), ",");
	}
	
	public double getTecValue(int lv) {
		return tecAddArr[lv-1];
	}

	public int getTecCostArr(int lv) {
		return tecCostArr[lv-1];
	}

	//获取额外功勋掉落
	public int getContrVal(Guild guild) {
		int lv = guild.guildTechnology().getTecLvById(getId());
		if(lv <= 0) {
			return 0;
		}
		if(RandomUtils.randomIsRate(getTecValue(lv))) {
			return getTec_value();
		}
		return 0;
	}
}








