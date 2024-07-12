package com.hm.war.sg.event;

import com.hm.libcore.util.gson.GSONUtils;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description:清除事件
 * @author siyunlong  
 * @date 2018年11月3日 下午3:07:32 
 * @version V1.0
 */
@NoArgsConstructor
public class CleanEvent extends Event{
	protected List<Integer> removeFuncIds;//清除的funcId
	private int funcId;//哪个func造成的清除
	
	public CleanEvent(int id,int funcId,List<Integer> removeFuncIds) {
		super(id, EventType.CleanEvent);
		this.funcId = funcId;
		this.removeFuncIds = removeFuncIds;
	}

	@Override
	public String toString() {
		return "清除buff:" + GSONUtils.ToJSONString(removeFuncIds) + "  funcId:" + funcId;
	}
}
