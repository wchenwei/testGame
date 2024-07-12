package com.hm.chat;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.hm.action.http.biz.HttpBiz;
import com.hm.annotation.Broadcast;
import com.hm.config.CityConfig;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.container.PlayerContainer;
import com.hm.message.ChatMessageComm;
import com.hm.libcore.json.JSONUtil;
import com.hm.libcore.msg.JsonMsg;
import com.hm.model.guild.tactics.AbstractCityTactics;
import com.hm.model.guild.tactics.AbstractGuildTactics;
import com.hm.model.player.Player;
import com.hm.observer.NormalBroadcastAdapter;
import com.hm.observer.ObservableEnum;
import com.hm.rpc.RpcManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatObserverBiz extends NormalBroadcastAdapter {
	@Resource
	private CityConfig cityConfig;
	@Resource
	private LanguageCnTemplateConfig langeConfig;
	@Resource
    private InnerChatFacade innerChatFacade;
	@Resource
    private HttpBiz httpBiz;

	@Broadcast(ObservableEnum.PlayerLoginSuc)
	public void doPlayerLogin(ObservableEnum observableEnum, Player player, Object... argv) {
		JsonMsg msg = new JsonMsg(ChatMessageComm.G2C_PlayerLogin);
		msg.setPlayerId(player.getId());
		msg.addProperty("playerId", player.getId());
		msg.addProperty("serverId", player.getServerId());
		msg.addProperty("guildId", player.getGuildId());
		ChatRpcUtils.sendMsg(msg);
	}


	@Broadcast(ObservableEnum.MinuteEvent)
	public void syncNoChatIds(ObservableEnum observableEnum, Player player, Object... argv) {
		RpcManager.broadAll();//广播rpc数据

		int minute = DateUtil.thisMinute();
        //每5分钟请求一次
        if (minute % 5 != 0) {
            return;
        }
        String ids = httpBiz.getNoChatIds();
		List<Long> noChatIds = StrUtil.isEmpty(ids)? Lists.newArrayList():JSONUtil.fromJson(ids, new TypeReference<ArrayList<Long>>() {
		});
        PlayerContainer.loadNoChatIds(noChatIds);
    }

	@Broadcast(ObservableEnum.UseTactics)
	public void sendUseUseTacticsGuildChat(ObservableEnum observableEnum, Player player, Object... argv) {
		AbstractGuildTactics guildTactics = (AbstractGuildTactics) argv[0];

		if(guildTactics instanceof AbstractCityTactics) {
			String tacticsName = guildTactics.getType().getDesc();
			AbstractCityTactics cityTactics = (AbstractCityTactics)guildTactics;
			CityTemplate cityTemplate = cityConfig.getCityById(cityTactics.getCityId());
			if(cityTemplate == null) {
				return;
			}
			String guildChat = langeConfig.getValue("Giuld_tec_push");
			String playerName = player.getName();
			String cityName = langeConfig.getValue(cityTemplate.getName());
			guildChat = String.format(guildChat, playerName, cityName,tacticsName);
			innerChatFacade.sendSysMsg(player, guildChat, ChatRoomType.Guild);
		}
	}

}
