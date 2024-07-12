package com.hm.model.guild.tactics;

import com.hm.config.GameConstants;
import com.hm.enums.GuildTacticsType;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;
import com.hm.model.player.Player;
import lombok.NoArgsConstructor;

/**
 * @Description: 部落战术
 * @author siyunlong  
 * @date 2019年1月17日 上午9:45:15 
 * @version V1.0
 */
@NoArgsConstructor
public abstract class AbstractGuildTactics {
	private GuildTacticsType type;
	private long startTime;
	private long endTime;
	
	public AbstractGuildTactics(GuildTacticsType guildTacticsType,int second) {
		this.type = guildTacticsType;
		this.startTime = System.currentTimeMillis();
		this.endTime = System.currentTimeMillis() + second*GameConstants.SECOND;
	}
	/**
	 * 使用战术
	 * @param args
	 */
	public abstract boolean useTactics(Player player,Guild guild,Object... args);
	/**
	 * 加载等级参数
	 * @param value
	 */
	public abstract void loadLvValue(String value);
	/**
	 * 创建vo
	 * @return
	 */
	public abstract BaseGuildTacticsVo createGuildTacticsVo();
	
	public boolean isRun() {
		return this.endTime > System.currentTimeMillis();
	}
	
	public long getEndTime() {
		return endTime;
	}

	public GuildTacticsType getType() {
		return type;
	}

    public long getStartTime() {
        return startTime;
    }
}
