package com.hm.action.guild.vo;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.GameConstants;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.model.player.Arms;
import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArmsVo{
	private long playerId;
	private String name;
	private String uid;
	private int id;
	private int lv;
	private long endTime;
    private int exp;
	
	public ArmsVo(Player player,Arms arms){
		this.playerId = player.getId();
		this.name = player.getName();
		this.uid = arms.getUid();
		this.id = arms.getId();
		this.lv = arms.getLv();
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		int minutes = commValueConfig.getCommValue(CommonValueType.GuildFactoryStrengthTime);
		this.endTime = System.currentTimeMillis()+minutes*GameConstants.MINUTE;
        this.exp = arms.getExp();
	}
	

}
