package com.hm.action.guild.command;

import cn.hutool.core.util.StrUtil;
import com.hm.chat.ChatRoomType;
import com.hm.chat.InnerChatFacade;
import com.hm.config.CityConfig;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.enums.GuildCommandType;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildTechBiz;
import com.hm.libcore.util.string.StringUtil;
import com.hm.message.MessageComm;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.command.AbstractGuildCommand;
import com.hm.model.guild.command.GuildAtkCommand;
import com.hm.model.guild.command.GuildDefCommand;
import com.hm.model.guild.command.GuildLineCommand;
import com.hm.model.player.Player;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.world.WorldServerContainer;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: 部落指挥
 * @author siyunlong  
 * @date 2019年3月15日 下午9:03:04 
 * @version V1.0
 */
@Biz
public class GuildCommandBiz implements IObserver{
	@Resource
	private GuildBiz guildBiz;

	@Resource
	private GuildTechBiz guildTechBiz;
	@Resource
	private CityConfig cityConfig;
	@Resource
	private LanguageCnTemplateConfig languageCnTemplateConfig;
	@Resource
	private InnerChatFacade innerChatFacade;

	@Override
	public void registObserverEnum() {
//		ObserverRouter.getInstance().registObserver(ObservableEnum.OccupyCity, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.GuildPlayerAdd, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch(observableEnum) {
//			case OccupyCity:
//				checkOccupyCity((WorldCity)argv[0]);
//				break;
			case GuildPlayerAdd:
				checkPlayerAddGuild(player);
				break;
		}
	}

	
	//检查玩家加入部落
	private void checkPlayerAddGuild(Player player) {
		sendGuildCommandUpdate(player);
		//发送部落战术信息
		guildTechBiz.sendPlayerGuildTactics(player);
	}
	
	public void broadGuildCommandUpdate(Guild guild) {
		JsonMsg msg = new JsonMsg(MessageComm.S2C_GuildCommand_Update);
		msg.addProperty("guildCommands", guild.getGuildCommands());
		guildBiz.broadGuildMember(guild, msg);
	}
	
	public void sendGuildCommandUpdate(Player player) {
		Guild guild = guildBiz.getGuild(player);
		if(guild != null) {
			JsonMsg msg = new JsonMsg(MessageComm.S2C_GuildCommand_Update);
			msg.addProperty("guildCommands", guild.getGuildCommands());
			player.sendMsg(msg);
		}
	}

	public void sendChat(Player player, AbstractGuildCommand command) {
		String key = null;
		int cityId = 0;
		if(command.getType() == GuildCommandType.CommandAtk.getType()) {
			key = "broadcast_21";
			cityId = ((GuildAtkCommand)command).getCityId();
		}else if(command.getType() == GuildCommandType.CommandDef.getType()) {
			key = "broadcast_20";
			cityId = ((GuildDefCommand)command).getCityId();
		}
		if(StrUtil.isEmpty(key)) {
			return;
		}
		CityTemplate cityTemplate = cityConfig.getCityById(cityId);
		if(cityTemplate == null) {
			return;
		}
		String content = languageCnTemplateConfig.getValue(key);
		String cityName = languageCnTemplateConfig.getValue(cityTemplate.getName());
		content = String.format(content, player.getName(),cityName);
		innerChatFacade.sendSysMsg(player, content, ChatRoomType.Guild);
	}

	public AbstractGuildCommand checkAndCreate(Player player,Guild guild, JsonMsg msg) {
		int type = msg.getInt("type");
		if(type == GuildCommandType.CommandAtk.getType()) {
			int cityId = msg.getInt("cityId");
			WorldCity worldCity = WorldServerContainer.of(guild).getWorldCity(cityId);
			if(worldCity == null
//					|| worldCity.getBelongGuildId() == guild.getId()
			) {
				player.sendErrorMsg(SysConstant.Guild_CommandAtk_SameGuild);
				return null;
			}
			return new GuildAtkCommand(cityId);
		}
		if(type == GuildCommandType.CommandDef.getType()) {
			int cityId = msg.getInt("cityId");
			WorldCity worldCity = WorldServerContainer.of(guild).getWorldCity(cityId);
			if(worldCity == null
//					|| worldCity.getBelongGuildId() != guild.getId()
			) {
				player.sendErrorMsg(SysConstant.Guild_CommandDef_NotSameGuild);
				return null;
			}
			return new GuildDefCommand(cityId);
		}
		if(type == GuildCommandType.CommandLine.getType()) {
			List<Integer> wayList = StringUtil.splitStr2IntegerList(msg.getString("ways"), ",");
			//检查是否能把通过这些点
			if(wayList.size() < 2 || !cityConfig.isFitWays(wayList)){
				return null;
			}
			return new GuildLineCommand(wayList);
		}
		return null;
	}

}








