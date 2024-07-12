/**  
 * Project Name:SLG_GameFuture.
 * File Name:MailContainer.java  
 * Package Name:com.hm.container  
 * Date:2017年12月19日上午10:48:53  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */  
  
package com.hm.servercontainer.mail;


import com.google.common.collect.Maps;
import com.hm.action.http.gm.GmMailManager;
import com.hm.cache.MailCacheManager;
import com.hm.config.GameConstants;
import com.hm.db.MailUtils;
import com.hm.enums.MailSendType;
import com.hm.model.mail.Mail;
import com.hm.model.player.Player;
import com.hm.servercontainer.ItemContainer;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
public class MailItemContainer extends ItemContainer{
	private Map<String,Mail> allMailMap = Maps.newConcurrentMap(); //全服邮件
	
	public MailItemContainer(int serverId) {
		super(serverId);  
	}
	
	@Override
	public void initContainer() {
		//加载全服邮件
		List<Mail> allMails = MailUtils.getMailList(this.getServerId(),MailSendType.All.getType());
		for(Mail mail:allMails){
			allMailMap.put(mail.getId(), mail);
		}
		//加载gm后台邮件[玩家注册邮件]
		for (Mail mail : GmMailManager.getInstance().getGmMailForServer(getServerId())) {
			allMailMap.put(mail.getId(), mail);
		}
	}
	
	
	
	/**
	 * 登陆时检查是否有系统邮件
	 * @param player
	 */
	public void loadPlayerSysMail(Player player) {
		// 判断是否有新的全服邮件
		for(Mail mail :allMailMap.values()) {
			if(isAddPlayerMail(player, mail)) {
				player.playerMail().addMail(mail);
			}
		}
		if(player.playerMail().Changed()) {
			player.saveDB();
		}
	}
	
	
	/**
	 * 添加邮件
	 *
	 * @author yanpeng 
	 * @param mail  
	 *
	 */
	public void addMail(Mail mail) {
		if(mail.getSendType() == MailSendType.All.getType()
				|| mail.getSendType() == MailSendType.PlayerGuild.getType()) {
			allMailMap.put(mail.getId(), mail);
		}else {
			MailCacheManager.getInstance().addMail(mail);
		}
	}
	
	/**
	 * 获取邮件
	 *
	 * @author yanpeng 
	 * @param id
	 * @return  
	 *
	 */
	public Mail getMail(String id) {
		Mail mail = allMailMap.get(id);
		if(mail == null) {
			mail = MailCacheManager.getInstance().getMail(id);
		}
		return mail;
	}


	/**
	 * 判断玩家是否有未发送的系统邮件
	 *
	 * @param player
	 * @param mail
	 * @return
	 * @author yanpeng
	 */
	private boolean isAddPlayerMail(Player player, Mail mail) {
		if (mail.getSendType() == MailSendType.All.getType()) {
			return mail.isCanAddForPlayer(player);
		}
		return false;
	}

	/**
	 * 定时器检查邮件是否可以删除
	 */
	public void checkMailExpire() {
		for (Mail value : allMailMap.values()) {
			if (value.isTimeExpiredForDel()) {//删除超过30天 没有附件的邮件
				delMail(value);
			}
		}
	}

	private void delMail(Mail mail){
		log.error(mail.getSendType() + "_" + mail.getId() + " mail del");
		allMailMap.remove(mail.getId());
		MailCacheManager.getInstance().removeMail(mail.getId());
		MailUtils.delete(mail);
	}
}
  
