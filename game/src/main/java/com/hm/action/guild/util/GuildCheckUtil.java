package com.hm.action.guild.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.db.PlayerUtils;
import com.hm.enums.CommonValueType;
import com.hm.enums.GuildJob;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.SensitiveWordUtil;
import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.guild.bean.GuildPlayerVo;
import com.hm.model.player.Player;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.sysConstant.SysConstant;
import com.hm.util.NameUtils;
import com.hm.util.StringUtil;

import java.util.Date;

public class GuildCheckUtil {
	
	public static void checkName(Player player, String name) throws GuildCheckException {
		if(StringUtil.isNullOrEmpty(name)) {
			throw new GuildCheckException(SysConstant.NAME_ILLEGAL);
		}
		if (!NameUtils.isFitName(name)) {
			throw new GuildCheckException(SysConstant.NAME_ILLEGAL);
		}
		if(name.contains("\r\n") || name.contains("\r") || name.contains("\n")) {
			throw new GuildCheckException(SysConstant.NAME_ILLEGAL);
		}
		//长度校验
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		if(name.length()>commValueConfig.getCommValue(CommonValueType.GuildNameLength)) {
			throw new GuildCheckException(SysConstant.Str_OverLength);
		}
	}
	//校验是否能加入到部落
	public static void checkAddGuild(Player player) throws GuildCheckException {
		//是否开启
		if(!checkIsOpen(player)) {
			throw new GuildCheckException(SysConstant.Guild_Close);
		}
		//是否已经加入部落中
		if(player.playerGuild().getGuildId()>0) {
			throw new GuildCheckException(SysConstant.Guild_Repeat);
		}
		//上次离开部落时间
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		if(!player.playerGuild().timeCheck(commValueConfig.getCommValue(CommonValueType.leaveTime))) {
			throw new GuildCheckException(SysConstant.Guild_CheckTime);
		}
	}
	//是否名字重复
	public static void checkNameRepeat(Player player, String name) throws GuildCheckException {
		//是否名字重复
		boolean isName = GuildContainer.of(player.getServerId()).containsGuildName(name);
		if(isName) {
			throw new GuildCheckException(SysConstant.Guild_Name_RE);
		}
	}
	//检验旗帜信息
	public static void checkFlagName(Player player, String flagName) throws GuildCheckException {
		if(StringUtil.isNullOrEmpty(flagName)) {
			throw new GuildCheckException(SysConstant.NAME_ILLEGAL);
		}
		//是否包含关键字
		if (SensitiveWordUtil.contains(flagName)) {
			throw new GuildCheckException(SysConstant.NAME_ILLEGAL);
		}
		//长度校验
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		if(flagName.length()>commValueConfig.getCommValue(CommonValueType.FlagName)) {
			throw new GuildCheckException(SysConstant.Str_OverLength);
		}
	}
	public static void checkFlagRepeat(Player player, String guildFlag) throws GuildCheckException {
		boolean isFlagName = GuildContainer.of(player.getServerId()).containsGuildFlag(guildFlag);
		if(isFlagName) {
			throw new GuildCheckException(SysConstant.Guild_FlagName_RE);
		}
	}
	
	public static void checkAddGuild(Player player, Guild guild) throws GuildCheckException {
		if(null==guild) {
			throw new GuildCheckException(SysConstant.Guild_NoExist);
		}
		GuildCheckUtil.checkAddGuild(player);
		
		if(player.playerGuild().reReq(guild.getId())) {
			throw new GuildCheckException(SysConstant.Guild_reReq);
		}
		//校验同时申请的数量
		CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
		if(player.playerGuild().getReqSize()>=commValueConfig.getCommValue(CommonValueType.reqSize)) {
			throw new GuildCheckException(SysConstant.Guild_ReqSize);
		}
	}
	//创建部落校验
	public static void checkCreate(Player player, String name, String trueName) throws GuildCheckException {
		GuildCheckUtil.checkAddGuild(player);
		GuildCheckUtil.checkName(player, name);
		GuildCheckUtil.checkNameRepeat(player, trueName);
	}
	//判断是否开启部落功能
	public static boolean checkIsOpen(Player player) {
		return true;//TODO
//		return player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Guild);
	}
	
	public static int checkCanImpleach(Player player, Guild guild, int time) {
		// 判断自己是否在部落长中
		if (null == guild) {
			return SysConstant.Guild_NoExist;
		}
		// 判断自己是不是部落长
		GuildJob guildJob = guild.getGuildMembers().getJob(player.getId());
		if (guildJob == GuildJob.PRESIDENT) {
			return SysConstant.Guild_ImpeachMyself;
		}
		GuildPlayer guildTarPlayer = guild.getGuildMembers().getPresidentGuildPlayer();
		if (null == guildTarPlayer) {
			return SysConstant.Guild_ImpeachError;
		}
		Player tarPlayer = PlayerUtils.getPlayer(guildTarPlayer.getPlayerId());
		if (null == tarPlayer) {
			return SysConstant.PLAYER_NOT_EXIST;
		}
		GuildPlayerVo guildTarPlayerVo = GuildPlayerVo.buildVo(guildTarPlayer);
		// 被弹劾团长在线，或者最后离线时间，小于24(配置时间)小时，或者已经再弹劾中了
		if (guildTarPlayerVo.getIsOnline() == 1 
				|| DateUtil.between(new Date(guildTarPlayerVo.getLastOffLineDate()*1000), new Date(), DateUnit.HOUR)<time
				|| !guild.getGuildImpeach().isEnd()) {
			return SysConstant.Guild_ImpeachCondition;
		}
		return -1;
	}
}











