package com.hm.model.guild;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.date.DateUtil;
import com.hm.config.GameConstants;
import com.hm.enums.GuildImpeachStateEnum;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ClassName: GuildImpeach. <br/>  
 * Function: 部落弹劾. <br/>  
 * date: 2019年3月27日 上午11:34:41 <br/>  
 * @author zxj  
 * @version
 */
public class GuildImpeach extends GuildComponent {
	//key：playerId;value：状态(GuildImpeachStateEnum对应)
	private Map<Long, Integer> memberMap = Maps.newConcurrentMap();
	
	private int state;	//是否进行部落弹劾(0,未弹劾状态，1：正在弹劾状态)
	private long startDate;	//开始时间
	private long endDate;	//结束时间
	private int startMember;//开始投票时的人数。此处按开始投票的人数
	
	//部落弹劾的状态，是否结束
	public boolean isEnd() {
		if(state==1) {
			return false;
		}
		return true;
	}
	
	public void startImpeach(long playerId, int lastTime) {
		state = 1;
		startMember = Context().getGuildMembers().getNum();
		startDate = new Date().getTime();
		Date end = new Date(new Date().getTime()+lastTime*GameConstants.HOUR);
		endDate = DateUtil.getNextHour(end).getTime();
		memberMap.clear();
		memberMap.put(playerId, GuildImpeachStateEnum.ActMember.getType());
		Context().getGuildMembers().getGuildMembers().forEach(m->{
			if(m.getPlayerId()!=playerId) {
				memberMap.put(m.getPlayerId(), GuildImpeachStateEnum.Common.getType());
			}
		});
		SetChanged();
	}
	
	public long getStartDate() {
		return startDate;
	}
	public int getStartMember() {
		return startMember;
	}
	@Override
	public void fillMsg(JsonMsg msg) {
		msg.addProperty("guildImpeach", this);
	}
	public int getState() {
		return state;
	}
	public void addImpeach(long playerId, GuildImpeachStateEnum guildImpeachStateEnum) {
		memberMap.put(playerId, guildImpeachStateEnum.getType());
		SetChanged();
	}

	public boolean hasImpeach(long playerId) {
		if(!memberMap.containsKey(playerId) || memberMap.get(playerId)!=GuildImpeachStateEnum.Common.getType()) {
			return true;
		}
		return false;
	}
	
	public List<Integer> getReuslt() {
		return this.memberMap.values().stream().collect(Collectors.toList());
	}

	//获取弹劾的发起人
	public long getActMember() {
		for(long key :this.memberMap.keySet()) {
			if(memberMap.get(key)==GuildImpeachStateEnum.ActMember.getType()) {
				return key;
			}
		}
		return 0;
	}
	public long getEndDate() {
		return endDate;
	}

	public void end() {
		state=0;
		SetChanged();
	}
}










