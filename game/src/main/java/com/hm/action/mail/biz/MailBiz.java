/**  
 * Project Name:SLG_GameFuture.
 * File Name:MailBiz.java  
 * Package Name:com.hm.action.mail  
 * Date:2017年12月18日下午5:23:27  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */

package com.hm.action.mail.biz;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.gson.reflect.TypeToken;
import com.hm.action.language.MailCustomContent;
import com.hm.libcore.language.LanguageVo;
import com.hm.action.mail.MailVO;
import com.hm.action.mail.SystemMailVO;
import com.hm.action.mail.vo.GMMailVO;
import com.hm.action.mail.vo.PlayerMailEntity;
import com.hm.config.excel.MailConfig;
import com.hm.config.excel.temlate.MailTemplate;
import com.hm.container.PlayerContainer;
import com.hm.db.MailUtils;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.httpserver.handler.HttpSession;
import com.hm.libcore.msg.JsonMsg;
import com.hm.libcore.util.URLUtil;
import com.hm.libcore.util.gson.GSONUtils;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.guild.Guild;
import com.hm.model.item.Items;
import com.hm.model.mail.IMailFilter;
import com.hm.model.mail.Mail;
import com.hm.model.mail.MailBuilder;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.servercontainer.mail.MailItemContainer;
import com.hm.servercontainer.mail.MailServerContainer;
import com.hm.util.ItemUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.stream.Collectors;


/**  
 * ClassName: MailBiz. <br/>  
 * date: 2017年12月18日 下午5:23:27 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Slf4j
@Biz
public class MailBiz {
	@Resource
	private MailConfig mailConfig;
	@Resource
	private LogBiz logBiz;
	

	public void loadMailList(Player player) {
		MailServerContainer.of(player.getServerId()).loadPlayerSysMail(player);
		JsonMsg mailMsg = JsonMsg.create(MessageComm.S2C_LoadMailList);
		mailMsg.addProperty("mailList", getMailVOList(player));
		player.sendMsg(mailMsg);
	}
	
	public List<MailVO> getMailVOList(Player player){
		List<MailVO> mailVOList = Lists.newArrayList();
		for(Mail mail :loadMailFromDB(player)) {
			mailVOList.add(createMailVO(player, mail));
		}
		return mailVOList; 
	}
	
	private List<Mail> loadMailFromDB(Player player) {
		List<String> idList = player.playerMail().getMailIdList();
		MailItemContainer mailItemContainer = MailServerContainer.of(player);
		List<Mail> mailList = idList.stream().map(e -> mailItemContainer.getMail(e))
				.filter(Objects::nonNull).sorted(Comparator.comparingLong(Mail::getSendDate).reversed())
				.collect(Collectors.toList());
		return mailList;
	}

	public void sendSysMailWithTypeId(int serverId, Set<Long> receivers, int mailId, List<Items> reward, LanguageVo... parms) {
		if (receivers.isEmpty()) {
			return;
		}
		MailTemplate mailTemplate = mailConfig.getMailTemplate(mailId);
		if(mailTemplate == null) {
			log.error("邮件模板找不到!"+mailId);
			return;
		}

		MailSendType sendType = getMailSendType(receivers);
		Mail mail = new Mail(serverId, mailId, reward, sendType, parms);
		mail.setReceivers(receivers);
		saveAndSendMail(mail);
	}
	public void sendServerSysMailWithTypeId(int serverId,MailConfigEnum mailType, List<Items> reward, LanguageVo... parms) {
		MailTemplate mailTemplate = mailConfig.getMailTemplate(mailType);
		if(mailTemplate == null) {
			log.error("邮件模板找不到!"+mailType);
			return;
		}
		Mail mail = new Mail(serverId, mailType.getType(), reward,  MailSendType.All, parms);
		saveMail(mail);
		for (Player player : PlayerContainer.getOnlinePlayersByServerId(serverId)) {
			addMail(player, mail);
		}
	}

	public void sendSysMail(int serverId,Set<Long> receivers, MailConfigEnum mailType, List<Items> reward, LanguageVo... params) {
		sendSysMailWithTypeId(serverId, receivers, mailType.getType(), reward,params);
	}

	public void sendSysMail(int serverId, long playerId, MailTemplate mailTemplate, List<Items> reward, LanguageVo... params) {
		sendSysMailWithTypeId(serverId, Sets.newHashSet(playerId), mailTemplate.getMail_id(), reward, params);
	}

	public void sendSysMail(int serverId, long playerId, MailConfigEnum mailType, List<Items> reward, LanguageVo... params) {
		sendSysMailWithTypeId(serverId, Sets.newHashSet(playerId), mailType.getType(), reward, params);
	}

	public void sendSysMail(int serverId, long playerId, int mailId, List<Items> reward, LanguageVo... params) {
		sendSysMailWithTypeId(serverId, Sets.newHashSet(playerId), mailId, reward, params);
	}

	public void sendSysMail(BasePlayer player, MailConfigEnum mailType, List<Items> reward, LanguageVo... params) {
		sendSysMail(player.getServerId(), player.getId(), mailType, reward, params);
	}

	public void sendSysMail(BasePlayer player, int mailId, List<Items> reward, LanguageVo... params) {
		sendSysMail(player.getServerId(), player.getId(), mailId, reward, params);
	}

	public void sendSysMail(int serverId,Set<Long> playerIds,MailTemplate mailTemplate,List<Items> reward, LanguageVo... params){
		sendSysMailWithTypeId(serverId,playerIds,mailTemplate.getMail_id(),  reward, params);
	}


	public void sendSysMail(BasePlayer player,MailTemplate mailTemplate,List<Items> reward, LanguageVo... params){
		sendSysMailWithTypeId(player.getServerId(),Sets.newHashSet(player.getId()),mailTemplate.getMail_id(), reward,params);
	}


	public void sendSysMail(Guild guild,int mailId,List<Items> reward, LanguageVo... params){
		Set<Long> receivers = guild.getGuildMembers().getGuildMembers().stream().map(e -> e.getPlayerId()).collect(Collectors.toSet());
		sendSysMailWithTypeId(guild.getServerId(),receivers,mailId, reward,params);
	}

	//==========================自定义内容邮件================================================
	public void sendCustomAllMail(int serverId, MailCustomContent content, List<Items> reward) {
		Mail mail = new Mail(serverId, content, reward, MailSendType.All);
		saveMail(mail);

		for (Player player : PlayerContainer.getOnlinePlayersByServerId(serverId)) {
			addMail(player, mail);
		}
	}

	public void sendCustomAllMail(int serverId, Set<Long> receivers, MailCustomContent content, List<Items> reward) {
		sendCustomAllMail(serverId,receivers,content,reward,null);
	}

	public void sendCustomAllMail(Guild guild, MailCustomContent content, List<Items> reward) {
		sendCustomAllMail(guild.getServerId(), guild.getGuildMembers().getGuildMemberIds(), content, reward,null);
	}

	public void sendCustomAllMail(int serverId, Set<Long> receivers, MailCustomContent content, List<Items> reward, IMailFilter mailFilter) {
		if (receivers.isEmpty()) {
			return;
		}
		MailBuilder.group(serverId,receivers)
				.setCustomContent(content)
				.setReward(reward)
				.setMailFilter(mailFilter)
				.send();
	}

	/**
	 * 保存邮件
	 *
	 * @author yanpeng 
	 * @param record  
	 *
	 */
	public void saveMail(Mail mail){
		if(mail != null){
			mail.saveDB();
			MailServerContainer.of(mail.getServerId()).addMail(mail);
		}
	}
	
	/**
	 * 保存并发送邮件
	 *
	 * @author yanpeng 
	 * @param mail  
	 *
	 */
	public void saveAndSendMail(Mail mail){
		saveMail(mail);
		sendMail(mail);
	}
	
	/**
	 * 发送邮件
	 *
	 * @author yanpeng 
	 * @param record  
	 *
	 */
	private void sendMail(Mail mail){
		if(mail != null){
			mail.getReceivers().stream().forEach(id->addMail(id, mail));
		}
	}
	
	private void addMail(long playerId, Mail mail) {
		addMail(PlayerUtils.getPlayer(playerId), mail);
	}
	
	/**
	 * 添加到玩家收件箱
	 * @author yanpeng
	 * @param player
	 * @param mailId
	 */
	private void addMail(Player player, Mail mail) {
		if(player != null) {
			player.playerMail().addMail(mail);
			player.saveDB();
			player.sendMsg(MessageComm.S2C_AddMail,MailBiz.createMailVO(player, mail));
		}
	}

	/**
	 * 阅读所有邮件
	 *
	 * @author yanpeng 
	 * @param player
	 * @return  
	 *
	 */
	public List<Items> readAllMail(Player player) {
		Table<Integer, Integer, Items> itemTable = HashBasedTable.create();
		List<String> idList = player.playerMail().readAll();
		MailItemContainer mailItemContainer = MailServerContainer.of(player);
		idList.forEach(id -> {
			Mail mail = mailItemContainer.getMail(id);
			if(mail != null && mail.isHaveReward()) {
				mail.getReward().forEach(items -> {
					logBiz.addGoods(player, items.getId(), items.getCount(), items.getItemType(), LogType.Mail_Get.value(mail.getMailId()));
					player.notifyObservers(ObservableEnum.MailReward,items,LogType.Mail_Get.value(mail.getMailId()));
					if(itemTable.contains(items.getItemType(), items.getId())) {
						Items reward = items.clone();
						reward.addCount(itemTable.get(reward.getItemType(), reward.getId()).getCount());
						itemTable.put(items.getItemType(), items.getId(), reward);
					}else {
						itemTable.put(items.getItemType(), items.getId(), items);
					}
				});
			}
		});
		List<Items> rewardList = Lists.newArrayList(itemTable.values());
		return rewardList;
	}
	
	
	/**
	 * GM查询玩家邮件
	 * @author yanpeng 
	 * @param player
	 * @param mailId
	 * @return  
	 */
	public List<PlayerMailEntity> queryPlayerMailByPage(Player player,int pageNo,int pageSize){
		String mailId = null;
		int number = (pageNo-1)*pageSize-1; 
		List<String> ids = player.playerMail().getMailIdList(); 
		if(number>=0 && number <ids.size()){
			mailId = ids.get(number);
		}
		List<Mail> mailList = loadPlayerMailFromDB(player, mailId,pageSize,MailGroup.All.getType());
		List<PlayerMailEntity> entityList = Lists.newArrayList(); 
		for(Mail mail:mailList){
			entityList.add(createPlayerMailEntity(player,mail));
		}
		return entityList; 
	}
	
	/**
	 * GM查询已删除的邮件
	 * @author yanpeng 
	 * @param player
	 * @param mailIds
	 * @return  
	 */
	public List<PlayerMailEntity> queryPlayerDelMail(Player player,String mailIds){
		List<Mail> mailList = Lists.newArrayList(); 
		String[] ids = mailIds.split(","); 
		for(String id:ids){
			Mail mail = MailUtils.getMail(id);
			if(mail!=null){
				mailList.add(mail);
			}
		}
		List<PlayerMailEntity> entityList = Lists.newArrayList(); 
		for(Mail mail:mailList){
			entityList.add(createPlayerMailEntity(player,mail));
		}
		return entityList; 
	}
	
	
	private PlayerMailEntity createPlayerMailEntity(Player player,Mail mail){
		PlayerMailEntity entity = new PlayerMailEntity(player,mail);
		entity.setReward(ItemUtils.itemListToString(mail.getReward()));
		return entity; 
	}
	
	/**
	 * 加载玩家邮件
	 * @author yanpeng
	 * @param playerMailMap
	 * @return
	 */
	private List<Mail> loadPlayerMailFromDB(Player player, String mailId, int loadCount,int group) {
		List<Mail> mailList = Lists.newArrayList();
		ConcurrentNavigableMap<String, Integer> mailMap = player.playerMail().getMailSortMap();
		if(mailMap.isEmpty()) {
			return mailList;
		}
		String lastMailId = mailId; // 发给客户端最后一封邮件id
		// 登陆的时候加载,mailId为null
		if(mailId == null) {
			MailServerContainer.of(player).loadPlayerSysMail(player);
			mailId = mailMap.firstKey();
		}
		ConcurrentNavigableMap<String, Integer> map = mailMap.tailMap(mailId);
		int count = 0;
		MailItemContainer mailItemContainer = MailServerContainer.of(player);
		for(Map.Entry<String, Integer> entry :map.entrySet()) {
			String id = entry.getKey();
			int state = entry.getValue();
			Mail mail = mailItemContainer.getMail(id);
			if(id != lastMailId && state != MailState.Del.getType() 
					&& mail != null) {
				mailList.add(mail);
				count ++ ;
			}
			if(count == loadCount) {
				break;
			}
		}
		return mailList;
	}

    public void sendKfMail(HttpSession session) {
		try {
			int serverId = Integer.parseInt(session.getParams("serverId"));
            log.error(serverId + "===========收到跨服邮件=================");
			int mailId = Integer.parseInt(session.getParams("mailId"));
			List<Items> rewards = ItemUtils.str2ItemList(session.getParams("rewards"), ",", ":");
			List<Long> ids = GSONUtils.FromJSONString(session.getParams("ids"), new TypeToken<List<Long>>(){}.getType());
            String parmas = URLUtil.decodeUTF8(session.getParams("args"));
			List<String> args = GSONUtils.FromJSONString(parmas, new TypeToken<List<String>>(){}.getType());
            String[] msgArrays = args.stream().toArray(String[]::new);

			MailTemplate mailTemplate = mailConfig.getMailTemplate(MailConfigEnum.getMailType(mailId));
			if(mailTemplate!=null){
                log.error(mailId + "发放成功");
                sendSysMail(serverId, new HashSet<Long>(ids), mailTemplate, rewards,LanguageVo.createStr(msgArrays));
            } else {
                log.error(mailId + "不存在!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

    }

	public static MailVO createMailVO(Player player, Mail mail) {
		if (mail.getMailId() > 0) {
			return new SystemMailVO(player, mail);
		}
		return new GMMailVO(player, mail);
	}

	private MailSendType getMailSendType(Set<Long> receivers) {
		return receivers.size() == 1 ? MailSendType.One : MailSendType.Group;
	}
}  
