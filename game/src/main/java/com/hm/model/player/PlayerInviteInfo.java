package com.hm.model.player;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.msg.JsonMsg;
import com.hm.db.PlayerUtils;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class PlayerInviteInfo extends PlayerDataContext {
	private long inviteId;//邀请我的玩家id
	private String inviteName;//邀请人名字
	private ConcurrentHashMap<Long, Integer> myInvites = new ConcurrentHashMap<>();
	private List<Integer> inviteReceives = Lists.newArrayList();//已经领取的邀请奖励
	private List<Integer> dayInviteReceives = Lists.newArrayList();//已经领取的每日邀请奖励
	/**
	 * 重置时间loop
	 */
	private transient int loopCnt;
	//被邀请奖励是否已领取
	private boolean beInviteReceive;
	//邀请的玩家充值的总金砖数
	private long inviteRechargeGold;
	
	private boolean cal;//同步标识 false-未同步 true-已同步

	//今日分享次数
	private int shareNum;//分享次数
	private int shareReward;//分享奖励次数
	private long nextShareTime;//分享时间

	//达到多少级的有多少人
	public int getLvNum(int lv){
		return myInvites.values().stream().filter(t ->t>=lv).collect(Collectors.toList()).size();
	}
	
	public int getInviteNum() {
		return myInvites.size();
	}
	public void bind(long playerId,String name){
		this.inviteId = playerId;
		this.inviteName = name;
		SetChanged();
	}
	public void beBind(Player player){
		this.myInvites.put(player.getId(), player.playerLevel().getLv());
		SetChanged();
	}
	
	public void inviteRecharge(long gold){
		this.inviteRechargeGold+=gold;
		SetChanged();
	}
	
	public void resetDay(Player player){
		this.beInviteReceive=false;

		int n = (int) (DateUtil.betweenDay(player.playerBaseInfo().getCreateDate(), new Date(), true) / 3);
		if (n != loopCnt || loopCnt == 0) {
			loopCnt = n;
			this.dayInviteReceives.clear();
		}

		this.shareNum = 1;
		this.shareReward = 0;
		this.nextShareTime = 0;
		SetChanged();
	}
	public boolean isReceived(int id) {
		if(inviteReceives.contains(id)){
			return true;
		}
		return false;
	}
	public boolean isDayReceived(int id) {
		if(dayInviteReceives.contains(id)){
			return true;
		}
		return false;
	}
	public void receive(int id) {
		this.inviteReceives.add(id);
		SetChanged();
	}
	public void receiveDay(int id) {
		this.dayInviteReceives.add(id);
		SetChanged();
	}
	public void receiveBeInvitedReward() {
		this.beInviteReceive = true;
		SetChanged();
	}
	private void loadInviteName() {
		if(this.inviteId!=0&&StrUtil.isBlank(this.inviteName)){
			Player invitePlayer = PlayerUtils.getPlayer(this.inviteId);
			if(invitePlayer!=null){
				this.inviteName = invitePlayer.getName();
			}
		}		
	}
	
	@Override
    protected void fillMsg(JsonMsg msg) {
		loadInviteName();
        msg.addProperty("PlayerInviteInfo", this);
    }
	public void setCal(boolean flag) {
		this.cal = flag;
		SetChanged();
	}
	
	public boolean haveChild(){
		return myInvites.size()>0;
	}


	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
		SetChanged();
	}

	public void setNextShareTime(long nextShareTime) {
		this.nextShareTime = nextShareTime;
		SetChanged();
	}

	public void setShareReward(int shareReward) {
		this.shareReward = shareReward;
		SetChanged();
	}
}
