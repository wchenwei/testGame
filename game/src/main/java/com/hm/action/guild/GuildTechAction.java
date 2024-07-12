package com.hm.action.guild;

import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildConfig;
import com.hm.config.excel.temlate.GuildDonateTemplateImpl;
import com.hm.config.excel.temlate.GuildTecTemplateImpl;
import com.hm.config.excel.templaextra.GuildTacticsTemplateImpl;
import com.hm.enums.*;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.bean.GuildPlayer;
import com.hm.model.guild.tactics.AbstractGuildTactics;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.SysConstant;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Action
public class GuildTechAction extends AbstractGuildAction{
	@Resource
	private GuildConfig guildConfig;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private GuildTechBiz guildTechBiz;
	@Resource
	private GuildBiz guildBiz;
	/**
	 * donation:(部落捐献). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
	@MsgMethod(MessageComm.C2S_Guild_Donation)
	public void donation(Player player, Guild guild, JsonMsg msg){
		//判断是否超过最大捐献次数
		long times = player.getPlayerStatistics().getTodayStatistics(StatisticsType.GuildGold);
		if(times >= guildConfig.getMaxDonateTimes()) {
			player.sendErrorMsg(SysConstant.Guild_Overload);
			return;
		}
		//判断用户是否在部落
		GuildPlayer guildPlayer = guild.getGuildMembers().guildPlayer(player.getId());
		if(null==guildPlayer) {
			player.sendErrorMsg(SysConstant.Guild_Noin);
			return;
		}
		//传入参数错误
		GuildDonateTemplateImpl donate = guildConfig.getNextDonate(times);
		if(null==donate) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		//校验玩家资源是否充足
		if(!itemBiz.checkItemEnoughAndSpend(player, donate.getItemsCost(), LogType.GuildDonateCost)) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		//处理捐赠信息
		List<Items> rewards = guildTechBiz.donate(guild, player, donate);
		//保存部落信息
		guild.saveDB();

		player.notifyObservers(ObservableEnum.GuildDonation);
		
		player.sendUserUpdateMsg();
		JsonMsg returnMsg = new JsonMsg(MessageComm.S2C_Guild_Donation);
		returnMsg.addProperty("times", player.getPlayerStatistics().getTodayStatistics(StatisticsType.GuildGold));
		returnMsg.addProperty("rewards", rewards);
		player.sendMsg(returnMsg);
	}
	/**
	 * tecUpdate:(部落科技升级). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
	@MsgMethod(MessageComm.C2S_Guild_TecUpdate)
	public void tecUpdate(Player player, Guild guild, JsonMsg msg){
		int techId = msg.getInt("techId");
		GuildTecTemplateImpl tecTemplate = guildConfig.getGuilTec(techId);
		//校验传入techid
		if(null==tecTemplate) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		//只有部落长副部落长能升级
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		//部落科技等级
		int techLv = guild.guildTechnology().getTecLvById(techId);
		if(techLv>=tecTemplate.getMax_level()) {
			player.sendErrorMsg(SysConstant.Guild_Tec_LvMax);
			return ;
		}
		//科技升级条件
		if(!guildTechBiz.isCanUpdate(guild, techId)) {
			player.sendErrorMsg(SysConstant.Guild_Update_Error);
			return;
		}
		int tecPointCost = tecTemplate.getTecCostArr(techLv+1);
		boolean result = guildTechBiz.reduceTecPoints(guild, tecPointCost);
		if(!result) {
			player.sendErrorMsg(SysConstant.Guild_Point_Not_Enough);
			return ;
		}
		
		guild.guildTechnology().updateTecLv(techId, techLv+1);
		guild.saveDB();
		
		guild.broadMemberGuildUpdate();
		JsonMsg returnMsg = new JsonMsg(MessageComm.S2C_Guild_TecUpdate);
		returnMsg.addProperty("techMsg", guild.guildTechnology());
		player.sendMsg(returnMsg);
	}
	/**
	 * tecReset:(重置部落科技). <br/>  
	 * @author zxj  
	 * @param player
	 * @param msg  使用说明
	 */
	@MsgMethod(MessageComm.C2S_Guild_TecReset)
	public void tecReset(Player player, Guild guild, JsonMsg msg){
		//只有部落长副部落长能充值科技
		if(!guild.isManamger(player)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		//冷却时间，单位分钟
		int resetTime = commValueConfig.getCommValue(CommonValueType.GuildResetTime);
		//是否已经过了重置时间
		boolean isOutRestTime = guild.guildTechnology().isOutRestTime(resetTime);
		if(!isOutRestTime) {
			player.sendErrorMsg(SysConstant.Guild_Tec_ResetTime);
			return ;
		}
		//检查消耗资源
		boolean reduceItem = itemBiz.checkItemEnoughAndSpend(player, commValueConfig.getListItem(CommonValueType.GuildResetTec), LogType.GuildTecReset);
		if(!reduceItem) {
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return ;
		}
		//重置科技
		guildTechBiz.resetTech(guild);
		guild.saveDB();
		
		guild.broadMemberGuildUpdate();
		
		JsonMsg returnMsg = new JsonMsg(MessageComm.S2C_Guild_TecReset);
		returnMsg.addProperty("techMsg", guild.guildTechnology());
		player.sendMsg(returnMsg);
	}
	
	
	@MsgMethod(MessageComm.C2S_Guild_UseTactics)
	public void useTactics(Player player, Guild guild, JsonMsg msg){
		int techId = msg.getInt("techId");
		int cityId = msg.getInt("cityId");
		GuildTecTemplateImpl guildTec = guildConfig.getGuilTec(techId);
		if(guildTec == null) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		GuildTacticsType tacticsType = GuildTacticsType.getType(guildTec.getTec_value());
		if(tacticsType == null) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		int lv = guild.guildTechnology().getTecLvById(techId);
		if(lv <= 0) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}

		int cuTimes = guild.guildTechnology().getTechUseTimes(techId);
		int maxTimes = (int)guildTec.getTecValue(lv);
		if(cuTimes >= maxTimes) {
			player.sendErrorMsg(SysConstant.Guild_Tec_TimesOver);
			return;
		}
		WorldCity worldCity = WorldServerContainer.of(player).getWorldCity(cityId);
		if(worldCity == null) {
			player.sendErrorMsg(SysConstant.Guild_Tec_CityNot);
			return;
		}
		if(guild.getGuildTactics().haveGuildTactics(worldCity.getId())) {
			player.sendErrorMsg(SysConstant.Guild_Tec_CityNot);
			return;
		}
		GuildTacticsTemplateImpl guildTacticsTemplate = guildConfig.getGuildTacticsTemplate(tacticsType.getType());
		GuildJob jobEnum = guild.getGuildMembers().getJob(player.getId());
		if(!guildTacticsTemplate.isCanUse(jobEnum)) {
			player.sendErrorMsg(SysConstant.Guild_NoPower);
			return ;
		}
		if(!guildTacticsTemplate.isCanFitCity(player, guild, worldCity)) {
			player.sendErrorMsg(SysConstant.Guild_Tec_CityNot);
			return ;
		}
		AbstractGuildTactics guildTactics = tacticsType.buildGuildTactics(guildTacticsTemplate.getTime());
		//对城池使用
		guildTactics.useTactics(player, guild, worldCity);
		//增加使用次数
		guild.guildTechnology().addTechUserTimes(techId);
		guildTactics.loadLvValue(guildTacticsTemplate.getLvValue(lv));
		guild.saveDB();

		guild.broadMemberGuildUpdate();
		//更新战术数据
		guildTechBiz.boradGuildTacticsUpdate(guild);
		//使用战术
		player.notifyObservers(ObservableEnum.UseTactics, guildTactics);

		log.error(guild.getId()+"使用战术:"+guildTactics.getType().getDesc()+"="+cityId);

	}
	
}
















