package com.hm.action.mail;

import cn.hutool.core.collection.CollUtil;
import com.hm.enums.MailState;
import com.hm.libcore.language.LanguageVo;
import com.hm.model.item.Items;
import com.hm.model.mail.Mail;
import com.hm.model.mail.MailExtraVo;
import com.hm.model.player.Player;
import lombok.Data;

import java.util.List;



@Data
public class MailVO {
	private String id; //邮件ID
	private long sendDate; //发送时间
	private int state; //状态
	private List<Items> reward; // 奖励
	private int mailType; //类型MailConfigEnum
	private int type;//系统邮件
	private MailExtraVo mailExtraVo;

	public MailVO(Player player, Mail mail) {
		this.id = mail.getId();
		this.mailType = mail.getMailId();
		this.sendDate = mail.getSendDate();
		this.state = player.playerMail().getMailState(mail.getId());
		this.reward = mail.getReward();
		this.mailExtraVo = mail.getMailExtraVo();
	}

	//删除优先级
	//已读无附件
	//已读已领取附件邮件
	//未读无附件邮件
	//未收取附件的邮件（带附件一起删除）
	//同一优先级的按时间先后顺序删除
	public int getSort(){
		if(MailState.Read.getType() == this.getState() && CollUtil.isEmpty(this.getReward())){
			return 1;
		}
		if(MailState.Get.getType() == this.getState()){
			return 2;
		}
		if(MailState.NewMail.getType() == this.getState()){
			return 3;
		}
		return 100;
	}
	
	
}
  
