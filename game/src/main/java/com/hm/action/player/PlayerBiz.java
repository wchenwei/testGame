package com.hm.action.player;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.hm.action.guild.biz.GuildBiz;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.biz.PlayerNameBiz;
import com.hm.action.task.biz.MainTaskBiz;
import com.hm.action.troop.biz.WorldTroopBiz;
import com.hm.action.warMake.WarMakesBiz;
import com.hm.config.GameConstants;
import com.hm.config.PlayerHeadConfig;
import com.hm.config.TaskConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.PlayerDefautConfig;
import com.hm.config.excel.PlayerLevelConfig;
import com.hm.config.excel.ShopConfig;
import com.hm.config.excel.templaextra.PlayerHeadExtraTemplate;
import com.hm.config.excel.templaextra.PlayerLevelExtraTemplate;
import com.hm.container.PlayerContainer;
import com.hm.db.PlayerUtils;
import com.hm.enums.*;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import com.hm.libcore.serverConfig.ServerConfig;
import com.hm.log.LogBiz;
import com.hm.message.MessageComm;
import com.hm.model.backRes.AdvanceVo;
import com.hm.model.item.Items;
import com.hm.model.player.*;
import com.hm.observer.IObservable;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.redis.data.HeadData;
import com.hm.redis.util.RedisUtil;
import com.hm.server.GameServerManager;
import com.hm.servercontainer.idcode.IdCodeContainer;
import com.hm.servercontainer.idcode.IdCodeInfo;
import com.hm.servercontainer.idcode.IdCodeItemContainer;
import com.hm.util.InviteCodeGenerator;
import com.hm.util.ServerUtils;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Biz
public class PlayerBiz implements IObserver, IObservable{
	@Resource
	private LogBiz logBiz;
	@Resource
	private PlayerDefautConfig playerDefautConfig;
	@Resource
	private PlayerLevelConfig playerLevelConfig;
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private ShopConfig shopConfig;
	@Resource
	private TaskConfig taskConfig;
	@Resource
	private GuildBiz guildBiz;
	@Resource
	private WarMakesBiz warMakesBiz;
	@Resource
	private PlayerHeadConfig playerHeadConfig;
	@Resource
	private WorldTroopBiz worldTroopBiz;
	@Resource
	private MainTaskBiz mainTaskBiz;
	@Resource
	private PlayerNameBiz playerNameBiz;

	/**
	 * createPlayer:(这里用一句话描述这个方法的作用). <br/>  
	 * @author Administrator  
	 * @param uid
	 * @param serverId
	 * @param channelId
	 * @param imei
	 * @param icon
	 * @param name
	 * @return  使用说明
	 */
	public Player createPlayer(long uid, int createServerId,int serverId, int channelId, String imei, int icon) {
		long playerId = PrimaryKeyWeb.getIntPrimaryKey(Player.class, serverId);
		if(playerId > ServerUtils.getMaxPlayerId(serverId)) {
			return null;
		}
		Player player=new Player();
		player.setId(playerId);
		player.playerFix().setInviteCode(InviteCodeGenerator.generateInviteCode());
		player.setServerId(serverId);
		player.setUidForCreate(uid,createServerId);
		player.changeName(playerNameBiz.randomPlayerName(serverId));
		player.playerTemp().setLastMsgTime(System.currentTimeMillis());
		player.playerHead().changeIcon(icon);
		player.setChannelId(channelId);
		player.getPlayerStatistics();//提前加载一下
		player.playerBaseInfo().setCreateDate(new Date());
		player.playerMissionBox().updateBoxTime();
		if(imei==null) {
			imei=player.getId()+"";
		}
		player.playerBaseInfo().setImei(imei);
		//初始化资产道具
		playerDefautConfig.initNewPlayer(player);
		initNewPlayer(player);
		player.playerSet().initCreateSetting(commValueConfig.getConvertObj(CommonValueType.DefaultOpenSets));
		//创建用户，给创建
		// player.playerTankFactory().reset();//坦克工厂天重置
		
		//初始化玩家任务,需要放在玩家等级初始化之后
		taskConfig.initPlayer(player);
		worldTroopBiz.doPlayerCreate(player);
		mainTaskBiz.loadTaskForCreatePlayer(player);
		player.saveNowDB();
		PlayerContainer.addPlayer2Map(player);

		RedisUtil.updateRedisInviteCode(player);

		return player;
	}
	
	
	/**  
	 * initNewPlayer:初始化玩家  
	 * @author zqh  
	 * @param player player
	 *
	 */
	private void initNewPlayer(Player player) {
		player.playerLevel().setLv(1);//初始化等级
	}
	/**
	 * 消耗玩家资产
	 * @param player
	 * @param currencyKind 资产类型
	 * @param spend 消耗数量
	 * @param logType 日志类型
	 */
	public void spendPlayerCurrency(BasePlayer player, CurrencyKind currencyKind, long spend, LogType logType) {
		if(spend>0) {
			if(currencyKind==CurrencyKind.Gold||currencyKind==CurrencyKind.SysGold){
				spendGold(player,spend,logType);
			}else{
				player.playerCurrency().spend(currencyKind,spend);
                if (logType != LogType.Advance) {
                    player.notifyObservers(ObservableEnum.CostRes, currencyKind.getIndex(), spend);
                }
				logBiz.currencySpendLog(player,currencyKind,spend,logType);
			}
		}
	}
	
	/**
	 * 消耗金币(包含系统金和充值金)
	 */
	public void spendGold(BasePlayer player,long spend,LogType logType){
		//消耗系统金
		long spendSysGold = Math.min(player.playerCurrency().get(CurrencyKind.SysGold), spend);
		//消耗充值金
		long spendRecGold = Math.max(spend-spendSysGold, 0);
		if (spendSysGold > 0) {
			player.playerCurrency().spend(CurrencyKind.SysGold, spendSysGold);
			logBiz.currencySpendLog(player, CurrencyKind.SysGold, spendSysGold, logType);
			player.notifyObservers(ObservableEnum.GoldStatistics, CurrencyKind.SysGold.getIndex(), spendSysGold, 0);
		}
		if(spendRecGold > 0) {
			player.playerCurrency().spend(CurrencyKind.Gold,spendRecGold);
			logBiz.currencySpendLog(player,CurrencyKind.Gold,spendRecGold,logType);
			player.notifyObservers(ObservableEnum.GoldStatistics, CurrencyKind.Gold.getIndex(),spendRecGold,0);
		}
        if (logType != LogType.Advance) {
			//记录累计消耗金砖
			player.getPlayerStatistics().addLifeStatistics(StatisticsType.SpendGold, spend);
			//记录今日消耗金砖数
			player.getPlayerStatistics().addTodayStatistics(StatisticsType.SpendGold, spend);
            player.notifyObservers(ObservableEnum.CostRes, CurrencyKind.SysGold.getIndex(), spend);
        }
    }

	/**
	 * 消耗资产
	 * @param player
	 * @param currencyKind
	 * @param spend
	 * @param logType
	 */
	public void spendPlayerCurrency(BasePlayer player, Map<CurrencyKind, Long> spendMap, LogType logType) {
		for(Map.Entry<CurrencyKind, Long> entry:spendMap.entrySet()) {
			if(entry.getValue()>0) {
				spendPlayerCurrency(player, entry.getKey(), entry.getValue(), logType);
			}
		}
	}
	
	/**
	 * 玩家资产增加
	 * @param player
	 * @param currencyKind
	 * @param add
	 * @param logType
	 */
	public void addPlayerCurrency(BasePlayer player, CurrencyKind currencyKind, long add, LogType logType) {
		if(currencyKind == null) {
			log.error(logType.toString()+" currency 不存在");
			return;
		}
		addPlayerCurrency(player,currencyKind,add,true,logType);
	}
	/**
	 * 玩家资产增加
	 * @param player
	 * @param currencyKind
	 * @param add
	 * @param logType
	 * @param isBeyond 是否可以超过上限
	 */
	public void addPlayerCurrency(BasePlayer player, CurrencyKind currencyKind, long add,boolean isBeyond, LogType logType) {
		if(add>0) {
//			if(!isBeyond && CurrencyKind.isRes(currencyKind)){
//				long resMax = player.getPlayerDynamicData().getResMax(currencyKind);
//				long currentRes = Math.min(player.playerCurrency().get(currencyKind)+add,resMax);
//				add = Math.max(currentRes - player.playerCurrency().get(currencyKind), 0);
//			}
            player.playerCurrency().add(currencyKind, add);
            player.notifyObservers(ObservableEnum.AddRes, currencyKind.getIndex(), add, logType);
            logBiz.currencyAddLog(player, currencyKind, add, logType);
            player.notifyObservers(ObservableEnum.GoldStatistics, currencyKind.getIndex(), add, 1);
            //判断增加功勋的同时，如果有部落，则增加部落的贡献
            if (currencyKind == CurrencyKind.Contr) {
                guildBiz.addContr(player, add);
            }
        }
	}
	/**
	 * addPlayerCurrency:(增加玩家资产). <br/>
	 * @author Administrator
	 * @param player
	 * @param addMap 玩家资源集合
	 * @param logType 行为标志位
	 */
	public void addPlayerCurrency(BasePlayer player, Map<CurrencyKind, Long> addMap, LogType logType) {
		addPlayerCurrency(player, addMap, true,logType);
	}
	/**
	 * addPlayerCurrency:(增加玩家资产). <br/>
	 * @author Administrator
	 * @param player
	 * @param addMap 玩家资源集合
	 * @param logType 行为标志位
	 * @param isBeyond 是否可以超过上限
	 */
	public void addPlayerCurrency(BasePlayer player, Map<CurrencyKind, Long> addMap,boolean isBeyond, LogType logType) {
		for(Map.Entry<CurrencyKind, Long> entry:addMap.entrySet()) {
			addPlayerCurrency(player, entry.getKey(),entry.getValue(),isBeyond, logType);
		}
	}
	
	/**
	 * 检查玩家资产是否足够
	 * @param player
	 * @param currencyKind 资产类型
	 * @param spend 消耗数量
	 * @param logType 日志类型
	 */
	public boolean checkPlayerCurrency(BasePlayer player, Map<CurrencyKind, Long> spendMap) {
		for(Map.Entry<CurrencyKind, Long> entry:spendMap.entrySet()) {
			if(!player.playerCurrency().canSpend(entry.getKey(),entry.getValue())) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 检查玩家资产是否足够
	 * @param player
	 * @param currencyKind 资产类型
	 * @param spend 消耗数量
	 * @param logType 日志类型
	 */
	public boolean checkPlayerCurrency(BasePlayer player, CurrencyKind kind, long num) {
		if(kind == null || !player.playerCurrency().canSpend(kind,num)) {
			return false;
		}
		return true;
	}
	/**
	 * 检查并消耗玩家资产
	 * @param player
	 * @param kind
	 * @param num
	 * @return
	 */
	public boolean checkAndSpendPlayerCurrency(BasePlayer player, Map<CurrencyKind, Long> spendMap, LogType logType) {
		if(checkPlayerCurrency(player, spendMap)){
			spendPlayerCurrency(player, spendMap, logType);
			return true;
		}
		return false;
	}
	/**
	 * 检查并消耗玩家资产
	 * @param player
	 * @param kind
	 * @param num
	 * @return
	 */
	public boolean checkAndSpendPlayerCurrency(BasePlayer player, CurrencyKind kind, long spend, LogType logType) {
		if(checkPlayerCurrency(player, kind, spend)){
			spendPlayerCurrency(player, kind, spend, logType);
			return true;
		}
		return false;
	}
	
	/**
	 * 增加玩家cd数量
	 * @param player
	 * @param type
	 * @param num
	 */
	public void addPlayerCd(BasePlayer player,CDType type,long num,LogType logType) {
		player.getPlayerCDs().addCdNum(type, num);
	}
	
	/**
	 * 增加 vip点数
	 */
	public void addVipExp(Player player, long exp, LogType logType){
		if(exp <= 0) {
			return;
		}
		PlayerVipInfo vipInfo = player.getPlayerVipInfo();
		vipInfo.addVipExp(exp);
		//当日充值的vip点数
		player.getPlayerStatistics().addTodayStatistics(StatisticsType.VipExp,exp);
		logBiz.vipExpIn(player, (int) exp, logType);
		int newVipLv = shopConfig.getVipLevel(vipInfo.getVipLv(), vipInfo.getVipExp());
		if(newVipLv > vipInfo.getVipLv()) {
			int oldVipLv = vipInfo.getVipLv();
			vipInfo.setVipLv(newVipLv);//vip升级了
			player.notifyObservers(ObservableEnum.VipLevelUp, oldVipLv,player.getPlayerVipInfo().getVipLv());
		}
	}
	/**
	 * 增加玩家经验
	 * @param player
	 * @param exp 新增经验
	 * @param logType
	 * @return
	 */
	public boolean addPlayerExp(BasePlayer player, long exp, LogType logType) {
		//玩家等级只和玩家关卡挂钩
//		if(exp<=0) {
//			return false;
//		}
//		int oldLv = player.playerLevel().getLv();
//		if(oldLv >= playerLevelConfig.getMaxLv()) {
//			//满级之后增加经验币
//			long expMoney = (long)(commValueConfig.getDoubleCommValue(CommonValueType.ExpMoneyRate)*exp);
//			addPlayerCurrency(player, CurrencyKind.ExpMoney, expMoney, logType);
//			return true;
//		}
//		Object [] lvExpArray = getPlayerLvExp(player,exp);
//		player.playerLevel().setLvExp((long) lvExpArray[1]);
//		logBiz.playerExpIn(player, exp, logType);
//		int newLv = (int) lvExpArray[0];
//		boolean isLvUp = notifyLvUp(player,newLv,oldLv);
//		player.notifyObservers(ObservableEnum.AddPlayerExp,exp,logType);
//		return isLvUp;
		return true;
	}


	/**
	 * 获取玩家等级经验
	 * @param player
	 * @param exp 新增经验
	 * @return
	 */
	public Object [] getPlayerLvExp(BasePlayer player,long exp){
		PlayerLevel playerLevel = player.playerLevel();
		// 剩余经验
		long lvExp = playerLevel.getLvExp() + exp;
		// 0：玩家等级  1:玩家等级剩余经验
		Object [] lvExpArray = {playerLevel.getLv(), lvExp};
		int maxLv = playerLevelConfig.getMaxLv();
		calPlayerLvExp(lvExpArray,maxLv);
		return lvExpArray;
	}

	/**
	 * 计算玩家等级经验
	 * @param lvExpArray 0：玩家等级  1:玩家等级剩余经验
	 * @param maxLv 最大等级
	 */
	private void calPlayerLvExp(Object[] lvExpArray,int maxLv){
		int lv = (int) lvExpArray[0];
		long lvExp = (long) lvExpArray[1];
		PlayerLevelExtraTemplate playerLevelTemplate = playerLevelConfig.getPlayerLevelTemplate(lv);
		// 剩余经验 > 升下级所需经验
		if (lvExp > playerLevelTemplate.getExp()){
			lvExp = lvExp - playerLevelTemplate.getExp();
			lv ++;
			if (lv > maxLv){
				return;
			}
			lvExpArray[0] = lv;
			lvExpArray[1] = lvExp;
			calPlayerLvExp(lvExpArray,maxLv);
		}
	}

	public boolean notifyLvUp(BasePlayer player,int newLv,int oldLv){
		boolean lvUp = newLv > oldLv;
		if(lvUp) {//升级处理
			for (int i = oldLv+1; i <= newLv; i++) {
				player.playerLevel().setLv(i);//设置最新等级
				player.notifyObservers(ObservableEnum.PlayerLevelChange, player.playerLevel().getLv());
			}
			player.notifyObservers(ObservableEnum.PlayerLevelUp);
		}
		return lvUp;
	}

	//增加荣誉
	public void addPlayerHonor(Player player,HonorType type,long addHonor,LogType logType) {
		if(addHonor <= 0) {
			return;
		}
		player.playerHonor().addHonor(type, addHonor);
		player.notifyObservers(ObservableEnum.AddHonor, type.getType(),addHonor,logType);
		//记录日志
		logBiz.addGoods(player, PlayerAssetEnum.Honor.getTypeId(), addHonor, ItemType.CURRENCY.getType(), logType);
	}
	//清除预扣资源
	public void clearAdvanceRes(Player player){
        AdvanceVo vo = player.playerCurrency().getAdvanceVo();
        if (vo == null || CollUtil.isEmpty(vo.getRewards())) {
            return;
        }
        //先把预扣的加上再减去
        itemBiz.addItem(player, vo.getRewards(), null);
        itemBiz.checkItemEnoughAndSpend(player, vo.getRewards(), vo.getLogType());
		player.playerCurrency().clearAdvances();
	}
	/*public void checkAdvanceOil(Player player) {
		long advanceOil = player.playerCurrency().getAdvanceOil();
		if(advanceOil>0){
			addPlayerCurrency(player, CurrencyKind.Oil, advanceOil, LogType.ResFailReturn);
			clearAdvanceRes(player);
		}
	}*/
	
	public void checkAdvances(Player player) {
        AdvanceVo vo = player.playerCurrency().getAdvanceVo();
        if (vo != null && CollUtil.isNotEmpty(vo.getRewards())) {
            itemBiz.addItem(player, vo.getRewards(), LogType.ResFailReturn.value(vo.getLogType().getCode()));
            player.playerCurrency().clearAdvances();
		}
	}

    /**
     * 改名字
     * 涉及部落长名字 部落成员名字 世界地图信息的名字
     * @param name
     */
	public void changeName(Player player,String name) {
        player.changeName(name);
    }

    public void addAdvances(Player player, List<Items> advances, LogType logType, int id) {
        AdvanceVo advanceVo = new AdvanceVo(logType, advances);
        advanceVo.setTagId(id);
        player.playerCurrency().addAdvance(advanceVo);
    }

    public void addAdvances(Player player, List<Items> advances, LogType logType) {
        AdvanceVo advanceVo = new AdvanceVo(logType, advances);
        player.playerCurrency().addAdvance(new AdvanceVo(logType, advances));
    }

    public void addAdvances(Player player, Items advances, LogType logType) {
        player.playerCurrency().addAdvance(new AdvanceVo(logType, Lists.newArrayList(advances)));
    }

    //获取服务器玩家最大等级
    public int getMaxLv(int serverId) {
        return 0;//没有等级排行了
    }
	/**
	 * 校验，检查玩家唯一码
	 * @param player
	 * @param idCode 客户端本地存储的唯一码
	 * @param bindIdCode 账号本身附带的唯一码(想要绑定的唯一码)
	 * @return
	 */
	public boolean checkPlayerIdCode(Player player, String idCode,String bindIdCode,String ip) {
		//不检查
		if(!ServerConfig.getInstance().isIdCodeSwitch()&&!ServerConfig.getInstance().isIpFilterSwitch()){
			return true;
		}
		int vipLv = player.getPlayerVipInfo().getVipLv();
		int vipLimit = commValueConfig.getCommValue(CommonValueType.VipFilterLimit);
		int ipLoginLimit = commValueConfig.getCommValue(CommonValueType.IpLoginLimit);
		int idCodeBindLimit = commValueConfig.getCommValue(CommonValueType.IdCodeBindLimit);
		//如果vipLv>=2则放行
		if(vipLv>=vipLimit){
			return true;
		}
		//没充值。。。。。。
		
		IdCodeItemContainer idCodeItemContainer= IdCodeContainer.of(player);
		if(ServerConfig.getInstance().isIpFilterSwitch()){
			//如果该ip该serverId下已经登陆的用户超过了某值则限制登录
			int ipNum = idCodeItemContainer.getIpNum(ip);
			//如果该ip该serverId下已经登陆的用户超过了2人
			if(ipNum>=ipLoginLimit){
				return false;
			}
		}
		if(!ServerConfig.getInstance().isIdCodeSwitch()){
			return true;
		}
		int bindNum = idCodeItemContainer.getIdCodeInfoNum(bindIdCode, player.getCreateServerId());
		if(StrUtil.isBlank(player.getIdCode())){//如果本角色本身没有绑定过唯一码
			//判断绑定checkIdCode的是否超上限
			if(ServerConfig.getInstance().isIdCodeSwitch()&&bindNum>=idCodeBindLimit){
				return false;
			}
			//没有超上限则将校验码
			bindIdCode(player,bindIdCode);
			return true;
		}
		//如果上传的校验码和我自己的校验码相同
		if(StrUtil.equals(idCode, player.getIdCode())){
			if(!ServerConfig.getInstance().isIdCodeSwitch()){//不限制登录则直接放行
				return true;
			}
			//如果限制登录则判断该玩家是否是正式绑定
			IdCodeInfo idCodeInfo = idCodeItemContainer.getIdCodeInfo(bindIdCode, player.getCreateServerId());
			int state = idCodeInfo.getState(player.getId());
			if(state==1){//如果是正式绑定则放行
				return true;
			}
			//不是正式绑定且没有达到绑定上限则将其重新绑定，绑定为正式
			if(bindNum<idCodeBindLimit){
				idCodeItemContainer.bindIdCode(bindIdCode,player.getCreateServerId(),player.getId());
				player.notifyObservers(ObservableEnum.BindIdCode, bindIdCode);
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	//绑定唯一码
	private void bindIdCode(Player player, String bindIdCode) {
		//绑定玩家身上的唯一码
		player.setIdCode(bindIdCode);
		player.saveDB();
		//绑定唯一码
		IdCodeContainer.of(player).bindIdCode(bindIdCode,player.getCreateServerId(),player.getId());
		player.notifyObservers(ObservableEnum.BindIdCode, bindIdCode);
	}

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.Recharge, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.HourEvent, this);
	}

    @Override
    public void invoke(ObservableEnum observableEnum, Player player, Object... argv) {
		switch (observableEnum){
			case Recharge:
				if (player != null && player.getExpireTime() != ON) {
					player.setExpireTime(ON);
				}
				return;
			case HourEvent:
				this.hourCheckHead();
				break;
			default:
				break;
		}
    }

	@Override
	public void notifyChanges(ObservableEnum observableEnum, Player player, Object... argv) {
		ObserverRouter.getInstance().notifyObservers( observableEnum, player, argv );
	}


	private static final long ON = -1L;

	public void checkExpireTime(Player player) {
		if (ON == player.getExpireTime()) {
			return;
		}

		long r = player.getPlayerStatistics().getLifeStatistics(StatisticsType.RECHARGE);
		if (r > 0) {
			player.setExpireTime(ON);
			return;
		}

		int lv = player.playerLevel().getLv();
		int nDay = 180;
		if (lv < 30) {
			nDay = 7;
		} else if (lv < 40) {
			nDay = 30;
		} else if (lv < 50) {
			nDay = 60;
		} else if (lv < 60) {
			nDay = 90;
		} else if (lv < 70) {
			nDay = 120;
		} else if (lv < 80) {
			nDay = 150;
		}
		player.setExpireTime(System.currentTimeMillis() + nDay * GameConstants.DAY);
	}

    /**
     * @param player
     * @param num     数量
     * @param logType
     * @description 道具形式添加战令经验
     * @date 2021/5/19 14:28
     */
    public void addWarMakes(Player player, long num, LogType logType) {
        if (num <= 0) {
            return;
        }
        warMakesBiz.addExperience(player, (int) num);
        //记录日志
        logBiz.addGoods(player, PlayerAssetEnum.KfWarMakes.getTypeId(), num, ItemType.CURRENCY.getType(), logType);
    }

	public void addIcon(Player player, int icon){
		PlayerHeadExtraTemplate headExtraTemplate = playerHeadConfig.getHead(icon);
		if(headExtraTemplate == null){
			return;
		}
		if(headExtraTemplate.getTime() > 0){
			// 有时效的，直接重置时间
			long endTime = DateUtil.offsetHour(DateUtil.date(), headExtraTemplate.getTime()).getTime();
			player.playerHead().unlockIcon(icon, endTime);
			HeadData headData = new HeadData(player.getId(), icon, endTime);
			headData.saveRedis();
		}else {
			//解锁头像
			player.playerHead().unlockIcon(icon);
		}
		player.notifyObservers(ObservableEnum.UnlockIcon,icon);
		player.playerHead().changeIcon(icon);
		player.notifyObservers(ObservableEnum.ChangeIcon);
		player.sendUserUpdateMsg();
	}

	public void hourCheckHead(){
		HeadData.queryAll().forEach(headData -> {
			if(!GameServerManager.getInstance().isServerMachinePlayer(headData.getPlayerId())){
				return;
			}
			if(headData.getEndTime() > System.currentTimeMillis()){
				return;
			}
			Player player = PlayerUtils.getPlayer(headData.getPlayerId());
			if(player == null){
				return;
			}
			player.playerHead().checkHead();
			player.notifyObservers(ObservableEnum.ChangeIcon);
			player.sendMsg(MessageComm.S2C_Player_UnlockIcon, player.playerHead().getIcon());
			player.sendUserUpdateMsg();
			headData.removeRedis();
		});
	}
}


