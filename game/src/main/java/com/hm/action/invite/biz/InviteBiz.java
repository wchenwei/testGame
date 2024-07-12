package com.hm.action.invite.biz;


import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.config.InviteConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.RechargeConfig;
import com.hm.config.excel.templaextra.InviteTemplate;
import com.hm.config.excel.templaextra.RechargeGiftTempImpl;
import com.hm.enums.CommonValueType;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerInviteInfo;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.InviteInfoData;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.SimpleInvitePlayer;
import com.hm.redis.util.RedisUtil;
import com.hm.util.InviteCodeGenerator;

import javax.annotation.Resource;
import java.util.List;

@Biz
public class InviteBiz implements IObserver{
	@Resource
	private RechargeConfig rechargeConfig;
	@Resource
	private InviteConfig inviteConfig;
	@Resource
	private CommValueConfig commValueConfig;
	
	public void checkInviteCode(Player player){
		if(StrUtil.isBlank(player.getInviteCode())){
			player.playerFix().setInviteCode(InviteCodeGenerator.generateInviteCode());
			player.playerInviteInfo().setCal(true);
			RedisUtil.updateRedisInviteCode(player);
			return;
		}
		if(!player.playerInviteInfo().isCal()&&player.playerInviteInfo().haveChild()){
			List<SimpleInvitePlayer> invites = Lists.newArrayList();
			player.playerInviteInfo().getMyInvites().forEach((k,v)->{
				invites.add(new SimpleInvitePlayer(k, v));
			});
			//将玩家的邀请数据同步到redis
			InviteInfoData data = new InviteInfoData(player.getId(),invites,player.playerInviteInfo().getInviteRechargeGold());
			RedisUtil.updateInviteInfo(player.getId(), data);
			player.playerInviteInfo().setCal(true);
		}
	}
	
	public void bind(Player player, Player beBindPlayer) {
		player.playerInviteInfo().bind(beBindPlayer.getId(),beBindPlayer.getName());
		beBindPlayer.playerInviteInfo().beBind(player);
	}
	
	public void bind(Player player, long beBindId,String name) {
		player.playerInviteInfo().bind(beBindId,name);
		InviteInfoData inviteInfoData = RedisUtil.getInviteInfoData(beBindId);
		if(inviteInfoData==null){
			inviteInfoData = new InviteInfoData(player);
		}else{
			inviteInfoData.addBeInvite(player);
		}
		//被绑定的玩家更新绑定数据
		RedisUtil.updateInviteInfo(beBindId, inviteInfoData);
	}

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelChange, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankAdd, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ShareWx, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		switch(observableEnum){
			case PlayerLevelChange:
			case TankAdd:
				updateBeInviteInfo(player);
				break;
			case Recharge:
				beInviteRecharge(player,argv);
				break;
			case ShareWx:
				doShareWx(player,argv);
				break;
		}
	}

	private void beInviteRecharge(Player player, Object[] argv) {
		long inviteId = player.playerInviteInfo().getInviteId();
		if(inviteId>0){
			int giftId = Integer.parseInt(argv[3].toString());
			RechargeGiftTempImpl rechargeTemplate = rechargeConfig.getRechargeGift(giftId);
			if(rechargeTemplate != null && rechargeTemplate.getDiamonds() > 0) {
				PlayerRedisData beBindPlayer = RedisUtil.getPlayerRedisData(inviteId);
				if(beBindPlayer != null) {
					InviteInfoData inviteInfoData = RedisUtil.getInviteInfoData(beBindPlayer.getId());
					inviteInfoData.addRecharge(rechargeTemplate.getDiamonds());
					RedisUtil.updateInviteInfo(beBindPlayer.getId(), inviteInfoData);
				}
			}
		}
	}

	private void updateBeInviteInfo(Player player) {
		long inviteId = player.playerInviteInfo().getInviteId();
		if(inviteId>0){
			PlayerRedisData beBindPlayer = RedisUtil.getPlayerRedisData(inviteId);
			if(beBindPlayer != null) {
				InviteInfoData inviteInfoData = RedisUtil.getInviteInfoData(beBindPlayer.getId());
				if(inviteInfoData==null){//邀请人已经删除
					return;
				}
				SimpleInvitePlayer simpleInvitePlayer = inviteInfoData.getBeInviteInfo(player.getId());
				if(simpleInvitePlayer!=null){
					simpleInvitePlayer.updatePlayerInfo(player);
					RedisUtil.updateInviteInfo(beBindPlayer.getId(), inviteInfoData);
				}
			}
		}
	}
	public boolean finish(Player player,int id) {
		InviteTemplate template = inviteConfig.getInviteTask(id);
		if(template==null){
			return false;
		}
		return template.finish(player);
	}


	public void doShareWx(Player player, Object[] argv) {
		int type = (int)argv[0];

		if(System.currentTimeMillis() < player.playerInviteInfo().getNextShareTime()
			|| player.playerInviteInfo().getShareNum() > 0) {
			return;//不加次数
		}
		int maxCount = commValueConfig.getCommValue(CommonValueType.ShareDayMax);
		PlayerInviteInfo playerInviteInfo = player.playerInviteInfo();
		if(playerInviteInfo.getShareReward() >= maxCount) {
			return;
		}
		player.playerInviteInfo().setShareNum(1);
	}
}
