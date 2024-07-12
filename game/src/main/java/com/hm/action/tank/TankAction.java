 /**  
 * Project Name:SLG_GameFuture.
 * File Name:MailAction.java  
 * Package Name:com.hm.action.mail  
 * Date:2017年12月18日下午3:50:23  
 * Copyright (c) 2017, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
 */  
  
package com.hm.action.tank;

 import cn.hutool.core.bean.BeanUtil;
 import com.google.common.collect.Lists;
 import com.google.common.collect.Maps;
 import com.hm.config.excel.temlate.TankLevelTemplate;
 import com.hm.config.excel.templaextra.*;
 import com.hm.libcore.annotation.MsgMethod;
 import com.hm.libcore.msg.JsonMsg;
 import com.hm.action.AbstractPlayerAction;
 import com.hm.action.item.ItemBiz;
 import com.hm.action.player.PlayerBiz;
 import com.hm.action.tank.biz.FactoryBiz;
 import com.hm.action.tank.biz.ResearchBiz;
 import com.hm.action.tank.biz.TankBiz;
 import com.hm.action.tank.biz.TankSoldierBiz;
 import com.hm.action.tank.enums.TankDetailMsg;
 import com.hm.action.tank.vo.ItemsVo;
 import com.hm.config.PlayerFunctionConfig;
 import com.hm.config.excel.CommValueConfig;
 import com.hm.config.excel.ItemConfig;
 import com.hm.config.excel.TankConfig;
 import com.hm.config.excel.TankFettersConfig;
 import com.hm.db.PlayerUtils;
 import com.hm.enums.*;
 import com.hm.message.MessageComm;
 import com.hm.model.item.Items;
 import com.hm.model.player.Player;
 import com.hm.model.player.CurrencyKind;
 import com.hm.model.tank.Driver;
 import com.hm.model.tank.Tank;
 import com.hm.observer.ObservableEnum;
 import com.hm.redis.PlayerRedisData;
 import com.hm.redis.util.RedisUtil;
 import com.hm.sysConstant.ItemConstant;
 import com.hm.sysConstant.SysConstant;
 import com.hm.war.sg.setting.TankSetting;
 import com.hm.libcore.annotation.Action;

 import javax.annotation.Resource;
 import java.util.List;
 import java.util.Map;

/**  
 * ClassName: TankAction. <br/>  
 * date: 2017年12月18日 下午3:50:23 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Action
public class TankAction extends AbstractPlayerAction {
	@Resource
	private TankBiz tankBiz;
	@Resource
	private ItemConfig itemConfig; 
	@Resource
	private ItemBiz itemBiz;
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private ResearchBiz researchBiz;
	@Resource
	private FactoryBiz factoryBiz;
	@Resource
    private CommValueConfig commValueConfig;
	@Resource
	private TankFettersConfig tankFettersConfig;
	@Resource
	private TankSoldierBiz tankSoldierBiz;
	@Resource
	private PlayerFunctionConfig playerFunctionConfig;

	
	/**
	 * 坦克升级
	 * @param
	 * @param
	 */
	@MsgMethod(MessageComm.C2S_TankLvUp)
	public void lvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId"); // 坦克id
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		if(tank.getLv() >= tankConfig.getTankLvMax()) {
			player.sendErrorMsg(SysConstant.TANK_LV_MAX);
			return;
		}
		if (tank.getLv() >= tankBiz.getPlayerTankMaxLv(player, tank)){
			player.sendErrorMsg(SysConstant.TANK_LV_LIMIT);
			return;
		}
		int itemId = commValueConfig.getCommValue(CommonValueType.TankLvUpCostItemId);
		TankLevelTemplate levelTemplate = tankConfig.getTankLevelTemplate(tank.getLv());
		// 扣除道具
		if(!itemBiz.checkItemEnoughAndSpend(player, itemId, levelTemplate.getExp(), ItemType.ITEM, LogType.TankLvUp.value(tankId))) {
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		tankBiz.lvUpTank(player, tank);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankLvUp,true);
	}
	
	/**
	 * 坦克一键升级
	 * @param session
	 * @param req
	 */
	@MsgMethod ( MessageComm.C2S_TankMaxLvUp)
	public void maxLvUp(Player player, JsonMsg msg){
		int tankId = msg.getInt("tankId"); //坦克id
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null){
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return; 
		}
		if(tank.getLv() >= tankConfig.getTankLvMax()){
			player.sendErrorMsg(SysConstant.TANK_LV_MAX);
			return;
		}
		if (tank.getLv() >= tankBiz.getPlayerTankMaxLv(player, tank)){
			player.sendErrorMsg(SysConstant.TANK_LV_LIMIT);
			return;
		}
		tankBiz.maxLvUpTank(player, tank);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankMaxLvUp,true);
		
	}

	/**
	 * 坦克升星
	 */
	@MsgMethod(MessageComm.C2S_TankStarUp)
	public void starUp(Player player,JsonMsg msg){
		if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankStar)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		int tankId = msg.getInt("tankId");
		int type = msg.getInt("type"); // 0 不使用万能图纸  1 使用万能图纸
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null){
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		StarLevelTemplateImpl nodeStarLevelTemplate = tankConfig.getTankStarLevelTemplate(tank.getId(), tank.getStar(), tank.getStarNode() +1);
		if (nodeStarLevelTemplate == null){
			player.sendErrorMsg(SysConstant.TANK_STAR_MAX);
			return;
		}
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		long paperCost = nodeStarLevelTemplate.getNeed_paper();
		int paperId = tankSetting.getPaper_id(); 
		//检查图纸
		List<Items> costItems = tankBiz.checkSpendItems(player, tankSetting, paperId, paperCost, type);
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.TankStarUp.value(String.format("%s_%s", tankId, tank.getStar())))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		tankBiz.unlockStartNode(player, tank, nodeStarLevelTemplate);
		JsonMsg serverMsg = JsonMsg.create(MessageComm.S2C_TankStarUp);
		if (nodeStarLevelTemplate.isStarUp()){
			StarUnlockTemplateImpl tankStarTemplate = tankConfig.getTankStarTemplate(tank.getStar());
			serverMsg.addProperty("position", tankStarTemplate.getSkill_unlock());//解锁技能位置
		}
		player.sendUserUpdateMsg();
		player.sendMsg(serverMsg);
	}


	/**
	 * 坦克合成
	 */
	@MsgMethod (MessageComm.C2S_TankUnlock)
	public void unLock(Player player,JsonMsg msg){
		int paperId = msg.getInt("paperId"); //坦克图纸id
		
		int tankId = tankConfig.getTankPaperTemplate(paperId).getTank_id();
		Tank tank = player.playerTank().getTank(tankId);
		if(tank != null){
			player.sendErrorMsg(SysConstant.TANK_EXIST);
			return; 
		}
		
		int star = tankConfig.getTankSetting(tankId).getStar(); 
		int count = tankConfig.getTankUnlock(star);
		//扣除道具
		if(!itemBiz.checkItemEnoughAndSpend(player, paperId, count,ItemType.PAPER, LogType.TankUnlock.value(tankId))) {
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		tankBiz.addTank(player, tankId,LogType.TankUnlock);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankUnlock,tankId);
	}

	/**
	 * 坦克突破
	 *
	 * @author yanpeng 
	 * @param player
	 * @param msg  
	 *
	 */
	@MsgMethod(MessageComm.C2S_TankReform)
	public void reform(Player player,JsonMsg msg){
		int tankId = msg.getInt("tankId"); // 坦克id
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		int reformMaxLv = tankConfig.getTankReformMaxLv(tank);
		if (tank.getReLv() >= reformMaxLv){
			player.sendErrorMsg(SysConstant.TANK_RELV_MAX);
			return;
		}

		TankLevelTemplate levelTemplate = tankConfig.getTankLevelTemplate(tank.getLv());
		Integer unlockReLv = levelTemplate.getReform_level_unclock();
		if (unlockReLv <= tank.getReLv()){
			return;
		}
		ReformTemplate reformTemplate = tankConfig.getTankReformTemplate(tank);
		if (!itemBiz.checkItemEnoughAndSpend(player, reformTemplate.getItemCost(), LogType.TankReform)){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		tankBiz.reform(player, tank);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankReform,true);
	}

	
	/**
	 * 车长升级
	 *
	 * @author yanpeng 
	 * @param player
	 * @param msg  
	 *
	 */
	@MsgMethod ( MessageComm.C2S_DriverLvUp)
	public void driverLvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId");
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Driver)){
			player.sendErrorMsg(SysConstant.DRIVER_LOCK);
			return;
		}
		int lv = tank.getDriver().getLv();
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		if(lv >= tankConfig.getDriverLvMax(tankSetting.getRare())){
			player.sendErrorMsg(SysConstant.DRIVER_LV_MAX);
			return;
		}
		if (lv >= tankConfig.getDriverUpLvMax(tankSetting.getRare(), tank.getDriver().getEvolveLv())){
			player.sendErrorMsg(SysConstant.DRIVER_LV_MAX);
			return;
		}
		if (lv >= tank.getLv()){
			player.sendErrorMsg(SysConstant.DRIVER_LV_MAX);
			return;
		}
		
		DriverLvTemplate temp = tankConfig.getDriverLvTemplate(tankSetting.getRare(), lv);
		List<Items> costItems = Lists.newArrayList(); 
		costItems.addAll(temp.getItemCost());
		
		if(!itemBiz.checkItemEnough(player, costItems)){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		//进阶需要消耗魂魄，可以用万能魂魄
		if(temp.getCost_soul()>0){
			int soulId = tankConfig.getDriverTemplate(tankId).getSoul();
			int cost = temp.getCost_soul(); 
			long count = player.playerBag().getItemCount(soulId);
			if(count >0){
				Items items = new Items(soulId,Math.min(cost, count),ItemType.ITEM.getType());
				costItems.add(items);
			}
			//需要消耗的道具
			Items needItems = new Items(soulId,cost,ItemType.ITEM.getType());
			if(!itemBiz.checkItemEnough(player,needItems)){
				cost -= count; 
				needItems = new Items(ItemConstant.COM_SOUL,cost,ItemType.ITEM.getType());
				costItems.add(needItems);
				if(!itemBiz.checkItemEnough(player, needItems)){
					player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
					return;
				}
			}
		}
		// 扣除道具
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.DriverLvUp.value(tankId))) {
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		tankBiz.driverLvUp(player, tank, lv+1);

		player.notifyObservers(ObservableEnum.CHTankDriverLvUp, tankId, lv, tank.getDriver().getLv(), costItems);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_DriverLvUp,true);
	}

	/**
	 * 车长一键升级
	 *
	 * @author yanpeng
	 * @param player
	 * @param msg
	 *
	 */
	@MsgMethod ( MessageComm.C2S_DriverLvUp_Num)
	public void driverLvUpAll(Player player, JsonMsg msg) {
		if (!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Driver)){
			player.sendErrorMsg(SysConstant.Function_Lock);
			return;
		}
		int tankId = msg.getInt("tankId");
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Driver)){
			player.sendErrorMsg(SysConstant.DRIVER_LOCK);
			return;
		}
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		int lv = tank.getDriver().getLv();
		if(lv >= tankConfig.getDriverLvMax(tankSetting.getRare())){
			player.sendErrorMsg(SysConstant.DRIVER_LV_MAX);
			return;
		}
		int upLvMax = Math.min(tank.getLv(), tankConfig.getDriverUpLvMax(tankSetting.getRare(), tank.getDriver().getEvolveLv()));
		if (lv >= upLvMax){
			player.sendErrorMsg(SysConstant.DRIVER_LV_MAX);
			return;
		}
		List<Items> costItems = Lists.newArrayList();
		int newDriverLv = tankBiz.getDriverUpLvAndCalCost(player, tank, costItems, upLvMax);
		if (newDriverLv <= lv){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		// 扣除道具
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.DriverLvUp.value(tankId))) {
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		tankBiz.driverLvUp(player, tank, newDriverLv);

		player.notifyObservers(ObservableEnum.CHTankDriverLvUp, tankId, lv, tank.getDriver().getLv(), costItems);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_DriverLvUp_Num,true);
	}



	
	@MsgMethod ( MessageComm.C2S_FriendLvUp)
	public void friendLvUp(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId");
		Tank tank = player.playerTank().getTank(tankId);
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
		if(tank == null || null==tankSetting) {
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		FettersImpl fetters = tankFettersConfig.getFettersById(tankSetting.getFetters());
		if(null==fetters) {
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankStar)){
			player.sendErrorMsg(SysConstant.FRIEND_LOCK);
			return;
		}
		if(tank.getFetters() >=fetters.getMaxLv()){
			player.sendErrorMsg(SysConstant.FRIEND_STAR_MAX);
			return;
		}
		List<Integer> tankList = fetters.getTankList();
		int totalStar = 0;
		for(int id:tankList) {
			Tank tempTank = player.playerTank().getTank(id);
			totalStar+=null==tempTank?0:tempTank.getStar();
		}
		if(totalStar<fetters.getStarNum(tank.getFetters()+1)) {
			player.sendErrorMsg(SysConstant.FRIEND_STAR_Limit);
			return;
		}
		Items costItems = fetters.getCostItem(tank.getFetters()+1);
		if(costItems.getCount() >0 && !itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.FriendLvUp.value(tankId))){
			player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
			return;
		}
		tank.addFetters();
		player.playerTank().SetChanged();
		player.notifyObservers(ObservableEnum.FriendLv, tankId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_FriendLvUp,true);
	}
	
	
	/**
	 * 查看坦克详情
	 *
	 * @author yanpeng 
	 * @param player
	 * @param msg  
	 *
	 */
	@MsgMethod ( MessageComm.C2S_TankDetail)
	public void tankDetail(Player player, JsonMsg msg) {
		int otherId = msg.getInt("otherId");//玩家id
		int tankId = msg.getInt("tankId");//坦克id
		
		Player other = PlayerUtils.getPlayer(otherId); 
		if(other == null){
			player.sendErrorMsg(SysConstant.PLAYER_NOT_EXIST);
			return; 
		}
		
		Tank tank = other.playerTank().getTank(tankId); 
		if(tank == null){
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return;
		}
		player.sendMsg(MessageComm.S2C_TankDetail,tank);
	}
	


    /**
     * 十连抽
     * @param player
     * @param msg
     */
    @MsgMethod(MessageComm.C2S_TankResearchSenior)
    public void senior(Player player, JsonMsg msg) {
        // 1:单 2: 10连
    	int type = msg.getInt("type");
		TankResearchType researchType = TankResearchType.getTankResearchType(type);
		if (researchType == null){
			player.sendErrorMsg(SysConstant.PARAM_ERROR);
			return;
		}
		List<ItemsVo> itemList = researchBiz.seniorCustom(player, researchType);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankResearchSenior, itemList);
    }

	/**
	 * 设置心愿单
	 * @param player
	 * @param msg
	 */
	@MsgMethod(MessageComm.C2S_TankResearchWish)
	public void wish(Player player, JsonMsg msg) {
		int tankId = msg.getInt("tankId");
		if (tankId > 0){
			TankSetting tankSetting = tankConfig.getTankSetting(tankId);
			if (tankSetting == null){
				player.sendErrorMsg(SysConstant.PARAM_ERROR);
				return;
			}
		}
		player.playerResearchTank().resetWishTankId(tankId);
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankResearchWish);
	}

    
    @MsgMethod(MessageComm.C2S_TankDrawLock)
    public void tankDrawLock(Player player, JsonMsg msg) {
    	int index = msg.getInt("index");
    	int type = msg.getInt("type");
    	boolean result = false;
    	//1:锁定，2：解锁
    	if(type==1) {
    		result = factoryBiz.lock(player, index);
    	}else {
    		result = factoryBiz.unLock(player, index);
    	}
    	if(result) {
    		player.sendUserUpdateMsg();
    		player.sendMsg(MessageComm.S2C_TankDrawLock, null);
    	}else {
    		player.sendErrorMsg(SysConstant.TANK_DRAW_LOCKERROR);
    	}
    }
	@MsgMethod(MessageComm.C2S_TankDrawBan)
	public void tankDrawBan(Player player, JsonMsg msg) {
		Integer type = msg.getInt("type");
		if (player.playerTankFactory().getBanRareTypeList().contains(type)) {
			player.playerTankFactory().getBanRareTypeList().remove(type);
		} else {
			player.playerTankFactory().getBanRareTypeList().add(type);
		}
		player.playerTankFactory().SetChanged();
		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_TankDrawBan);
	}

	/**
     * getTargetPlayerTank:(获取用户坦克明细信息). <br/>  
     * TODO(这里描述这个方法适用条件 – 可选).<br/>  
     * @author zxj  
     * @param player
     * @param msg  使用说明
     */
    @MsgMethod(MessageComm.C2S_TarTankMsg)
    public void getTargetPlayerTank(Player player, JsonMsg msg) {
    	int tankId = msg.getInt("tankId");
    	int tarPlayerId = msg.getInt("tarPlayerId");
    	
    	Player tarPlayer = PlayerUtils.getPlayer(tarPlayerId);
    	if(null==tarPlayer) {
    		player.sendErrorMsg(SysConstant.PLAYER_NOT_EXIST);
    		return;
    	}
    	if(!tarPlayer.playerTank().isHaveTank(tankId)) {
    		player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
    		return;
    	}
    	Map<Integer, Object> resultMap = Maps.newHashMap();
    	for(TankDetailMsg temp :TankDetailMsg.values()) {
    		resultMap.put(temp.getType(), temp.getData(tarPlayer, tankId));
    	}
    	player.sendMsg(MessageComm.S2C_TarTankMsg, resultMap);
    }
    
    /**
	 * 兽魂觉醒
	 */
	@MsgMethod ( MessageComm.C2S_DriverEvolveLvUp)
	public void driverEvolveLvUp(Player player,JsonMsg msg){
		int tankId = msg.getInt("tankId"); 
		Tank tank = player.playerTank().getTank(tankId);
		if(tank == null){
			player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
			return; 
		}

		//判断坦克进化升星的条件
		Driver driver = tank.getDriver();

		DriverTemplate driverTemplate = tankConfig.getDriverTemplate(tankId);
		int maxEvolveLv = driverTemplate.getMaxEvolveLv();
		if (driver.getEvolveLv() >= maxEvolveLv){
			player.sendErrorMsg(SysConstant.Driver_EvolveStar_Max);
			return;
		}

		DriverLvTemplate driverLvTemplate = tankConfig.getDriverLvTemplate(tank);

		if(driver.getEvolveLv() >= driverLvTemplate.getEvolve_level()){
			player.sendErrorMsg(SysConstant.Driver_Evolve_Where);
			return; 
		}
		//资源消耗
		List<Items> costItems = driverTemplate.getEvolveCost(tank.getEvolveStar()+1);
		if(!itemBiz.checkItemEnoughAndSpend(player, costItems, LogType.DriverEvolveStarUp.value(tankId))){
			player.sendErrorMsg(SysConstant.ITEMS_NOT_ENOUGH);
			return;
		}
		driver.addEvolveLv();
		player.playerTank().SetChanged();

		player.notifyObservers(ObservableEnum.DriverEvolveStarUp, tank.getId(), driver.getEvolveLv(), costItems);

		player.sendUserUpdateMsg();
		player.sendMsg(MessageComm.S2C_DriverEvolveLvUp);
	}

    @MsgMethod(MessageComm.C2S_TarTank)
    public void getTargetTank(Player player, JsonMsg msg) {
        int tankId = msg.getInt("tankId");
        long tarPlayerId = msg.getLong("tarPlayerId");

        Player tarPlayer = PlayerUtils.getPlayer(tarPlayerId);
        if (null == tarPlayer) {
            player.sendErrorMsg(SysConstant.PLAYER_NOT_EXIST);
            return;
        }
        if (!tarPlayer.playerTank().isHaveTank(tankId)) {
            player.sendErrorMsg(SysConstant.TANK_NOT_EXIST);
            return;
        }
        Map<Long, Map<Integer, Object>> playerId2Tank = Maps.newHashMap();

        Map<Integer, Object> tankMsg = tankBiz.getTankMsg(tarPlayer, tankId);
        PlayerRedisData redisData = RedisUtil.getPlayerRedisData(tarPlayerId);
        tankMsg.put(-1, redisData);
        playerId2Tank.put(tarPlayerId, tankMsg);

        if (player.playerTank().isHaveTank(tankId)) {
            Map<Integer, Object> selfTankMsg = tankBiz.getTankMsg(player, tankId);
            playerId2Tank.put(player.getId(), selfTankMsg);
        }
        player.sendMsg(MessageComm.S2C_TarTank, playerId2Tank);
    }
}
  







