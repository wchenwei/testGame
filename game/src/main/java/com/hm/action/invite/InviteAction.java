package com.hm.action.invite;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.MsgMethod;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.AbstractPlayerAction;
import com.hm.action.cmq.CmqBiz;
import com.hm.action.invite.biz.InviteBiz;
import com.hm.action.item.ItemBiz;
import com.hm.config.GameConstants;
import com.hm.config.InviteConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.DropConfig;
import com.hm.config.excel.templaextra.InviteTemplate;
import com.hm.enums.CommonValueType;
import com.hm.enums.LogType;
import com.hm.message.MessageComm;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.observer.ObservableEnum;
import com.hm.redis.InviteInfoData;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.sysConstant.SysConstant;
import com.hm.libcore.annotation.Action;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Action
public class InviteAction extends AbstractPlayerAction{
	@Resource
	private InviteBiz inviteBiz;
	@Resource
	private InviteConfig inviteConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private CmqBiz cmqBiz;
	@Resource
	private DropConfig dropConfig;
	
	@MsgMethod(MessageComm.C2S_Invite_Bind)
    public void bind(Player player, JsonMsg msg) {
		/*if(player.playerLevel().getLv()>commValueConfig.getCommValue(CommonValueType.BeInviteLvLimit)){
			return;
		}
		String code = msg.getString("code");
		if(player.playerInviteInfo().getInviteId()>0){
			return;
		}
		if(StrUtil.equals(code, player.getInviteCode())){
			return;
		}
		int beBindId = RedisUtil.getPlayerIdByInviteCode(code);
		Player beBindPlayer = PlayerUtils.getPlayer(beBindId);
		if(beBindPlayer==null){
			//邀请码错误，找不到玩家
			player.sendErrorMsg(SysConstant.InviteCode_Error);
			return;
		}
		inviteBiz.bind(player,beBindPlayer);
		List<Items> rewards = commValueConfig.getListItem(CommonValueType.BeInviteReward);
		itemBiz.addItem(player, rewards, LogType.InviteReward);
		player.sendUserUpdateMsg();
		beBindPlayer.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Invite_Bind, rewards);
		
		cmqBiz.sendUserInvite(beBindPlayer.getUid(), player.getId());*/
		
		if(player.playerLevel().getLv()>commValueConfig.getCommValue(CommonValueType.BeInviteLvLimit)){
			return;
		}
		String code = msg.getString("code");
		if(player.playerInviteInfo().getInviteId()>0){
			return;
		}
		if(StrUtil.equals(code, player.getInviteCode())){
			return;
		}
		long beBindId = RedisUtil.getPlayerIdByInviteCode(code);
		PlayerRedisData beBindPlayer = RedisUtil.getPlayerRedisData(beBindId);
		if(beBindPlayer==null){
			//邀请码错误，找不到玩家
			player.sendErrorMsg(SysConstant.InviteCode_Error);
			return;
		}
		inviteBiz.bind(player,beBindId,beBindPlayer.getName());
		List<Items> rewards = commValueConfig.getListItem(CommonValueType.BeInviteReward);
		itemBiz.addItem(player, rewards, LogType.InviteReward);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Invite_Bind, rewards);
		
		cmqBiz.sendUserInvite(player.getUid(), beBindId);
	}
	
	
	@MsgMethod(MessageComm.C2S_Invite_Receive)
    public void receive(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		if(player.playerInviteInfo().isReceived(id)){
			//领取过
			player.sendErrorMsg(SysConstant.InviteCode_Received);
			return;
		}
		if(!inviteBiz.finish(player,id)){
			//未完成
			return;
		}
		player.playerInviteInfo().receive(id);
		InviteTemplate template = inviteConfig.getInviteTask(id);
		List<Items> rewards = template.getRewards();
		itemBiz.addItem(player, rewards, LogType.InviteReward);
		player.notifyObservers(ObservableEnum.ChInviteReceive,id);

		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Invite_Receive, rewards);
	}

	@MsgMethod(MessageComm.C2S_Invite_DayReceive)
	public void receiveDay(Player player, JsonMsg msg) {
		int id = msg.getInt("id");
		if(player.playerInviteInfo().isDayReceived(id)){
			//领取过
			player.sendErrorMsg(SysConstant.InviteCode_Received);
			return;
		}
		if(!inviteBiz.finish(player,id)){
			//未完成
			return;
		}
		player.playerInviteInfo().receiveDay(id);
		InviteTemplate template = inviteConfig.getInviteTask(id);
		List<Items> rewards = template.getRewards();
		itemBiz.addItem(player, rewards, LogType.InviteReward);
		player.notifyObservers(ObservableEnum.ChInviteReceive,id);

		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Invite_DayReceive, rewards);
	}
	
	@MsgMethod(MessageComm.C2S_Invite_Receive_BeInvited)
    public void beInvitedReceive(Player player, JsonMsg msg) {
		if(player.playerInviteInfo().getInviteId()<=0){
			return;
		}
		if(player.playerInviteInfo().isBeInviteReceive()){
			//领取过
			player.sendErrorMsg(SysConstant.InviteCode_Received);
			return;
		}
		List<Items> rewards = commValueConfig.getListItem(CommonValueType.BeInvitedDayReward);
		itemBiz.addItem(player, rewards, LogType.InviteReward);
		player.playerInviteInfo().receiveBeInvitedReward();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_Invite_Receive_BeInvited, rewards);
	}
	
	@MsgMethod(MessageComm.C2S_Invite_Open)
    public void open(Player player, JsonMsg msg) {
		InviteInfoData inviteInfoData = RedisUtil.getInviteInfoData(player.getId());
		if (inviteInfoData != null) {
			Set<String> dateStr = InviteInfoData.getDateStr(player);
			inviteInfoData.getBeInvites().removeIf(e-> e.getDate() == null || !CollUtil.contains(dateStr, e.getDate()));
		}
		player.sendMsg(MessageComm.S2C_Invite_Open, inviteInfoData);
	}

	@MsgMethod(MessageComm.C2S_Invite_ShareReward)
	public void shareReward(Player player, JsonMsg msg) {
		int shareNum = player.playerInviteInfo().getShareNum();
		int shareReward = player.playerInviteInfo().getShareReward();

		if(shareNum <= 0) {
			//判断分享
			player.sendErrorMsg(SysConstant.ShareReward_Not);
			return;
		}
		player.playerInviteInfo().setShareReward(shareReward+1);
		if(shareReward > 0) {
			int cd = commValueConfig.getCommValue(CommonValueType.ShareDayCD);
			player.playerInviteInfo().setNextShareTime(System.currentTimeMillis()+ GameConstants.SECOND*cd);
		}
		player.playerInviteInfo().setShareNum(0);

		int dropId = commValueConfig.getCommValue(CommonValueType.ShareReward);
		List<Items> itemsList = dropConfig.randomItem(dropId);
		itemBiz.addItem(player,itemsList,LogType.SHARE_REWARD);
		player.sendUserUpdateMsg();

		JsonMsg retMsg = JsonMsg.create(MessageComm.S2C_Invite_ShareReward);
		retMsg.addProperty("itemList",itemsList);
		player.sendMsg(retMsg);
	}
}
