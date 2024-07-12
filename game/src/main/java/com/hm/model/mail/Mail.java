package com.hm.model.mail;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Sets;
import com.hm.action.language.MailCustomContent;
import com.hm.libcore.language.LanguageVo;
import com.hm.libcore.db.mongo.DBEntity;
import com.hm.config.GameConstants;
import com.hm.db.MailUtils;
import com.hm.enums.MailSendType;
import com.hm.libcore.util.GameIdUtils;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerMail;
import com.hm.sysConstant.MailConstant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;

/**
 * @author siyunlong
 * @version V1.0
 * @Description: 邮件
 * @date 2024/4/17 10:23
 */
@Data
@NoArgsConstructor
@Document(collection="Mail")
public class Mail extends DBEntity<String>{
	private int senderId; // 发送者ID，系统和后台邮件为0
	private transient int sendType; //发送类型MailSendType
	private long sendDate; //发送日期
	private int mailId; // 配置表的ID
	private LanguageVo[] params;


	private transient Set<Long> receivers = Sets.newHashSet(); //收件人
	private List<Items> reward; // 奖励

	//================================================
	private MailExtraVo mailExtraVo;//邮件额外信息
	private MailCustomContent customContent; //自定义邮件
	private IMailFilter mailFilter;

	public Mail(String id, int serverId, List<Items> rewards, MailSendType sendType) {
		setId(id);
		setServerId(serverId);
		this.sendDate = System.currentTimeMillis();
		this.reward = rewards;
		this.sendType = sendType.getType();
	}

	public Mail(String id, int serverId, int mailId, List<Items> rewards, MailSendType sendType, LanguageVo... params) {
		this(id, serverId, rewards, sendType);
		this.mailId = mailId;
		this.params = params;
	}

	public Mail(int serverId, int mailId, List<Items> rewards, MailSendType sendType, LanguageVo... params) {
		this(GameIdUtils.nextStrId(serverId),
				serverId, mailId, rewards, sendType, params);
	}

	public Mail(int serverId, MailCustomContent customContent, List<Items> rewards, MailSendType sendType) {
		this(GameIdUtils.nextStrId(serverId), serverId, rewards, sendType);
		this.customContent = customContent;
	}

	public Mail(int senderId, int serverId, List<Items> rewards, MailCustomContent customContent, MailSendType sendType) {
		this(serverId, customContent, rewards, sendType);
		this.senderId = senderId;
	}

	public Mail(int senderId, int serverId, Set<Long> receivers, List<Items> rewards, MailCustomContent customContent, MailSendType sendType) {
		this(senderId, serverId, rewards, customContent, sendType);
		this.receivers = receivers;
	}

	public void addReceivers(Set<Long> receivers) {
		this.receivers.addAll(receivers);
	}

	public void setReceivers(Set<Long> receivers) {
		this.receivers = receivers;
	}

	public boolean isHaveReward() {
		return CollUtil.isNotEmpty(reward);
	}


	public boolean isCanDel() {
		return !MailSendType.isAllMail(this.sendType);
	}

	//系统自动删除过期邮件
	public boolean isTimeExpiredForDel() {
		if (this.mailFilter != null && !this.mailFilter.checkAutoDelForTime()) {
			return false;
		}
		return System.currentTimeMillis() - getSendDate() > GameConstants.MailExpireDay * GameConstants.DAY
				&& CollUtil.isEmpty(this.reward);
	}

	public void saveDB() {
		MailUtils.saveMail(this);
	}

	public boolean isAddRedis() {
		return MailSendType.One.getType() == this.sendType || MailSendType.Group.getType() == this.sendType;
	}

	public boolean isCanAddForPlayer(Player player) {
		PlayerMail playerMail = player.playerMail();
		//玩家身上已有该邮件
		if (playerMail.isHaveMail(this))
			return false;

		//创号时间比邮件时间大
		if (player.playerBaseInfo().getCreateDate().getTime() > getSendDate())
			return false;

		//其他邮件条件
		if (!mailFilterIsCan(player))
			return false;

		return true;
	}


	public boolean mailFilterIsCan(Player player){
		return this.mailFilter == null || this.mailFilter.isFitPlayer(player);
	}


	/**
	 * 是否可以删除邮件
	 * @param mail
	 * @return
	 */
	public boolean canDel(Mail mail){
		return false;
	}


	public boolean isTimeExpired() {
		return System.currentTimeMillis() > this.sendDate+MailConstant.ExpiredDay*GameConstants.DAY;
	}
}
