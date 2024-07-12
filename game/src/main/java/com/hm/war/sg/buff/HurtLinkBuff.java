package com.hm.war.sg.buff;

import com.google.common.collect.Lists;
import com.hm.util.MathUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.unit.Unit;

import java.util.List;

/**
 * @Description: 致命连接buff
 * @author siyunlong  
 * @date 2018年11月9日 下午4:52:52 
 * @version V1.0
 */
public class HurtLinkBuff extends BaseBuffer{
	protected List<Integer> linkIds = Lists.newArrayList();
	
	public HurtLinkBuff(long endFrame,double value,List<Integer> linkIds) {
		super(UnitBufferType.HurtLink,endFrame,BuffKind.None);
		setValue(value);
		this.linkIds = Lists.newArrayList(linkIds);
	}

	
	@Override
	public boolean isCanReplace(Frame frame,BaseBuffer buff) {
		return this.getFuncId() == buff.getFuncId();
	}
	
	/**
	 * 处理致命连接
	 * @param frame
	 * @param unit
	 * @param hurt
	 */
	public void doLinkHurt(Frame frame,Unit unit,long hurt) {
		long trueHurt = MathUtils.mul(hurt, getValue());
		if(trueHurt <= 0) {
			return;
		}
		long endFrame = frame.getId();
		unit.getDefGroup().getLifeUnit().stream().filter(e -> linkIds.contains(e.getId()))
		.forEach(def -> {
			HurtBear hurtBear = new HurtBear(unit.getId(), endFrame, trueHurt, 0, getFuncId());
			hurtBear.setLinkHurt(false);
			def.addBear(frame, hurtBear);
		});
	}
	
	public List<Integer> getLinkIds() {
		return linkIds;
	}
	public void changeLinkIds(Integer deathId,Integer newId) {
		this.linkIds.remove(deathId);
		this.linkIds.add(newId);
	}
	
	//普通连接buff重新寻找目标
	public boolean checkLinkDeath(int deathId) {
		return false;
	}
}