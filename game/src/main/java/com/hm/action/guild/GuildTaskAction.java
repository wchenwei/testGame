package com.hm.action.guild;

import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.task.GuildTaskBiz;
import com.hm.action.guild.task.GuildTaskType;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildConfig;
import com.hm.config.excel.GuildTaskConfig;
import com.hm.config.excel.temlate.GuildLevelTemplate;
import com.hm.config.excel.templaextra.GuildTaskTemplate;
import com.hm.enums.LogType;
import com.hm.libcore.annotation.Action;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.GuildTask;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.sysConstant.SysConstant;
import com.hm.util.ItemUtils;

import javax.annotation.Resource;
import java.util.List;

@Action
public class GuildTaskAction extends AbstractGuildAction{
	@Resource
	private GuildTaskConfig guildTaskConfig;
	@Resource
	private GuildBiz guildBiz;
	@Resource
    private ItemBiz itemBiz;
	@Resource
	private GuildConfig guildConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private GuildTaskBiz guildTaskBiz;
	
	
	@MsgMethod(MessageComm.C2S_Guild_TaskReward)
    public void rewardGuildTask(Player player, Guild guild, JsonMsg msg) {
		if(!guildTaskBiz.isGuildCanTask(guild)) {
			player.sendErrorMsg(SysConstant.Guild_Lv_Not_Enough);
			return;
		}
		//下一阶段的id
		int nextId = player.playerGuild().getGuildTaskPos()+1;
		GuildTask guildTask = guild.getGuildTask();
		GuildTaskType guildTaskType = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerGuildTaskData().getTaskType();
		GuildTaskTemplate guildTaskTemplate = guildTaskConfig.getGuildTaskTemplate(guildTaskType, nextId);
		if(guildTaskTemplate == null || guildTask.getScore() < guildTaskTemplate.getPoints()) {
			player.sendErrorMsg(SysConstant.Guild_Score_Not_Enough);
			return;
		}
		player.playerGuild().setGuildTaskPos(nextId);
		
		LogType logType = LogType.GuildTaskReward.value(nextId);
		List<Items> itemList = guildTaskTemplate.getRewardList();
		//计算部落等级加成
		GuildLevelTemplate guildLevelTemplate = guildConfig.getGuildLevelTemplate(guild.guildLevelInfo().getLv());
		double addRate = guildLevelTemplate != null ? guildLevelTemplate.getEmploy_reward_buff():0;
		itemList = ItemUtils.calItemExtraAdd(itemList, addRate);
		
		itemBiz.addItem(player, itemList, logType);
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = new JsonMsg(MessageComm.S2C_Guild_TaskReward);
		serverMsg.addProperty("itemList", itemList);
		player.sendMsg(serverMsg);
		
	}
}
