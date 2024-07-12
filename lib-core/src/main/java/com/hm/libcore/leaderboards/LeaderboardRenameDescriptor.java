package com.hm.libcore.leaderboards;

/**   
 * @Description: 重命名
 * @author siyunlong  
 * @date 2016年8月17日 下午2:25:51 
 * @version V1.0   
 */
public class LeaderboardRenameDescriptor {
	protected String Name;
	protected String Game;
	protected String addSuffix;
	
	public LeaderboardRenameDescriptor(String game, String name, String addSuffix) {
		this.Game = game;
		this.Name = name;
		this.addSuffix = addSuffix;
	}
	
	
}
