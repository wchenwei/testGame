package com.hm.model.guild.command;

import com.hm.enums.GuildCommandType;

import java.util.List;

/**
 * @Description: 进攻
 * @author siyunlong  
 * @date 2019年3月15日 下午9:05:35 
 * @version V1.0
 */
public class GuildLineCommand extends AbstractGuildCommand{
	private List<Integer> wayList;
	
	public GuildLineCommand(List<Integer> wayList) {
		super(GuildCommandType.CommandLine);
		this.wayList = wayList;
	}
	
	
}
