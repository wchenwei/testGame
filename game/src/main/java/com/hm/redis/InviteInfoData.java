package com.hm.redis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteInfoData {
	
	private long id;
	//被邀请玩家基本信息
	private List<SimpleInvitePlayer> beInvites = Lists.newArrayList();
	private long recharge;//被邀请玩家充值信息
	
	public InviteInfoData(Player player) {
		this.id = player.getId();
		this.beInvites.add(new SimpleInvitePlayer(player));
	}
	
	public void addRecharge(long add){
		this.recharge += add;
	}
	
	public void addBeInvite(Player player){
		this.beInvites.add(new SimpleInvitePlayer(player));
	}

	public SimpleInvitePlayer getBeInviteInfo(long id) {
		Optional<SimpleInvitePlayer> optional = beInvites.stream().filter(t ->t.getPlayerId()==id).findFirst();
		if(optional.isPresent()){
			return optional.get();
		}
		return null;
	}

	public int getLvNum(Player player, int needLv, int taskType) {
		if(taskType==1){//成就任务
			return beInvites.stream().filter(t ->t.getLv()>=needLv).collect(Collectors.toList()).size();
		}
		Set<String> dateStr = getDateStr(player);
		return beInvites.stream().filter(t ->t.getLv()>=needLv&&CollUtil.contains(dateStr, t.getDate())).collect(Collectors.toList()).size();
	}

	public int getInviteNum(Player player, int taskType){
		if(taskType==1){//成就任务
			return beInvites.size();
		}
		Set<String> dateStr = getDateStr(player);

		return (int)beInvites.stream().filter(t-> CollUtil.contains(dateStr, t.getDate())).count();
		// return (int)beInvites.stream().filter(t-> StrUtil.equals(t.getDate(), DateUtil.today())).count();
	}

	/**
	 * [2023-10-01, 2023-10-02, 2023-10-03]
	 * @param player
	 * @return
	 */
	public static Set<String> getDateStr(Player player) {
		// 以建号开始算每三天一轮
		int n = (int) (DateUtil.betweenDay(player.playerBaseInfo().getCreateDate(), new Date(), true) % 3);

		Set<String> dateStr = Sets.newHashSet();
		for (int i = 0; i < 3; i++) {
			if (n == i) {
				dateStr.add(DateUtil.today());
			} else {
				DateTime dateTime = new DateTime().offsetNew(DateField.DAY_OF_MONTH, i - n);
				String s = DateUtil.formatDate(dateTime);
				dateStr.add(s);
			}
		}
		return dateStr;
	}

	//获取满足高品坦克数量的玩家数量
	public int getTankNum(int needNum,int taskType){
		if(taskType==1){//成就任务
			return beInvites.size();
		}
		return (int)beInvites.stream().filter(t-> StrUtil.equals(t.getDate(), DateUtil.today())&&t.getSeniorTankNum()>=needNum).count();
	}



}
