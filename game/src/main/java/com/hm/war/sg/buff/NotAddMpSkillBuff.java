package com.hm.war.sg.buff;

/**
 * @Description: 不增加mp buff
 * @author siyunlong  
 * @date 2019年8月13日 上午2:48:10 
 * @version V1.0
 */
public class NotAddMpSkillBuff extends BaseBuffer{
	
	public NotAddMpSkillBuff(long endFrame) {
		super(UnitBufferType.NotAddMpSkillBuff,endFrame,BuffKind.DeBuff);
	}
}
