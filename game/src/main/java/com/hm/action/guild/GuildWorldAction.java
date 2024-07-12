package com.hm.action.guild;

import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.guild.biz.GuildCityBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.CityConfig;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.camp.CityRewardShow;
import com.hm.model.camp.SimpleCityRewardShow;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: GuildWorldAction. <br/>  
 * Function: 部落的城池消息处理. <br/>  
 * date: 2018年11月17日 下午1:44:42 <br/>  
 * @author zxj  
 * @version
 */
@Action
public class GuildWorldAction extends AbstractGuildAction{
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private CityConfig cityConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private GuildCityBiz guildCityBiz;

	// 部落城池产出信息
	@MsgMethod(MessageComm.C2S_GuildCityReward)
	public void guildCityReward(Player player, Guild guild, JsonMsg msg) {
		SimpleCityRewardShow rewardShow = guildCityBiz.getSimpleCityRewardShow(player, guild);
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_GuildCityReward);
		serverMsg.addProperty("cityReward", rewardShow);
		player.sendMsg(serverMsg);
	}

	// 部落城池产出详细日志
	@MsgMethod(MessageComm.C2S_GuildCityRewardDetail)
	public void guildCityRewardDetail(Player player, Guild guild, JsonMsg msg) {
		List<CityRewardShow> cityRewardShows = guildCityBiz.getCityRewardShow(player, guild);

		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_GuildCityRewardDetail);
		serverMsg.addProperty("cityRewardList", cityRewardShows);
		player.sendMsg(serverMsg);
	}
	
	//领取部落城池的奖励信息#msg:30037
	@MsgMethod(MessageComm.C2S_Guild_WorldReward)
    public void getWorldReward(Player player, Guild guild, JsonMsg msg) {
		//添加城市奖励信息
		List<CityRewardShow> cityRewardShows = guildCityBiz.getCityRewardShow(player, guild);
		if(cityRewardShows.isEmpty()) {
			return;
		}
		List<Items> items = cityRewardShows.stream()
				.flatMap(e -> e.getAllItems().stream()).collect(Collectors.toList());
		long maxTime = cityRewardShows.stream().mapToLong(e -> e.getTime()).max().orElse(System.currentTimeMillis());
		
		//增加奖励
		itemBiz.addItem(player, items, LogType.GuildWorldReward);
		//清除已经领取过的奖励
		player.playerGuild().updateLastCityTime(maxTime);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Guild_WorldReward, itemBiz.createItemList(items));
	}
}








