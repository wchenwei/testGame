package com.hm.model.guild.bean;

import com.hm.container.PlayerContainer;
import com.hm.enums.UserSetTypeEnum;
import com.hm.model.player.Player;
import com.hm.model.player.PlayerBaseInfo;
import com.hm.redis.PlayerRedisData;
import com.hm.redis.util.RedisUtil;
import com.hm.util.bluevip.QQBlueVip;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

//用于发送app的部落成员信息
@Data
@NoArgsConstructor
public class GuildPlayerVo {
	private long playerId;	//用户id
	private int guildJob;	//官职
	private String name;	//用户名字
	private int lv;			//等级
	private long combat;	//战力
	private long contr;		//部落贡献
	private long dayContr;	//当天的新增的部落贡献值
	private long lastOffLineDate;
	private int isOnline;
	private String icon;
	private int frameIcon;//当前头像框

	private int carLv;//车长等级
	private int mlv;//军衔等级
	private int[] equQuality;//装备品质

	private int viplv;	//vip等级
	public int noVip;
	private int part;//当天配件贡献值
	private int armsStrengthCount;//当天武器强化次数
    private QQBlueVip blueVip;
	
	public GuildPlayerVo(GuildPlayer guildP) {
		this.playerId = guildP.getPlayerId();
		this.guildJob = guildP.getGuildJob();
		this.contr = guildP.getContr();
		this.dayContr= guildP.getDayContr();
		this.part =guildP.getPart();
		this.armsStrengthCount =guildP.getArmsStrengthCount();
		boolean isPlayerOnline = PlayerContainer.isOnline(this.playerId);
		this.isOnline = isPlayerOnline?1:0;
		if (isPlayerOnline) {
			Player player = PlayerContainer.getPlayer(this.playerId);
			PlayerBaseInfo playerBaseInfo = player.playerBaseInfo();
			long lastLogin = playerBaseInfo.getLastLoginDate().getTime();
			Date lastOffLineDate = playerBaseInfo.getLastOffLineDate();
			long lastOff = lastOffLineDate != null ? lastOffLineDate.getTime() : lastLogin;
			//处理用户非正常退出时的时间显示问题
			this.lastOffLineDate = (lastLogin>lastOff?lastLogin:lastOff)/1000;
			this.icon = player.playerHead().getIcon();
			this.frameIcon = player.playerHead().getFrameIcon();
			this.name = player.getName();
			this.lv = player.playerLevel().getLv();
			this.combat = player.getCombat();
			this.viplv = player.getPlayerVipInfo().getVipLv();
			this.noVip =  player.playerSet().getState(UserSetTypeEnum.NoShowVip);
			this.blueVip = player.playerBlueVip().getBlueVip();
			this.carLv = player.playerCommander().getCarLv();
			this.mlv = player.playerCommander().getMilitaryLv();
			this.equQuality = player.playerEquip().getEquQuality();
		} else {
			PlayerRedisData playerVo = RedisUtil.getPlayerRedisData(guildP.getPlayerId());
			long lastLogin = playerVo.getLastLoginDate();
			long lastOff = playerVo.getLastOffLineDate();
			//处理用户非正常退出时的时间显示问题
			this.lastOffLineDate = (lastLogin>lastOff?lastLogin:lastOff)/1000;
			this.icon = playerVo.getIcon();
			this.frameIcon = playerVo.getFrameIcon();
			this.name = playerVo.getName();
			this.lv = playerVo.getLv();
			this.combat = playerVo.getCombat();
			this.viplv = playerVo.getVipLv();
			this.carLv = playerVo.getCarLv();
			this.mlv = playerVo.getMlv();
			this.equQuality = playerVo.getEquQuality();
		}
	}


	public static GuildPlayerVo buildVo(GuildPlayer guildP){
		return new GuildPlayerVo(guildP);
	}

}




