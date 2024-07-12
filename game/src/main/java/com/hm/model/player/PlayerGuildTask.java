package com.hm.model.player;

import com.hm.libcore.msg.JsonMsg;

/**
 * @Description: 玩家部落任务
 * @author siyunlong  
 * @date 2019年9月25日 上午2:34:54 
 * @version V1.0
 */
public class PlayerGuildTask extends PlayerDataContext {
	private int taskProgress;//当前领取到哪一级别了
	private String mark;
    
    public int getTaskProgress() {
		return taskProgress;
	}

	public void setTaskProgress(int taskProgress) {
		this.taskProgress = taskProgress;
		SetChanged();
	}
	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
		SetChanged();
	}
	
	@Override
    public void fillMsg(JsonMsg msg) {
        msg.addProperty("PlayerGuildTask", this);
    }
}
