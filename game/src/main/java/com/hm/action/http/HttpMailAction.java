package com.hm.action.http;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.action.language.MailCustomContent;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.http.gm.GmMailManager;
import com.hm.action.item.ItemBiz;
import com.hm.action.mail.biz.MailBiz;
import com.hm.action.mail.vo.PlayerMailEntity;
import com.hm.cache.RechargeOrderCacheManager;
import com.hm.config.excel.GiftPackageConfig;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.GiftPackageTemplate;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.db.PlayerUtils;
import com.hm.enums.ActionType;
import com.hm.enums.MailConfigEnum;
import com.hm.enums.MailSendType;
import com.hm.log.LogBiz;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.mail.CreateServerMailFilter;
import com.hm.model.player.Player;
import com.hm.server.GameServerManager;
import com.hm.util.ItemUtils;
import com.hm.util.PubFunc;
import com.hm.util.ServerUtils;
import com.hm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import com.hm.libcore.annotation.Action;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("http.mail")
public class HttpMailAction{
	@Resource
	private MailBiz mailBiz; 
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private GiftPackageConfig giftPackageConfig;

	@Resource
	private MailConfig mailConfig;

	@Resource
	private LogBiz logBiz;

	/**
	 * 发送系统邮件
	 *
	 * @param session
	 * @author yanpeng
	 */
	public void sendSysMail(HttpSession session) {
		try {
			Map<String, String> map = session.getParams();
			String idStr = new String();
			int serverId = PubFunc.parseInt(map.get("serverId"));
			int type = Integer.parseInt(map.get("type"));//MailSendType
			if (checkServerId(serverId, type)) {
				return;
			}
			String title = URLDecoder.decode(map.get("title"), "utf-8");
			String content = URLDecoder.decode(map.get("content"), "utf-8");
//			MailCustomContent customContent = GSONUtils.FromJSONString(content, MailCustomContent.class);
			MailCustomContent customContent = MailCustomContent.buildDefault(title,content);

			List<Items> reward = Lists.newArrayList();
			if (map.containsKey("itemStr") && map.get("itemStr") != null) {
				String itemStr = URLDecoder.decode(map.get("itemStr"), "utf-8");
				reward = ItemUtils.str2ItemList(itemStr, ",", ":");
			}
			if (type == MailSendType.All.getType()) {// 全服发送
				mailBiz.sendCustomAllMail(serverId, customContent, reward);
			} else if (type == MailSendType.Group.getType()) {// 发送给指定玩家
				if (map.containsKey("ids") && map.get("ids") != null) {
					idStr = URLDecoder.decode(map.get("ids"), "utf-8");
					List<Long> playerIds = Arrays.asList(idStr.split(",")).stream()
							.map(Long::parseLong)
							.filter(e -> ServerUtils.isServerPlayer(serverId, e))
							.collect(Collectors.toList());
					Set<Long> receivers = Sets.newHashSet(playerIds);
					mailBiz.sendCustomAllMail(serverId, receivers, customContent, reward);
				}
			} else if (type == MailSendType.One.getType()) {
				idStr = URLDecoder.decode(map.get("ids"), "utf-8");
				mailBiz.sendCustomAllMail(serverId, Sets.newHashSet(Long.parseLong(idStr)), customContent, reward);
			}
			else if (type == MailSendType.CreateServer.getType()) { //国家邮件
				int createServerId = Integer.parseInt(map.get("createServerId"));
				mailBiz.sendCustomAllMail(serverId, Sets.newHashSet(), customContent, reward,new CreateServerMailFilter(createServerId));
			}
			else if (type == MailSendType.PlayerIds.getType()) { //过滤本服玩家
				idStr = map.get("ids");
				Map<Integer, Set<Long>> serverMap = createServerPlayerIds(StringUtil.splitStr2IntegerList(idStr, ","));
				for (int tempServerId : serverMap.keySet()) {
					try {
						Set<Long> receivers = serverMap.get(tempServerId);
						log.error(tempServerId + "发送playerids邮件:" + GSONUtils.ToJSONString(receivers));
						mailBiz.sendCustomAllMail(tempServerId, receivers, customContent, reward);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			} else if(type==MailSendType.PlayerGuild.getType()){
				idStr = map.get("ids");
				List<Integer> ids = StringUtil.splitStr2IntegerList(idStr,",");
				Set<Guild> guildSet = createGuildByPlayerIds(ids);
				for (Guild guild : guildSet){
					try {
						mailBiz.sendCustomAllMail(guild, customContent, reward);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (type == MailSendType.RegisterDate.getType()) { //注册邮件
				int mailId = Integer.parseInt(map.get("mailId"));
				GmMailManager.getInstance().loadGmMail(mailId);
			} else {
				session.Write("fail");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.Write("fail");
			return;
		}
		session.Write("succ");
	}

	/**
	 * 检查server是否存在
	 * @param type
	 * @return
	 */
	private boolean checkServerId(int serverId,int type){
		if (MailSendType.RegisterDate.getType() == type) {
			return false;
		}
		if (!GameServerManager.getInstance().isDbServer(serverId) &&
				type!=MailSendType.PlayerIds.getType() && type!=MailSendType.PlayerGuild.getType()){
			return true;
		}
		return false;
	}

	/**
	 * 玩家部落
	 * @param ids
	 * @return
	 */
	private Set<Guild> createGuildByPlayerIds(List<Integer> ids) {
		Set<Guild> guildSet = Sets.newHashSet();
		for (long playerId : ids){
			Player player = PlayerUtils.getPlayer(playerId);
			if (player != null){
				Guild guild = guildBiz.getGuild(player);
				if (guild != null){
					guildSet.add(guild);
				}
			}
		}
		return guildSet;
	}

	/**
	 * 查询玩家邮件
	 *
	 * @author yanpeng 
	 * @param session  
	 *
	 */
	public void queryPlayerMail(HttpSession session) {
		try {
			Map<String, String> map = session.getParams();
			long playerId = Long.parseLong(map.get("playerId"));
			int delState = Integer.parseInt(map.get("delState"));
			Player player = PlayerUtils.getPlayer(playerId);
			if(player != null){
				if(delState == 0){
					int pageNo = Integer.parseInt(map.get("pageNo"));
					int pageSize = Integer.parseInt(map.get("pageSize"));
					int total = player.playerMail().getCount();
					List<PlayerMailEntity> mailList = mailBiz.queryPlayerMailByPage(player,pageNo,pageSize);
					Map<String, Object> postMap = Maps.newHashMap(); 
					postMap.put("total", total);
					postMap.put("mailList", mailList);
					String json = JSON.toJSONString(postMap);
					log.info("queryPlayerMail delState=0:"+json);
					session.Write(json);
				}else if(delState ==1){
				 	 String mailIds = map.get("mailIds");
					 List<PlayerMailEntity> mailList = mailBiz.queryPlayerDelMail(player, mailIds);
					 String json = JSON.toJSONString(mailList);
					 log.info("queryPlayerMail delState=1:"+json);
					 session.Write(json);
				}
			}else{
				log.info("queryPlayerMail playerId="+playerId+" is not exist:");
				session.Write("fail");
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.Write("fail");
		}
	}
	
	
	public static Map<Integer,Set<Long>> createServerPlayerIds(List<Integer> ids) {
		Map<Integer,Set<Long>> serverMap = Maps.newHashMap();
		for (long playerId : ids) {
			int serverId = ServerUtils.getCreateServerId(playerId);
			if(GameServerManager.getInstance().containServer(serverId)) {
				int dbId = GameServerManager.getInstance().getDbServerId(serverId);
				Set<Long> serverIds = serverMap.get(dbId);
				if(serverIds == null) {
					serverIds = Sets.newHashSet();
					serverMap.put(dbId, serverIds);
				}
				serverIds.add(playerId);
			}
		}
		return serverMap;
	}
	public void send9yMail(HttpSession session) {
		Map<String, String> map = session.getParams();
		int serverId = Convert.toInt(map.get("serverId"), -1);
		int giftId = Convert.toInt(map.get("giftId"), -1);
		long playerId = Convert.toInt(map.get("playerId"), -1);

		String cacheKey = String.format("9youweb_%d_%d_%d", serverId, playerId, giftId);
		if (RechargeOrderCacheManager.getInstance().orderIsExist(cacheKey)) {
			session.Write("succ");
			return;
		}

		if (!checkGiftId(giftId)) {
			session.Write("fail");
			return;
		}

		if (!GameServerManager.getInstance().isDbServer(serverId)) {
			session.Write("fail");
			return;
		}

		Player player = PlayerUtils.getPlayer(playerId);
		if (player == null) {
			session.Write("fail");
			return;
		}

		GiftPackageTemplate cfg = giftPackageConfig.getGiftPackageTemplateById(giftId);
		if (cfg == null || CollUtil.isEmpty(cfg.getItemList())) {
			session.Write("fail");
			return;
		}

		MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.NineYouGameGift);
		mailBiz.sendSysMail(serverId, playerId, mailTemplate, cfg.getItemList());
		RechargeOrderCacheManager.getInstance().addOrderId(cacheKey);

		logBiz.addPlayerActionLog(player, ActionType.Gift9You, String.format("%d,%d", playerId, giftId));
		session.Write("succ");
	}

	/**
	 * 9you 礼包可用id区间
	 *
	 * @param giftId
	 * @return
	 */
	private boolean checkGiftId(int giftId) {
		return 60000 <= giftId && giftId <= 60999;
	}
}


