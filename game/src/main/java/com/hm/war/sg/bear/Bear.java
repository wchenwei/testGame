package com.hm.war.sg.bear;

import com.hm.war.sg.Frame;
import com.hm.war.sg.event.Event;
import com.hm.war.sg.unit.Unit;
import lombok.Data;

//玩家承受的信息
@Data
public abstract class Bear {
	private int atkId;//攻击者
	private long effectFrame;//生效帧
	
	public Bear(int atkId, long effectFrame) {
		super();
		this.atkId = atkId;
		this.effectFrame = effectFrame;
	}
	//生成事件信息
	public abstract Event createEvent(Unit unit);
	
	//生效后的处理
	public abstract boolean doUnit(Frame frame, Unit unit);
	
	//是否删除效果
	public boolean isCanDelBear() {
		return true;
	}
	
	//检查是否生效
	public boolean checkFrame(long curFrame) {
		return curFrame >= effectFrame;
	}
	
}
