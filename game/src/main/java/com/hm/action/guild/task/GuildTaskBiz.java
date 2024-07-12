package com.hm.action.guild.task;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.GuildTaskConfig;
import com.hm.config.excel.templaextra.GuildTaskTypeTemplate;
import com.hm.container.PlayerContainer;
import com.hm.enums.CommonValueType;
import com.hm.enums.RankType;
import com.hm.leaderboards.HdLeaderboardsService;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.guild.GuildTask;
import com.hm.model.player.Player;
import com.hm.model.serverpublic.ServerDataManager;
import com.hm.model.serverpublic.ServerGuildTaskData;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import javax.annotation.Resource;

@Slf4j
@Biz
public class GuildTaskBiz implements IObserver{
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private GuildTaskConfig guildTaskConfig;
	@Resource
	private CommValueConfig commValueConfig;
	

	
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.CityBattleResult, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ShopBuy, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.OccupyCity, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.CostRes, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		if(player == null || player.getGuildId() <= 0) {
			return;
		}
		Guild guild = guildBiz.getGuild(player);
		if(guild == null || !isGuildCanTask(guild)) {
			return;
		}
		GuildTask guildTask = guild.getGuildTask();
		GuildTaskType guildTaskType = ServerDataManager.getIntance().getServerData(player.getServerId()).getServerGuildTaskData().getTaskType();
		if(guildTaskType != null && guildTaskType.isFitObservableEnum(observableEnum)) {
			GuildTaskTypeTemplate typeTemplate = guildTaskConfig.getGuildTaskTypeTemplate(guildTaskType);
			if(typeTemplate == null) {
				log.error(guildTaskType+"部落任务积分不存在!");
				return;
			}
			int score = guildTaskType.chkEffect(typeTemplate,observableEnum, argv);
			if(score <= 0) {
				return;
			}
			guildTask.addScore(player.getId(), score);
			HdLeaderboardsService.getInstance().updatePlayerRankForAdd(player, RankType.PlayerGuildScore, score);
			guild.saveDB();
			
			broadGuildScoreChange(guild);
			//获取部落积分
			player.notifyObservers(ObservableEnum.GuildScoreAdd, score);
		}
	}

	public boolean isGuildCanTask(Guild guild){
		return guild.guildLevelInfo().getLv() >= commValueConfig.getCommValue(CommonValueType.GuildTaskLv);
	}
	
	public void doWeekReset(int serverId) {
		ServerGuildTaskData serverGuildTaskData = ServerDataManager.getIntance().getServerData(serverId).getServerGuildTaskData();
		serverGuildTaskData.resetData();
		//重置部落积分
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_ServerGuildTaskChange);
		serverMsg.addProperty("serverGuildTaskData", serverGuildTaskData);
		PlayerContainer.broadPlayer(serverId, serverMsg);
		
	}
	//广播阵部落积分变化
	public void broadGuildScoreChange(Guild guild) {
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_Broad_GuildScoreChange);
		serverMsg.addProperty("guildTask", guild.getGuildTask());
		guildBiz.broadGuildMember(guild, serverMsg);
	}
}
