package com.hm.war.sg.buff;

import com.hm.war.sg.Frame;

/**
 * @Description: 有此buff后玩家不能添加护盾buff
 * @author siyunlong  
 * @date 2020年12月16日 下午4:25:19 
 * @version V1.0
 */
public class NotShieldBuff extends BaseBuffer{
	public NotShieldBuff(long endFrame) {
		super(UnitBufferType.NoShieldBuff,endFrame,BuffKind.DeBuff);
	}
	
	@Override
	public boolean isCanReplace(Frame frame,BaseBuffer newBuff) {
		return newBuff.isForoverBuff() || newBuff.getEndFrame() > getEndFrame();
	}
}
