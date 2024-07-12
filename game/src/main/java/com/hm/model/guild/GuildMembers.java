package com.hm.model.guild;

import com.hm.config.GameConstants;
import com.hm.enums.GuildJob;
import com.hm.libcore.msg.JsonMsg;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.guild.bean.GuildPlayerSimpleVo;

import java.util.List;
import java.util.stream.Collectors;

public class GuildMembers extends GuildPlayerBase {

	public void changeJob(long playerId, GuildJob job) {
		GuildPlayer tempP = guildPlayer(playerId);
		if(null!=tempP) {
			tempP.setGuildJob(job.getType());
			SetChanged();
		}
	}

	public GuildJob getJob(long playerId) {
		GuildPlayer guildP = guildPlayer(playerId);
		return null==guildP?GuildJob.NONE:GuildJob.getType(guildP.getGuildJob());
	}
	
	/**
	 * 获取部落张
	 * @return
	 */
	public GuildPlayer getPresidentGuildPlayer() {
		return this.playerMap.values().stream().filter(a->a.getGuildJob()==GuildJob.PRESIDENT.getType())
				.findFirst().orElse(null);
	}

	public long getJobSize(GuildJob job) {
		return this.playerMap.values().stream().filter(a->a.getGuildJob()==job.getType()).count();
	}

	public boolean isGuildMember(long playerId) {
		return this.playerMap.containsKey(playerId);
	}
	
	/**
	 * 获取成员的平均等级
	 * @return
	 */
	public double getAverageLv() {
		return getRedisPlayerList().stream().mapToInt(e -> e.getLv()).average().orElse(0);
	}
	//部落清除部落长时，获取下一个部落长
	public long randomNextPresident() {
		List<GuildPlayer> guildMembers = getGuildMembers();
		// 副首领
		long playerId = guildMembers.stream()
				.filter(e -> e.getGuildJob() == GuildJob.LEGATUS.getType())
				.map(GuildPlayer::getPlayerId)
				.parallel().findAny().orElse(0L);
		if (playerId > 0){
			return playerId;
		}
		// 没有副首领 给战力最高的活跃玩家 或者玩家
		List<GuildPlayerSimpleVo> playerSimpleVoList = guildMembers.stream()
				.filter(e -> e.getGuildJob() != GuildJob.PRESIDENT.getType())
				.map(e -> new GuildPlayerSimpleVo(e)).collect(Collectors.toList());
		// 活跃天数
		long lastActTime = System.currentTimeMillis() - 3 * GameConstants.DAY;
		return playerSimpleVoList.stream().filter(e -> e.getLastLoginTime() > lastActTime)
				.sorted(GuildPlayerSimpleVo::compareTo)
				.map(GuildPlayerSimpleVo::getPlayerId)
				.findFirst().orElse(
						playerSimpleVoList.stream()
								.sorted(GuildPlayerSimpleVo::compareTo)
								.map(GuildPlayerSimpleVo::getPlayerId)
								.findFirst().orElse(0L)
				);
	}
	
	public void clearDay(){
		this.getGuildMembers().forEach(member->{
			member.resetDay();
		});
		SetChanged();
	}

	public long getTotalCombat() {
		return this.getGuildMembers().stream().mapToLong(e -> e.getCombat()).sum();
	}

	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("memberNum", getNum());
	}
}









