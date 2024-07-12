package com.hm.action.http;


import com.hm.libcore.util.TimeUtils;
import com.hm.enums.PlayerStatusType;
import com.hm.enums.StatisticsType;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import lombok.Data;
@Data
public class SimplePlayerInfo {
	private long id;
	private String name;
	private int channelId;
	private long gold;//充值金
	private long sysGold;//系统金
	private long crystal;//钞票
	private long uid;
	private int lv;//等级
	private long exp;//经验
	private int warLv;//军衔
	private int warCon;
	private int vipLv;//vip等级
	private long vipExp;//vip经验
	private long combat;//战力
	private int energy;//体力
	private long loginTime;//上次登录时间
	private long firstTime;//注册时间
	private int state;//状态 0-离线 1-在线
	private long recharge;//充值 
	private int serverRank;//全服排行
	private int arenaRank;//竞技排行
	private String lastLoginTime;
	private String registeTime;
	private int chatState;
	private String chatStartTime;//禁言开始时间
	private String chatEndTime;//禁言结束时间
	private int loginDays;//累计登录天数
	private String lastIp;//上次登录ip
	private String imei;//最近登录设备
	private String lastPhone;//最近登录设备型号
	private int onlineTime;//当天在线分钟数
	private int fbId;//副本进度
	private long monsterCount;//打野次数
	private int areaId;//区域id
	private int channel;//渠道
	private int techLv;//科技中心等级
	private int blackState;
	private String blackStartTime;//关小黑屋开始时间
	private String blackEndTime;//关小黑屋结束时间
	private String idCode;//唯一码
	public SimplePlayerInfo() {}

	public SimplePlayerInfo(Player player) {
		this.id = player.getId();
		this.name = player.getName();
		this.channelId = player.getChannelId();
		this.gold = player.playerCurrency().get(CurrencyKind.Gold);
		this.sysGold = player.playerCurrency().get(CurrencyKind.SysGold);
		this.crystal= player.playerCurrency().get(CurrencyKind.Cash);
		this.uid = player.getUid();
		this.lv=player.playerCommander().getMilitaryLv();
		this.exp=player.playerLevel().getLvExp();
		this.vipLv=player.getPlayerVipInfo().getVipLv();
		this.vipExp = player.getPlayerVipInfo().getVipExp();
		this.combat=player.getCombat();
		this.state = player.isOnline()?1:0;
		this.recharge = (int) player.getPlayerStatistics().getLifeStatistics(StatisticsType.RECHARGE);
		this.loginDays = (int) player.getPlayerStatistics().getLifeStatistics(StatisticsType.LOGIN_DAYS);
		this.lastIp = player.getIp();
		this.imei = player.playerBaseInfo().getImei();
		this.lastPhone = player.playerBaseInfo().getLastPohone();
		this.onlineTime = (int) player.getPlayerStatistics().getTodayStatistics(StatisticsType.ONLINE_TIME);
		this.channel = player.getChannelId();
		this.idCode = player.getIdCode();
		try {
			this.loginTime=player.playerBaseInfo().getLastLoginDate().getTime();
			this.firstTime=player.playerBaseInfo().getCreateDate().getTime();
			this.registeTime = TimeUtils.ToString(player.playerBaseInfo().getCreateDate());
			this.lastLoginTime = TimeUtils.ToString(player.playerBaseInfo().getLastLoginDate());
			this.chatState = player.playerStatus().isStatus(PlayerStatusType.NotChat)?1:0;
			this.chatStartTime =TimeUtils.getTimeString(player.playerStatus().getStartTime(PlayerStatusType.NotChat));
			this.chatEndTime = TimeUtils.getTimeString(player.playerStatus().getEndTime(PlayerStatusType.NotChat));
			this.blackState = player.playerStatus().isStatus(PlayerStatusType.BlackHome)?1:0;
			this.blackStartTime =TimeUtils.getTimeString(player.playerStatus().getStartTime(PlayerStatusType.BlackHome));
			this.blackEndTime = TimeUtils.getTimeString(player.playerStatus().getEndTime(PlayerStatusType.BlackHome));
		} catch (Exception e) {
		}
	}

	
	
}	
