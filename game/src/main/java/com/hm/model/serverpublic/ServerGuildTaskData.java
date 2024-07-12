package com.hm.model.serverpublic;

import com.hm.action.guild.task.GuildTaskType;
import lombok.Data;

/**
 * 
 * @Description: 服务器部落任务类型
 * @author siyunlong  
 * @date 2019年10月10日 下午2:29:21 
 * @version V1.0
 */
@Data
public class ServerGuildTaskData extends ServerPublicContext{
	private transient int week;
	private transient GuildTaskType taskType = GuildTaskType.Task1;//类型
	private int guildTaskType = GuildTaskType.Task1.getId();
	
	public GuildTaskType getTaskType() {
		return taskType;
	}

	
	public void resetData() {
		this.week ++;
		this.taskType = GuildTaskType.num2enum(week%GuildTaskType.getMaxType()+1);
		this.guildTaskType = this.taskType.getId();
		save();
	}
}
