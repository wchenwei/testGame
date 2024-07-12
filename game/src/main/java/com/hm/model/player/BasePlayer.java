package com.hm.model.player;


import com.google.common.collect.Lists;
import com.hm.db.PlayerUtils;
import com.hm.db.dbmonitor.DbMonotor;
import com.hm.enums.BuildQueueType;
import com.hm.enums.QueueType;
import com.hm.libcore.msg.JsonMsg;
import com.hm.message.MessageComm;
import com.hm.model.giftpush.PlayerGiftPush;
import com.hm.model.queue.AbstractQueue;
import com.hm.model.queue.BuildUpQueue;
import com.hm.observer.ObservableEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BasePlayer extends PlayerSession{
	@Getter@Setter
	private long uid;//uid:登录服务器的唯一Id 不是玩家的id!
	private int channelId;//渠道id
	private int createServerId;//创建时的serverId
	@Indexed
	private String name;//玩家昵称
	@Indexed
	private String idCode;//唯一码
	/**
	 * 用户过期时间、合服清理用户用
	 */
	@Indexed
	private transient long expireTime;
	//========================================
	//玩家基础信息
	protected PlayerBaseInfo playerBaseInfo;
	protected PlayerLevel playerLevel;//玩家等级相关信息
	protected PlayerCurrency playerCurrency;//玩家资产信息
	protected PlayerVipInfo playerVipInfo;//vip信息
	protected PlayerBag playerBag;//玩家背包数据
	protected PlayerTankPaper playerTankPaper;//玩家的，战车图纸
	protected PlayerTankpiece playerTankpiece;//战车装备碎片
	protected PlayerEquip playerEquip;//指挥官装备
	
	protected PlayerStatus playerStatus;//玩家状态信息 禁言 封号
	protected PlayerMail playerMail; //玩家邮件
	protected PlayerRecharge playerRecharge; //玩家充值信息
	protected PlayerStatistics playerStatistics;//玩家统计数据
	protected PlayerCDs playerCDs;//玩家cd
	protected PlayerChat playerChat; //聊天设置
	protected PlayerFunction playerFunction;//玩家功能模块开启
	protected PlayerDynamicData playerDynamicData;//玩家动态数据存储
	protected PlayerMainTask playerMainTask;//玩家任务
	protected PlayerTask playerTask;//玩家任务
	protected PlayerDailyTask playerDailyTask;//玩家每日任务
	protected PlayerActivity playerActivity;//活动数据
	protected PlayerVoucher playerVoucher;//兑换码
	protected PlayerMission playerMission;//玩家关卡
	protected PlayerTreasury playerTreasury;//府库
	protected PlayerTank playerTank;//坦克
    protected PlayerResearchTank playerResearchTank;//坦克研发
    protected PlayerTankFactory playerTankFactory;//tank工厂
	protected PlayerHead playerHead;//头像相关
	protected PlayerTroops playerTroops;//玩家部队
	protected PlayerShop playerShop;//玩家商店模块
	protected PlayerGuild playerGuild;//玩家部落信息
	protected PlayerBattle playerBattle;//玩家战役
	protected PlayerExpedition playerExpedition;//燃烧的远征
	protected PlayerCommander playerCommander; //指挥官
	protected PlayerGuide playerGuide;//新手引导
	protected PlayerHonor playerHonor;//玩家功勋
	protected PlayerShipTrade playerShipTrade;
	protected PlayerTradeStock playerTradeStock; //贸易股东
	protected PlayerRobSupply playerRobSupply; //玩家补给掠夺信息
	protected PlayerTitle playerTitle;//玩家称号
	protected PlayerStone playerStone;//玩家宝石箱
	protected PlayerLevelEvent playerLevelEvent;//玩家等级事件
	protected PlayerOverallWar playerOverallWar;//全面战争
	protected PlayerBuffer playerBuffer;//玩家buff信息
	protected PlayerCustomTanks playerCustomTanks;//玩家自定义坦克阵容
	protected PlayerRandomTask playerRandomTask;//地图民情
	protected PlayerInviteInfo playerInviteInfo;//邀请信息
	protected PlayerGiftPack playerGiftPack;//玩家礼包信息
	protected PlayerResBack playerResBack;//资源找回模块
	protected PlayerMastery playerMastery;//专精
	protected PlayerAgent playerAgent;//特工
	protected PlayerCloneTroops playerCloneTroops;//玩家克隆部队数据
	protected PlayerFight playerFight;//玩家Pve战斗数据
	protected PlayerPresidentData playerPresidentData;//玩家大总统数据
	protected PlayerPassenger playerPassenger;//乘员
	protected PlayerMilitaryLineup playerMilitaryLineup;//军阵
	protected PlayerWorldBuildData playerWorldBuildData;//军阵
	protected PlayerMysteryShop playerMysteryShop;//神秘商店
	protected PlayerActivityTask playerActivityTask;//活动任务
	protected PlayerQueue playerQueue;//队列
	protected PlayerBuild playerBuild;//建筑
	protected PlayerMemorialHall playerMemorialHall;//纪念馆
	protected PlayerTrain playerTrain;
	protected PlayerRepairTrain playerRepairTrain;
	protected PlayerCaptive playerCaptive;//玩家战俘
    protected PlayerFriend playerFriend;//玩家私聊信息

	protected PlayerEvent playerEvent;//玩家事件
	protected PlayerKfTask playerKfTask;//玩家跨服任务数据

	//=======================================
	protected PlayerFix playerFix;//玩家相对固定的信息
	protected PlayerSet playerSet;//玩家设置
	protected PlayerWarcraft playerWarcraft;// 玩家的兵法
	protected PlayerCityWarSkill playerCityWarSkill;// 玩家的兵法

	protected PlayerAircraft playerAircraft;//飞机
	protected PlayerAircraftCarrier playerAircraftCarrier;//航母 (飞机的载体)
	protected PlayerElement playerElement;//元件

	protected PlayerWarMakes playerWarMakes; // 战令

    protected PlayerBlueVip playerBlueVip; // QQ游戏 蓝钻信息
    protected PlayerGame playerGame;//小游戏

	protected PlayerGiftPush playerGiftPush;
	protected PlayerAds playerAds;
	protected PlayerRecord playerRecord;
	protected PlayerShare playerShare;
	protected PlayerFish playerFish;// 钓鱼玩法
	protected PlayerStrength playerStrength;// 力量系统
	protected PlayerTips playerTips;
	protected PlayerArms playerArms;//武器（部落神兽）
	protected PlayerMissionBox playerMissionBox = new PlayerMissionBox();;
	protected PlayerTankMaster playerTankMaster;// 图鉴
	protected PlayerPaperResearch playerPaperResearch;// 驯化
	protected PlayerFieldBoss playerFieldBoss;


	public transient PlayerVideoOrder playerVideoOrder;
	@Transient
	public transient long lastDBTime;//上次存储时间
	@Transient
	public transient AtomicBoolean isDBChangeMark = new AtomicBoolean(false);


    public void setUidForCreate(long uid,int createServerId) {
		this.uid = uid;
		this.createServerId = createServerId;
		changeDBChangeMark();
	}

	public void changeName(String name) {
		this.name = name;
		changeDBChangeMark();
	}
	public String getName(){
		return this.name; 
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
		changeDBChangeMark();
	}

	public int getCreateServerId() {
		return createServerId;
	}

	public void setCreateServerId(int createServerId) {
		this.createServerId = createServerId;
		changeDBChangeMark();
	}

	public String getIp() {
		return playerFix().getIp();
	}

	public String getInviteCode() {
		return playerFix().getInviteCode();
	}

	public String getIdCode() {
		return idCode;
	}
	public void setIdCode(String idCode) {
		this.idCode = idCode;
		changeDBChangeMark();
	}
	public void unBindIdCode() {
		this.idCode ="";
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	public PlayerLevel playerLevel() {
		if(this.playerLevel==null) this.playerLevel=new PlayerLevel();
		this.playerLevel.LateInit(this);
		return this.playerLevel;
	}
	public PlayerBaseInfo playerBaseInfo() {
		if(this.playerBaseInfo==null) this.playerBaseInfo=new PlayerBaseInfo();
		this.playerBaseInfo.LateInit(this);
		return this.playerBaseInfo;
	}
	public PlayerCurrency playerCurrency() {
		if(this.playerCurrency==null) this.playerCurrency=new PlayerCurrency();
		this.playerCurrency.LateInit(this);
		return this.playerCurrency;
	}
	
	public PlayerBag playerBag(){
		if(this.playerBag==null) this.playerBag=new PlayerBag();
		this.playerBag.LateInit(this);
		return this.playerBag;
	}
	public PlayerMail playerMail(){
		if(this.playerMail==null) this.playerMail=new PlayerMail();
		this.playerMail.LateInit(this);
		return this.playerMail;
	}
	
	
	public PlayerChat playerChat(){
		if(this.playerChat==null) this.playerChat=new PlayerChat();
		this.playerChat.LateInit(this);
		return this.playerChat;
	}
	
	public PlayerTask playerTask(){
		if(this.playerTask==null) this.playerTask=new PlayerTask();
		this.playerTask.LateInit(this);
		return this.playerTask;
	}
	public PlayerMainTask playerMainTask() {
		if (this.playerMainTask == null) this.playerMainTask = new PlayerMainTask();
		this.playerMainTask.LateInit(this);
		return this.playerMainTask;
	}

	public PlayerVipInfo getPlayerVipInfo() {
		if(null == playerVipInfo) {
			this.playerVipInfo = new PlayerVipInfo();
		}
		this.playerVipInfo.LateInit(this);
		return this.playerVipInfo;
	}
	public PlayerFunction getPlayerFunction(){
		if(this.playerFunction==null) this.playerFunction=new PlayerFunction();
		this.playerFunction.LateInit(this);
		return this.playerFunction;
	}
	public PlayerStatus playerStatus(){
		if(this.playerStatus==null) this.playerStatus=new PlayerStatus();
		this.playerStatus.LateInit(this);
		return this.playerStatus;
	}
	public PlayerStatistics getPlayerStatistics() {
		if(null ==  this.playerStatistics) {
			this.playerStatistics = new PlayerStatistics();
		}
		this.playerStatistics.LateInit(this);
		return playerStatistics;
	}
	
	public PlayerDynamicData getPlayerDynamicData() {
		if(null == playerDynamicData) playerDynamicData =new PlayerDynamicData();
		this.playerDynamicData.LateInit(this);
		return playerDynamicData;
	}
	
	public PlayerRecharge getPlayerRecharge() {
		if(null == playerRecharge) playerRecharge =new PlayerRecharge();
		this.playerRecharge.LateInit(this);
		return playerRecharge;
	}
	
	public PlayerCDs getPlayerCDs() {
		if(null == playerCDs) playerCDs =new PlayerCDs();
		this.playerCDs.LateInit(this);
		return playerCDs;
	}
	public PlayerActivity getPlayerActivity() {
		if(null == playerActivity) playerActivity =new PlayerActivity();
		this.playerActivity.LateInit(this);
		return playerActivity;
	}
	public PlayerVoucher playerVoucher() {
		if(null == playerVoucher) playerVoucher =new PlayerVoucher();
		this.playerVoucher.LateInit(this);
		return playerVoucher;
	}
	public PlayerSet playerSet() {
		if(null == playerSet) playerSet =new PlayerSet();
		this.playerSet.LateInit(this);
		return playerSet;
	}

	public PlayerFix playerFix() {
		if(null == playerFix) playerFix =new PlayerFix();
		this.playerFix.LateInit(this);
		return playerFix;
	}
	
	public PlayerDailyTask playerDailyTask() {
		if (null == playerDailyTask) playerDailyTask = new PlayerDailyTask();
		this.playerDailyTask.LateInit(this);
		return playerDailyTask;
	}
	
	public PlayerWarcraft playerWarcraft() {
		if (null == playerWarcraft) playerWarcraft = new PlayerWarcraft();
		this.playerWarcraft.LateInit(this);
		return playerWarcraft;
	}

	
	public PlayerMission playerMission(){
		if(this.playerMission==null) this.playerMission=new PlayerMission();
		this.playerMission.LateInit(this);
		return this.playerMission;
	}
	
	public PlayerTreasury playerTreasury(){
		if(this.playerTreasury==null) this.playerTreasury=new PlayerTreasury();
		this.playerTreasury.LateInit(this);
		return this.playerTreasury;
	}
	
	public PlayerTank playerTank(){
		if(this.playerTank==null) this.playerTank=new PlayerTank();
		this.playerTank.LateInit(this);
		return this.playerTank;
	}
	
	public PlayerHead playerHead(){
		if(this.playerHead==null) this.playerHead=new PlayerHead();
		this.playerHead.LateInit(this);
		return this.playerHead;
	}
	public PlayerTankpiece playerTankpiece(){
		if(this.playerTankpiece==null) this.playerTankpiece=new PlayerTankpiece();
		this.playerTankpiece.LateInit(this);
		return this.playerTankpiece;
	}
	public PlayerTankPaper playerTankPaper(){
		if(this.playerTankPaper==null) this.playerTankPaper=new PlayerTankPaper();
		this.playerTankPaper.LateInit(this);
		return this.playerTankPaper;
	}
	public PlayerEquip playerEquip(){
		if(this.playerEquip==null) this.playerEquip=new PlayerEquip();
		this.playerEquip.LateInit(this);
		return this.playerEquip;
	}
    public PlayerTankFactory playerTankFactory() {
        if (this.playerTankFactory == null) {
            this.playerTankFactory = new PlayerTankFactory();
        }
        this.playerTankFactory.LateInit(this);
        return this.playerTankFactory;
    }

    public PlayerResearchTank playerResearchTank() {
        if (this.playerResearchTank == null) {
            this.playerResearchTank = new PlayerResearchTank();
        }
        this.playerResearchTank.LateInit(this);
        return this.playerResearchTank;
    }

	public PlayerTroops playerTroops(){
		if(this.playerTroops==null) this.playerTroops=new PlayerTroops();
		this.playerTroops.LateInit(this);
		return this.playerTroops;
	}
	public PlayerRobSupply playerRobSupply(){
		if(this.playerRobSupply==null) this.playerRobSupply=new PlayerRobSupply();
		this.playerRobSupply.LateInit(this);
		return this.playerRobSupply;
	}
	
	public PlayerShop playerShop(){
		if(this.playerShop==null) this.playerShop=new PlayerShop();
		this.playerShop.LateInit(this);
		return this.playerShop;
	}
	
	public PlayerGuild playerGuild(){
		if(this.playerGuild==null) this.playerGuild=new PlayerGuild();
		this.playerGuild.LateInit(this);
		return this.playerGuild;
	}
	
	public PlayerBattle playerBattle(){
		if(this.playerBattle==null) this.playerBattle=new PlayerBattle();
		this.playerBattle.LateInit(this);
		return this.playerBattle;
	}
	public PlayerExpedition playerExpedition(){
		if(this.playerExpedition==null) this.playerExpedition=new PlayerExpedition();
		this.playerExpedition.LateInit(this);
		return this.playerExpedition;
	}
	
	public PlayerCommander playerCommander() {
		if(this.playerCommander==null) this.playerCommander=new PlayerCommander();
		this.playerCommander.LateInit(this);
		return this.playerCommander;
	}
	
	
	public PlayerGuide playerGuide() {
		if(this.playerGuide==null) this.playerGuide=new PlayerGuide();
		this.playerGuide.LateInit(this);
		return this.playerGuide;
	}
	public PlayerHonor playerHonor() {
		if(this.playerHonor==null) this.playerHonor=new PlayerHonor();
		this.playerHonor.LateInit(this);
		return this.playerHonor;
	}

	public PlayerShipTrade playerShipTrade() {
		if (this.playerShipTrade == null) this.playerShipTrade = new PlayerShipTrade();
		this.playerShipTrade.LateInit(this);
		return this.playerShipTrade;
	}
	public PlayerTradeStock playerTradeStock() {
		if (this.playerTradeStock == null) this.playerTradeStock = new PlayerTradeStock();
		this.playerTradeStock.LateInit(this);
		return this.playerTradeStock;
	}

	public PlayerLevelEvent playerLevelEvent() {
		if(this.playerLevelEvent==null) this.playerLevelEvent=new PlayerLevelEvent();
		this.playerLevelEvent.LateInit(this);
		return this.playerLevelEvent;
	}
	public PlayerTitle playerTitle() {
		if(this.playerTitle==null) this.playerTitle=new PlayerTitle();
		this.playerTitle.LateInit(this);
		return this.playerTitle;
	}
	public PlayerStone playerStone() {
		if(this.playerStone==null) this.playerStone=new PlayerStone();
		this.playerStone.LateInit(this);
		return this.playerStone;
	}
	public PlayerOverallWar playerOverallWar() {
		if(this.playerOverallWar==null) this.playerOverallWar=new PlayerOverallWar();
		this.playerOverallWar.LateInit(this);
		return this.playerOverallWar;
	}
	public PlayerBuffer playerBuffer(){
		if(this.playerBuffer==null) this.playerBuffer=new PlayerBuffer();
		this.playerBuffer.LateInit(this);
		return this.playerBuffer;
	}
	public PlayerCustomTanks playerCustomTanks(){
		if(this.playerCustomTanks==null) this.playerCustomTanks=new PlayerCustomTanks();
		this.playerCustomTanks.LateInit(this);
		return this.playerCustomTanks;
	}
	public PlayerRandomTask playerRandomTask(){
		if(this.playerRandomTask==null) this.playerRandomTask=new PlayerRandomTask();
		this.playerRandomTask.LateInit(this);
		return this.playerRandomTask;
	}
	public PlayerInviteInfo playerInviteInfo(){
		if(this.playerInviteInfo==null) this.playerInviteInfo=new PlayerInviteInfo();
		this.playerInviteInfo.LateInit(this);
		return this.playerInviteInfo;
	}
	//用户的礼包信息
	public PlayerGiftPack playerGiftPack(){
		if(this.playerGiftPack==null) this.playerGiftPack=new PlayerGiftPack();
		this.playerGiftPack.LateInit(this);
		return this.playerGiftPack;
	}
	public PlayerResBack playerResBack(){
		if(this.playerResBack==null) this.playerResBack=new PlayerResBack();
		this.playerResBack.LateInit(this);
		return this.playerResBack;
	}
	public PlayerMastery playerMastery(){
		if(this.playerMastery==null) this.playerMastery=new PlayerMastery();
		this.playerMastery.LateInit(this);
		return this.playerMastery;
	}
	public PlayerAgent playerAgent(){
		if(this.playerAgent==null) this.playerAgent=new PlayerAgent();
		this.playerAgent.LateInit(this);
		return this.playerAgent;
	}
	public PlayerCloneTroops playerCloneTroops(){
		if(this.playerCloneTroops==null) this.playerCloneTroops=new PlayerCloneTroops();
		this.playerCloneTroops.LateInit(this);
		return this.playerCloneTroops;
	}
	
	public PlayerFight playerFight(){
		if(this.playerFight==null) this.playerFight=new PlayerFight();
		this.playerFight.LateInit(this);
		return this.playerFight;
	}
	public PlayerPresidentData playerPresidentData(){
		if(this.playerPresidentData==null) this.playerPresidentData=new PlayerPresidentData();
		this.playerPresidentData.LateInit(this);
		return this.playerPresidentData;
	}
	public PlayerPassenger playerPassenger(){
		if(this.playerPassenger==null) this.playerPassenger=new PlayerPassenger();
		this.playerPassenger.LateInit(this);
		return this.playerPassenger;
	}
	public PlayerMilitaryLineup playerMilitaryLineup(){
		if(this.playerMilitaryLineup==null) this.playerMilitaryLineup=new PlayerMilitaryLineup();
		this.playerMilitaryLineup.LateInit(this);
		return this.playerMilitaryLineup;
	}
	public PlayerWorldBuildData playerWorldBuildData(){
		if(this.playerWorldBuildData==null) this.playerWorldBuildData=new PlayerWorldBuildData();
		this.playerWorldBuildData.LateInit(this);
		return this.playerWorldBuildData;
	}
	public PlayerMysteryShop playerMysteryShop(){
		if(this.playerMysteryShop==null) this.playerMysteryShop=new PlayerMysteryShop();
		this.playerMysteryShop.LateInit(this);
		return this.playerMysteryShop;
	}
	public PlayerActivityTask playerActivityTask(){
		if(this.playerActivityTask==null) this.playerActivityTask=new PlayerActivityTask();
		this.playerActivityTask.LateInit(this);
		return this.playerActivityTask;
	}
	public PlayerQueue playerQueue(){
		if(this.playerQueue==null) this.playerQueue=new PlayerQueue();
		this.playerQueue.LateInit(this);
		return this.playerQueue;
	}
	public PlayerBuild playerBuild(){
		if(this.playerBuild==null) this.playerBuild=new PlayerBuild();
		this.playerBuild.LateInit(this);
		return this.playerBuild;
	}
	public PlayerMemorialHall playerMemorialHall(){
		if(this.playerMemorialHall==null) this.playerMemorialHall=new PlayerMemorialHall();
		this.playerMemorialHall.LateInit(this);
		return this.playerMemorialHall;
	}
	public PlayerTrain playerTrain(){
		if(this.playerTrain==null) this.playerTrain=new PlayerTrain();
		this.playerTrain.LateInit(this);
		return this.playerTrain;
	}

    public PlayerArms playerArms(){
		if(this.playerArms==null) this.playerArms=new PlayerArms();
		this.playerArms.LateInit(this);
		return this.playerArms;
	}

    public PlayerRepairTrain playerRepairTrain(){
		if(this.playerRepairTrain==null) this.playerRepairTrain=new PlayerRepairTrain();
		this.playerRepairTrain.LateInit(this);
		return this.playerRepairTrain;
	}

    public PlayerCityWarSkill playerCityWarSkill(){
		if(this.playerCityWarSkill==null) this.playerCityWarSkill=new PlayerCityWarSkill();
		this.playerCityWarSkill.LateInit(this);
		return this.playerCityWarSkill;
	}
	public PlayerCaptive playerCaptive(){
		if(this.playerCaptive==null) this.playerCaptive=new PlayerCaptive();
		this.playerCaptive.LateInit(this);
		return this.playerCaptive;
	}
	public PlayerAircraft playerAircraft(){
		if(this.playerAircraft==null) this.playerAircraft=new PlayerAircraft();
		this.playerAircraft.LateInit(this);
		return this.playerAircraft;
	}
	public PlayerAircraftCarrier playerAircraftCarrier(){
		if(this.playerAircraftCarrier==null) this.playerAircraftCarrier=new PlayerAircraftCarrier();
		this.playerAircraftCarrier.LateInit(this);
		return this.playerAircraftCarrier;
	}
	public PlayerWarMakes playerWarMakes(){
		if(this.playerWarMakes==null) this.playerWarMakes=new PlayerWarMakes();
		this.playerWarMakes.LateInit(this);
		return this.playerWarMakes;
	}

	public PlayerEvent playerEvent() {
		if(this.playerEvent==null) this.playerEvent=new PlayerEvent();
		this.playerEvent.LateInit(this);
		return this.playerEvent;
	}

	public PlayerKfTask playerKfTask() {
		if (this.playerKfTask == null) this.playerKfTask = new PlayerKfTask();
		this.playerKfTask.LateInit(this);
		return this.playerKfTask;
	}

	public PlayerVideoOrder playerVideoOrder() {
		if(this.playerVideoOrder==null) this.playerVideoOrder=new PlayerVideoOrder();
		this.playerVideoOrder.LateInit(this);
		return this.playerVideoOrder;
	}

	public PlayerElement playerElement() {
		if(this.playerElement==null) this.playerElement=new PlayerElement();
		this.playerElement.LateInit(this);
		return this.playerElement;
	}

    public PlayerBlueVip playerBlueVip() {
        if (this.playerBlueVip == null) this.playerBlueVip = new PlayerBlueVip();
        this.playerBlueVip.LateInit(this);
        return this.playerBlueVip;
    }

    public PlayerGame playerGame() {
        if (this.playerGame == null) this.playerGame = new PlayerGame();
        this.playerGame.LateInit(this);
        return this.playerGame;
    }


    public PlayerFriend playerFriend() {
        if (this.playerFriend == null) this.playerFriend = new PlayerFriend();
        this.playerFriend.LateInit(this);
        return this.playerFriend;
    }

	public PlayerGiftPush playerGiftPush() {
		if (this.playerGiftPush == null) this.playerGiftPush = new PlayerGiftPush();
		this.playerGiftPush.LateInit(this);
		return this.playerGiftPush;
	}
	public PlayerAds playerAds() {
		if (this.playerAds == null) this.playerAds = new PlayerAds();
		this.playerAds.LateInit(this);
		return this.playerAds;
	}
	public PlayerRecord playerRecord() {
		if (this.playerRecord == null) this.playerRecord = new PlayerRecord();
		this.playerRecord.LateInit(this);
		return this.playerRecord;
	}
	public PlayerShare playerShare() {
		if (this.playerShare == null) this.playerShare = new PlayerShare();
		this.playerShare.LateInit(this);
		return this.playerShare;
	}

	public PlayerFish playerFish() {
		if (this.playerFish == null) this.playerFish = new PlayerFish();
		this.playerFish.LateInit(this);
		return this.playerFish;
	}
	public PlayerStrength playerStrength() {
		if (this.playerStrength == null) this.playerStrength = new PlayerStrength();
		this.playerStrength.LateInit(this);
		return this.playerStrength;
	}
	public PlayerTips playerTips() {
		if (this.playerTips == null) this.playerTips = new PlayerTips();
		this.playerTips.LateInit(this);
		return this.playerTips;
	}

	public PlayerMissionBox playerMissionBox(){
		this.playerMissionBox.LateInit(this);
		return this.playerMissionBox;
	}

	public PlayerTankMaster playerTankMaster() {
		if (this.playerTankMaster == null) this.playerTankMaster = new PlayerTankMaster();
		this.playerTankMaster.LateInit(this);
		return this.playerTankMaster;
	}

	public PlayerPaperResearch playerPaperResearch(){
		if (this.playerPaperResearch == null) this.playerPaperResearch = new PlayerPaperResearch();
		this.playerPaperResearch.LateInit(this);
		return this.playerPaperResearch;
	}

	public PlayerFieldBoss playerFieldBoss(){
		if (this.playerFieldBoss == null) this.playerFieldBoss = new PlayerFieldBoss();
		this.playerFieldBoss.LateInit(this);
		return this.playerFieldBoss;
	}


	/**
	 * 填充玩家信息到消息体中
	 * @param msg
	 * @param ignoreChange true:不分是否change全部填充
	 */
	public void fillMsg(JsonMsg msg,boolean ignoreChange) {
		msg.addProperty("id", getId());
		msg.addProperty("uid", this.getUid());
		msg.addProperty("serverId", getServerId());
		msg.addProperty("createServerId", getCreateServerId());
		msg.addProperty("name", name);

		playerBaseInfo().fillMsg(msg,ignoreChange);
		playerLevel().fillMsg(msg,ignoreChange);
		playerCurrency().fillMsg(msg,ignoreChange);
		playerMission().fillMsg(msg, ignoreChange);
		playerTreasury().fillMsg(msg, ignoreChange);
		getPlayerVipInfo().fillMsg(msg,ignoreChange);
		playerBag().fillMsg(msg,ignoreChange);
		playerStatus().fillMsg(msg,ignoreChange);
		getPlayerDynamicData().fillMsg(msg,ignoreChange);
		getPlayerFunction().fillMsg(msg,ignoreChange);
		getPlayerRecharge().fillMsg(msg, ignoreChange);
		getPlayerStatistics().fillMsg(msg, ignoreChange);
		getPlayerCDs().fillMsg(msg, ignoreChange);
		playerChat().fillMsg(msg,ignoreChange);
		getPlayerVipInfo().fillMsg(msg, ignoreChange);
		playerTask().fillMsg(msg, ignoreChange);
		getPlayerActivity().fillMsg(msg,ignoreChange);
		playerSet().fillMsg(msg, ignoreChange);
		playerDailyTask().fillMsg(msg, ignoreChange);
		playerTank().fillMsg(msg,ignoreChange);
		playerResearchTank().fillMsg(msg, ignoreChange);
        playerTankFactory().fillMsg(msg, ignoreChange);
		playerEquip().fillMsg(msg,ignoreChange);
		playerTankPaper().fillMsg(msg,ignoreChange);
		playerTankpiece().fillMsg(msg,ignoreChange);
		playerShop().fillMsg(msg, ignoreChange);
		playerHead().fillMsg(msg, ignoreChange);
		playerGuild().fillMsg(msg, ignoreChange);
		playerBattle().fillMsg(msg, ignoreChange);
//		playerExpedition().fillMsg(msg,ignoreChange);
		playerCommander().fillMsg(msg,ignoreChange);
		playerGuide().fillMsg(msg,ignoreChange);
		playerHonor().fillMsg(msg,ignoreChange);
		// playerTrade().fillMsg(msg,ignoreChange);
//		playerShipTrade().fillMsg(msg,ignoreChange);
//		playerRobSupply().fillMsg(msg,ignoreChange);
//		playerLevelEvent().fillMsg(msg, ignoreChange);
		playerStone().fillMsg(msg, ignoreChange);
		playerTitle().fillMsg(msg, ignoreChange);
//		playerOverallWar().fillMsg(msg,ignoreChange);
		playerBuffer().fillMsg(msg, ignoreChange);
//		playerCustomTanks().fillMsg(msg, ignoreChange);
//		playerRandomTask().fillMsg(msg, ignoreChange);
//		playerInviteInfo().fillMsg(msg, ignoreChange);
		playerGiftPack().fillMsg(msg, ignoreChange);
//		playerResBack().fillMsg(msg, ignoreChange);
//		playerMastery().fillMsg(msg, ignoreChange);
//		playerAgent().fillMsg(msg, ignoreChange);
		playerFight().fillMsg(msg, ignoreChange);
		playerCloneTroops().fillMsg(msg, ignoreChange);
		playerPresidentData().fillMsg(msg, ignoreChange);
//		playerMilitaryLineup().fillMsg(msg, ignoreChange);
//		playerWorldBuildData().fillMsg(msg, ignoreChange);
		playerMysteryShop().fillMsg(msg, ignoreChange);
		playerActivityTask().fillMsg(msg, ignoreChange);
//		playerMemorialHall().fillMsg(msg, ignoreChange);
//		playerBuild().fillMsg(msg, ignoreChange);
//		playerQueue().fillMsg(msg, ignoreChange);
//		playerWarcraft().fillMsg(msg, ignoreChange);
		playerTrain().fillMsg(msg, ignoreChange);
		playerRepairTrain().fillMsg(msg, ignoreChange);
//		playerArms().fillMsg(msg, ignoreChange);
//		playerCityWarSkill().fillMsg(msg, ignoreChange);
//		playerCaptive().fillMsg(msg, ignoreChange);
//		playerAircraft().fillMsg(msg, ignoreChange);
//		playerAircraftCarrier().fillMsg(msg, ignoreChange);
//		playerWarMakes().fillMsg(msg, ignoreChange);
		playerEvent().fillMsg(msg, ignoreChange);
		playerElement().fillMsg(msg, ignoreChange);
//		playerKfTask().fillMsg(msg, ignoreChange);
//        playerBlueVip().fillMsg(msg, ignoreChange);
//        playerGame().fillMsg(msg, ignoreChange);
        playerFriend().fillMsg(msg, ignoreChange);
		playerGiftPush().fillMsg(msg, ignoreChange);
		playerAds().fillMsg(msg, ignoreChange);
		playerRecord().fillMsg(msg, ignoreChange);
		playerShare().fillMsg(msg, ignoreChange);
//		playerFish().fillMsg(msg, ignoreChange);
		playerStrength().fillMsg(msg, ignoreChange);
		playerMissionBox().fillMsg(msg, ignoreChange);
		playerTankMaster().fillMsg(msg, ignoreChange);
		playerPaperResearch().fillMsg(msg, ignoreChange);
		playerFieldBoss().fillMsg(msg, ignoreChange);
		playerMainTask().fillMsg(msg, ignoreChange);
		playerGiftPack().fillMsg(msg, ignoreChange);
	}

	public int getGuildId() {
		return playerGuild().getGuildId();
	}
	
	public void sendUserUpdateMsg() {
		sendUserUpdateMsg(false,false);//立即存储
	}
	public void sendUserUpdateMsgAndNowDB() {
		sendUserUpdateMsg(false,true);//立即存储
	}
	public void sendUserUpdateMsg(boolean delaySaveDB) {
		sendUserUpdateMsg(delaySaveDB,false);//立即存储
	}
	//是否延迟保存到数据库内
	public void sendUserUpdateMsg(boolean delaySaveDB,boolean isNowDb) {
		if(isKFPlayer()) {
			return;
		}
		boolean isOnline = isOnline();
		if(isOnline) {
			JsonMsg updateMsg = JsonMsg.create(MessageComm.S2C_PlayerUpdate);
			fillMsg(updateMsg, false);
			sendMsg(updateMsg);
		}
		if(isNowDb) {
			saveNowDB();
			this.lastDBTime = System.currentTimeMillis();
		}else{
			if(!delaySaveDB || !isOnline || DbMonotor.checkCanSavePlayer(this)) {
				saveDB();
				this.lastDBTime = System.currentTimeMillis();
			}
		}
	}
	
	public void saveNowDB() {
		if(isKFPlayer()) {
			return;
		}
		PlayerUtils.saveOrUpdate(this);
		isDBChangeMark.set(false);
	}
	
	public abstract void saveDB();
	public abstract void notifyObservers(ObservableEnum observableEnum, Object... argv);
	
	public void changeDBChangeMark() {
		isDBChangeMark.set(true);
	}
	public boolean isDBChangeMark() {
		return isDBChangeMark.get();
	}
	//获取当前空闲的建筑队列
	public BuildQueueType getFreeBuildQueue() {
		List<BuildQueueType> freeQueues = Lists.newArrayList(BuildQueueType.values());
		List<AbstractQueue> buildQueues = playerQueue().getQueueByType(QueueType.BuildUp);
		for(AbstractQueue queue :buildQueues){
			BuildUpQueue q=(BuildUpQueue)queue;
			for(int i = freeQueues.size()-1;i>=0;i--){
				if(freeQueues.get(i).getType()==q.getBuildQueueType()){
					freeQueues.remove(i);
					break;
				}
			}
		}
		if(freeQueues.contains(BuildQueueType.Normal)){
			return BuildQueueType.Normal;
		}
		if(freeQueues.contains(BuildQueueType.Queue2)&&playerQueue().getBuyQueueNum()>0){
			return BuildQueueType.Queue2;
		}
		if(freeQueues.contains(BuildQueueType.Queue3)&&playerQueue().getBuyQueueNum()>1){
			return BuildQueueType.Queue3;
		}
		return BuildQueueType.None;
	}

	public boolean isKFPlayer() {
		return false;
	}
}
