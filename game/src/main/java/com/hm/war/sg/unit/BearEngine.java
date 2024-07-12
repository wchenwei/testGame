package com.hm.war.sg.unit;

import com.google.common.collect.Lists;
import com.hm.war.sg.Frame;
import com.hm.war.sg.bear.Bear;

import java.util.List;

/**
 * 
 * @Description: 受伤引擎
 * @author siyunlong  
 * @date 2018年10月10日 下午4:02:54 
 * @version V1.0
 */
public class BearEngine {
	private transient Unit unit;
	//将要受到的伤害
	protected transient List<Bear> bearList = Lists.newArrayList();
	
	public BearEngine(Unit unit) {
		this.unit = unit;
	}
	
	public void addBear(Frame frame,Bear bear) {
		if(checkBear(frame, bear)) {
			return;
		}
		this.bearList.add(bear);
	}
	
	/**
	 * 计算本帧收到的承受信息
	 * @param frame
	 */
	public void calUnitHurt(Frame frame) {
		long curFrame = frame.getId();
		//==========计算收到的伤害===============
		for (int i = this.bearList.size()-1; i >= 0; i--) {
			Bear bear = this.bearList.get(i);
			if(bear.checkFrame(curFrame)) {
				if(bear.doUnit(frame,unit)) {//生效后加入Event
					frame.addEvent(bear.createEvent(unit));
				}
				if(bear.isCanDelBear()) {
					this.bearList.remove(i);
				}
				if(unit.isDeath()) {
					return;//死亡了
				}
			}
		}
	}
	
	public boolean checkBear(Frame frame,Bear bear) {
		if(bear.checkFrame(frame.getId())) {
			if(bear.doUnit(frame,unit)) {//生效后加入Event
				frame.addEvent(bear.createEvent(unit));
			}
			return true;
		}
		return false;
	}
}
