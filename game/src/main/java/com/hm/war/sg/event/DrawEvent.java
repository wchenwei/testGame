package com.hm.war.sg.event;

import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 吸取事件
 * @author siyunlong  
 * @date 2018年9月10日 下午3:55:39 
 * @version V1.0
 */
@NoArgsConstructor
public class DrawEvent extends Event{
	private int drawType;
	private List<Integer> defList;
	private long frame;//持续帧数
	private int funcId;
	
	public DrawEvent(int id, List<Integer> defList,int type,long frame,int funcId) {
		super(id, EventType.DrawEevet);
		this.frame = frame;
		this.funcId = funcId;
		this.drawType = type;
		this.defList = defList;
	}
	
}
