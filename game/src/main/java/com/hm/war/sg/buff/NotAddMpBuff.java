package com.hm.war.sg.buff;

/**
 * @Description: 不增加mp buff
 * @author siyunlong  
 * @date 2019年8月13日 上午2:48:10 
 * @version V1.0
 */
public class NotAddMpBuff extends BaseBuffer{
	
	public NotAddMpBuff(long endFrame) {
		super(UnitBufferType.NotAddMpBuff,endFrame,BuffKind.DeBuff);
	}
}
