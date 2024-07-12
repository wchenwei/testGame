package com.hm.war.sg.buff;

/**
 * @Description: 闪避buff
 * @author siyunlong  
 * @date 2019年8月13日 上午2:48:10 
 * @version V1.0
 */
public class DodgeBuff extends BaseBuffer{
	private int maxCount;//多次攻击闪避一次
	private int atkCount;//
	
	public DodgeBuff(long endFrame,double value) {
		super(UnitBufferType.DodgeBuff,endFrame,BuffKind.Buff);
		this.maxCount = (int)value;
	}
	
	public boolean checkDodge() {
		this.atkCount ++;
		if(this.atkCount > this.maxCount) {
			this.atkCount = 0;
			return true;
		}
		return false;
	}
}
