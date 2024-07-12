
package com.hm.action.guild.order;

import com.hm.action.guild.vo.GuildVo;

import java.util.Comparator;

/**
 * Title: GuildOrder.java
 * Description:
 * Copyright: Copyright (c) 2014
 * Company: Hammer Studio
 * @author 李飞
 * @date 2015年6月17日 下午2:13:56
 * @version 1.0
 */
public class GuildOrder implements Comparator<GuildVo>{

	@Override
	public int compare(GuildVo o1, GuildVo o2) {
		long result = o2.getCombat()-o1.getCombat();
		if(result!=0) {
			return result>0?1:-1;
		}
		result = o2.getLv()-o1.getLv();
		if(result!=0) {
			return result>0?1:-1;
		}
		result = o2.getCount()-o1.getCount();
		if(result!=0) {
			return result>0?1:-1;
		}
		long createTime2 = o2.getCreateTime()==null?0l:o2.getCreateTime().getTime();
		long createTime1 = o1.getCreateTime()==null?0l:o1.getCreateTime().getTime();
		result = createTime2 - createTime1;
		if(result!=0) {
			return result>0?1:-1;
		}
		return 1;
	}

}

