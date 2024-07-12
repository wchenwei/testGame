 /**  
 * Project Name:SLG_GameFuture.
 * File Name:MailAction.java  
 * Package Name:com.hm.action.mail  
 * Date:2017年12月18日下午3:50:23  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */  
  
package com.hm.action.mail;

 import cn.hutool.core.collection.CollectionUtil;
 import com.google.common.collect.Lists;
 import com.hm.libcore.annotation.MsgMethod;
 import com.hm.libcore.msg.JsonMsg;
 import com.hm.action.AbstractPlayerAction;
 import com.hm.action.item.ItemBiz;
 import com.hm.action.mail.biz.MailBiz;
 import com.hm.enums.LogType;
 import com.hm.enums.MailState;
 import com.hm.log.LogBiz;
 import com.hm.message.MessageComm;
 import com.hm.model.item.Items;
 import com.hm.model.mail.Mail;
 import com.hm.model.player.Player;
 import com.hm.observer.ObservableEnum;
 import com.hm.servercontainer.mail.MailServerContainer;
 import com.hm.sysConstant.SysConstant;
 import com.hm.libcore.annotation.Action;

 import javax.annotation.Resource;
 import java.util.List;

/**  
 * ClassName: MailAction. <br/>  
 * date: 2017年12月18日 下午3:50:23 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Action
public class MailAction extends AbstractPlayerAction {

	@Resource
	private ItemBiz itemBiz;
	@Resource
	private MailBiz mailBiz;
	@Resource
	private LogBiz logBiz;
	
	
	@MsgMethod ( MessageComm.C2S_ReadMail)
	public void read(Player player, JsonMsg msg){
		String id = msg.getString("id"); 
		Mail mail = MailServerContainer.of(player).getMail(id);
		if(mail == null){
			player.sendErrorMsg(SysConstant.MAIL_NOT_EXIST);
			return;
		}
		int mailState = player.playerMail().getMailState(id);
		if(mailState == MailState.Read.getType()
				|| mailState == MailState.ReadNoGet.getType()
				|| mailState == MailState.Get.getType()
				|| mailState == MailState.Del.getType()
				) {
			return;
		}
		player.playerMail().readMail(mail);
		player.saveDB();
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_ReadMail);
		serverMsg.addProperty("id",id);
		serverMsg.addProperty("state",player.playerMail().getMailState(id));
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod ( MessageComm.C2S_GetMailReward)
	public void getReward(Player player, JsonMsg msg){
		String id = msg.getString("id"); 
		Mail mail = MailServerContainer.of(player).getMail(id);
		if(mail == null){
			player.sendErrorMsg(SysConstant.MAIL_NOT_EXIST);
			return;
		}
		//是否有附件
		if(!mail.isHaveReward()){
			player.sendErrorMsg(SysConstant.Mail_Reward_NOT_EXIST);
			return;
		}
		//是否已领
		int mailState = player.playerMail().getMailState(mail.getId());
		if(mailState == MailState.Get.getType()
				|| mailState == MailState.Del.getType()){
			player.sendErrorMsg(SysConstant.Mail_Reward_GET);
			return;
		}
		player.notifyObservers(ObservableEnum.MailGetReward);
		
		itemBiz.addItem(player, mail.getReward(), LogType.Mail_Get.value(mail.getMailId()));
		player.playerMail().getReward(id);
		player.sendUserUpdateMsg();
		
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_GetMailReward);
		serverMsg.addProperty("id", id);
		serverMsg.addProperty("state",player.playerMail().getMailState(id));
		serverMsg.addProperty("reward", mail.getReward());
		player.sendMsg(serverMsg);
	}
	
	@MsgMethod ( MessageComm.C2S_ReadAllMail)
	public void readAll(Player player, JsonMsg msg){
		List<Items> rewardList = mailBiz.readAllMail(player);
		if(!rewardList.isEmpty()){
			//此处不再记录收入日志，上一层已经在合并奖励前记录过
			itemBiz.addItem(player, rewardList, null);
			player.notifyObservers(ObservableEnum.MailReadAll);
			player.sendUserUpdateMsg();
		}
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_ReadAllMail);
		serverMsg.addProperty("reward", rewardList);
		serverMsg.addProperty("mailList", mailBiz.getMailVOList(player));
		player.sendMsg(serverMsg);
		
	}
	
	@MsgMethod ( MessageComm.C2S_DelAllMail)
	public void delAllMail(Player player, JsonMsg msg){
		List<String> mailList = player.playerMail().delAll();
		player.saveDB(); 
		// 记录日志
		if (CollectionUtil.isNotEmpty(mailList)) {
			logBiz.addDelMailLog(player, mailList);
		}
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_DelAllMail);
		serverMsg.addProperty("mailList", mailBiz.getMailVOList(player));
		player.sendMsg(serverMsg);
	}

	@MsgMethod (MessageComm.C2S_DelOne_Mail)
	public void delOneMail(Player player, JsonMsg msg){
		String mailId = msg.getString("id");
		boolean deleteMail = player.playerMail().removeMail(mailId);
		if(!deleteMail){
			return;
		}
		// 记录日志
		logBiz.addDelMailLog(player, Lists.newArrayList(mailId));
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_DelOne_Mail);
		serverMsg.addProperty("id", mailId);
		player.sendUserUpdateMsg();
		player.sendMsg(serverMsg);
	}
}
  
