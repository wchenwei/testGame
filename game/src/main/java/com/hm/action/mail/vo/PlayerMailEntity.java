package com.hm.action.mail.vo;

import com.hm.enums.MailSendType;
import com.hm.enums.MailState;
import com.hm.model.mail.Mail;
import com.hm.model.player.Player;

import java.util.Date;



/**
 * GM查询玩家邮件实体类
 * ClassName: PlayerMailEntity. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年5月23日 下午3:33:21 <br/>  
 *  
 * @author yanpeng  
 * @version
 */
public class PlayerMailEntity {
	private String id;
	private String title;
	private String state;
	private int type;
	private Date sendTime; 
	private String content;
	private String reward;
	private String result; 
	
	public PlayerMailEntity(Player player,Mail mail){
		this.id = mail.getId();
		this.type = mail.getSendType();
		this.sendTime = new Date(mail.getSendDate());
		if(player.playerMail().isHaveMail(mail)){
			MailState mailState = MailState.getState(player.playerMail().getState(id));
			this.state =mailState.getDesc();
		}else{
			this.state = MailState.Del.getDesc(); 
		}
		this.title=MailSendType.getMailType(mail.getSendType()).getDesc();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	
}
