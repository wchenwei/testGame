package com.hm.war.sg.buff;

import java.util.List;

/**
 * @Description: 致命连接buff
 * @author siyunlong  
 * @date 2018年11月9日 下午4:52:52 
 * @version V1.0
 */
public class HurtLinkBuffDeath extends HurtLinkBuff{
	public HurtLinkBuffDeath(long endFrame, double value, List<Integer> linkIds) {
		super(endFrame, value, linkIds);
	}
	
	@Override
	public boolean checkLinkDeath(int deathId) {
		return this.linkIds.contains(deathId);
	}
}