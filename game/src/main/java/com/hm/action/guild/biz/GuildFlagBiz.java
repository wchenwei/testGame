package com.hm.action.guild.biz;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.ItemType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.enums.StatisticsType;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.IObservable;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.servercontainer.guild.GuildContainer;
import com.hm.util.RandomUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

//修改部落名字，旗帜信息
@Biz
public class GuildFlagBiz implements IObservable{
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private GuildBiz guildBiz;

	
	//获取修改名字的消耗
	public Items getChangeNameCost() {
		int changeNameCost = commValueConfig.getCommValue(CommonValueType.GuildChangName);
		return new Items(PlayerAssetEnum.Gold.getTypeId(), changeNameCost, ItemType.CURRENCY.getType());
	}

	//修改部落的名字，旗帜信息
	public void changeGuildName(Guild guild,String guildName) {
		guild.getGuildInfo().setGuildName(guildName);
		guild.saveDB();
		//同步部落的在线玩家
		guild.broadMemberGuildUpdate();
		this.notifyChanges(ObservableEnum.GuildChangeNameOrFlag, null, guild);
	}

	public void changeGuildFlag(Guild guild, String guildFlag) {
		guild.getGuildFlag().setFlagName(guildFlag);
		guild.getGuildStatistics().addLifeStatistics(StatisticsType.GuildChangeFlag);
		guild.saveDB();
		//同步部落的在线玩家
		guild.broadMemberGuildUpdate();
		this.notifyChanges(ObservableEnum.GuildChangeNameOrFlag, null, guild);
	}

	public String randomGuildFlag(int serverId){
		List<String> allFlagList = Lists.newArrayList(GuildFlagManger.getFlagList());
		List<String> usedFlagList = getUsedGuildFlagList(serverId);
		allFlagList.removeAll(usedFlagList);
		return RandomUtils.randomEle(allFlagList);
	}

	// 已使用的部落旗帜
	public List<String> getUsedGuildFlagList(int serverId){
		return GuildContainer.of(serverId).getAllGuild().stream()
				.map(e -> e.getGuildFlag().getFlagName())
				.collect(Collectors.toList());
	}



	@Override
	public void notifyChanges(ObservableEnum observableEnum, Player player, Object... argv) {
		ObserverRouter.getInstance().notifyObservers(observableEnum, player, argv);
	}
}
