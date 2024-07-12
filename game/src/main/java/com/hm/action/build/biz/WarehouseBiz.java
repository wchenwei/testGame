package com.hm.action.build.biz;/*package com.hm.action.build.biz;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.libcore.msg.JsonMsg;
import com.hm.action.item.ItemBiz;
import com.hm.action.player.PlayerBiz;
import com.hm.action.world.biz.WorldBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.BuffType;
import com.hm.enums.BuildType;
import com.hm.enums.CommonValueType;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.enums.PlayerAssetEnum;
import com.hm.enums.ReBuildType;
import com.hm.message.MessageComm;
import com.hm.model.baseinterface.warehouse.ReBuildHouse;
import com.hm.model.baseinterface.warehouse.Warehouse;
import com.hm.model.item.Items;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Player;
import com.hm.model.player.CurrencyKind;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.sysConstant.NumericalConstant;
import com.hm.util.MathUtils;

@Biz
public class WarehouseBiz implements IObserver{
	@Resource
	private PlayerBiz playerBiz;
	@Resource
	private WorldBiz worldBiz;
	@Resource
	private BuildBiz buildBiz;
	@Resource
	private MaterialBiz materialBiz;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private ItemBiz itemBiz;
	*//**
	 * 侦查掠夺量
	 * @param attackPlayer
	 * @return
	 *//*
	public Map<Integer,Long> sponPlunder(Player beAttackPlayer){
		long protect = beAttackPlayer.warehouse().getProtectRe();
		Map<Integer,Long> resMap = Maps.newConcurrentMap();//侦查资源结果
		resMap.put(CurrencyKind.Elec.getIndex(), Math.max(beAttackPlayer.playerCurrency().get(CurrencyKind.Elec)-protect,0));
		resMap.put(CurrencyKind.Oil.getIndex(), Math.max(beAttackPlayer.playerCurrency().get(CurrencyKind.Oil)-protect,0));
		resMap.put(CurrencyKind.PaperMoney.getIndex(), Math.max(beAttackPlayer.playerCurrency().get(CurrencyKind.PaperMoney)-protect,0));
		resMap.put(CurrencyKind.PEOPLE.getIndex(), Math.max((long)beAttackPlayer.playerCurrency().get(CurrencyKind.PEOPLE),0));
		resMap.put(CurrencyKind.Gold.getIndex(), Math.max(beAttackPlayer.playerCurrency().get(CurrencyKind.Gold),0));
		return resMap;
	}
	*//**
	 * 实际夺量
	 * @param
	 * @return
	 *//*
	public Map<Integer,Long> plunderRes(Player attackPlayer,Player beAttackPlayer){
		Map<Integer,Long> map = Maps.newConcurrentMap();
		double peoplePlunder = MathUtils.div(commValueConfig.getCommValue(CommonValueType.PeoplePlunder), 100) ;//人口掠夺系数
		long protect = beAttackPlayer.warehouse().getProtectRe();
		Map<Integer,Long> haveRes = Maps.newConcurrentMap();//被攻击玩家资源拥有量
		haveRes.put(CurrencyKind.Elec.getIndex(), beAttackPlayer.playerCurrency().get(CurrencyKind.Elec));
		haveRes.put(CurrencyKind.Oil.getIndex(), beAttackPlayer.playerCurrency().get(CurrencyKind.Oil));
		haveRes.put(CurrencyKind.PaperMoney.getIndex(), beAttackPlayer.playerCurrency().get(CurrencyKind.PaperMoney));

		// 刷新一下当前玩家的人数  只计算离线玩家
		if (!beAttackPlayer.isOnline()) {
            materialBiz.calcOfflinePeople(beAttackPlayer);
        }
		haveRes.put(CurrencyKind.PEOPLE.getIndex(), (long) (beAttackPlayer.playerCurrency().get(CurrencyKind.PEOPLE)));
		Warehouse attackPlayerWarehouse = attackPlayer.warehouse();//攻击者仓库
		Warehouse beAttackPlayerWarehouse = beAttackPlayer.warehouse();//被攻击者仓库
		for(Map.Entry<Integer, Long> entry: haveRes.entrySet()){
			//损失资源量 = （资源量 - 保护资源量）*掠夺系数
			//掠夺系数=Math.max(0,Math.min((0.3+（掠夺者仓库lv - 被掠夺仓库lv）*0.04)))即0<=公式内<=1
			//损失人口=
			long bePlunderRes = 0;
			if(entry.getKey()==CurrencyKind.PEOPLE.getIndex()){
				bePlunderRes = (long) (entry.getValue()*peoplePlunder);
			}else if(entry.getValue()>protect){
				bePlunderRes = (long) ((entry.getValue()-protect)*Math.max(0,Math.min(1, (0.3+((attackPlayerWarehouse.getLv()-beAttackPlayerWarehouse.getLv())*0.04)))));
			}
			map.put(entry.getKey(), bePlunderRes);
		}
		return map;
	}
	
	*//**
	 * 掠夺资源(被攻击玩家损失资源)
	 * @param player
	 * @param beAttrPlayer
	 * @param load
	 * @return 玩家被掠夺量   （掠夺量!=被掠夺量）
	 *//*
	public Map<Integer,Long> plunder(Player player,Player beAttrPlayer){
		Map<Integer,Long> lossMap = plunderRes(player,beAttrPlayer);//玩家损失资源
		
		bePlunder(beAttrPlayer, lossMap);
		return lossMap;
	}
	*//**
	 * 被掠夺
	 * @param player
	 * @param map
	 *//*
	private void bePlunder(Player player,Map<Integer,Long> map){
		double peoplePlunder = MathUtils.div(commValueConfig.getCommValue(CommonValueType.PeoplePlunder), 100) ;//人口掠夺系数
		double peopleLoss = MathUtils.div(commValueConfig.getCommValue(CommonValueType.PeopleLoss), 100) ;//人口丢失系数
		for(Map.Entry<Integer, Long> entry: map.entrySet()){
			long res = entry.getValue();//掠夺量
			CurrencyKind kind = CurrencyKind.getCurrencyByIndex(entry.getKey());
			if(kind == CurrencyKind.PEOPLE){
				res = (long) MathUtils.mul(MathUtils.div(res,peoplePlunder),peopleLoss);//人口丢失量=人口被掠夺量/人口掠夺系数*人口丢失系数
			}
			playerBiz.spendPlayerCurrency(player, kind, res, LogType.Rob_Gather);
		}
	}
	*//**
	 *从被掠夺量换算掠夺量 ,人口掠夺量和人口损失量计算公式不一致,获得资源最高为自己仓库的保护量
	 *//*
	public List<Items> lossToplunder(List<Items> items,BasePlayer attackPlayer){
		double peoplePlunder = MathUtils.div(commValueConfig.getCommValue(CommonValueType.PeoplePlunder), 100) ;//人口掠夺系数
		double peopleLoss = MathUtils.div(commValueConfig.getCommValue(CommonValueType.PeopleLoss), 100) ;//人口丢失系数
		double resPlunder = MathUtils.div(commValueConfig.getCommValue(CommonValueType.ResPlunder), 100) ;//资源掠夺系数
		long pro = attackPlayer.warehouse().getProtectRe();
		List<Items> returnItems = Lists.newArrayList();
		for(Items item:items){
			Items temp = item.clone();
			CurrencyKind kind = PlayerAssetEnum.getCurrencyByPlayerAssetEnum(temp.getId());
			if(kind==CurrencyKind.PEOPLE){
				temp.setCount((long)MathUtils.mul(MathUtils.div(temp.getCount(),peopleLoss),peoplePlunder));
			}else{
				temp.setCount(Math.min(MathUtils.mul(temp.getCount(),resPlunder),pro));
			}
			returnItems.add(temp);
		}
		return returnItems;
	}
	
	*//**
	 *从被掠夺量换算掠夺量 ,人口掠夺量和人口损失量计算公式不一致,获得资源最高为自己仓库的保护量
	 *//*
	public Map<Integer,Long> lossToplunder(Map<Integer,Long> bePlunderMap,BasePlayer attackPlayer){
		Map<Integer,Long> getRes = Maps.newConcurrentMap();
		double peoplePlunder = MathUtils.div(commValueConfig.getCommValue(CommonValueType.PeoplePlunder), 100) ;//人口掠夺系数
		double peopleLoss = MathUtils.div(commValueConfig.getCommValue(CommonValueType.PeopleLoss), 100) ;//人口丢失系数
		double resPlunder = MathUtils.div(commValueConfig.getCommValue(CommonValueType.ResPlunder), 100) ;//资源掠夺系数
		long pro = attackPlayer.warehouse().getProtectRe();
		for(Map.Entry<Integer, Long> entry: bePlunderMap.entrySet()){
			int currencyId = entry.getKey();
			if(currencyId == CurrencyKind.PEOPLE.getIndex()){
				getRes.put(currencyId, (long)MathUtils.mul(MathUtils.div(entry.getValue(),peopleLoss),peoplePlunder));
			}else{
				getRes.put(currencyId, Math.min(MathUtils.mul(entry.getValue(),resPlunder),pro));
			}
		}
		return getRes;
	}
	
	
	*//**
	 * 玩家被击飞后调用此方法
	 * @param player
	 * @param bePlunderRes
	 *//*
	public void rebuildHouse(Player player,List<Items> items ){
		JsonMsg msg = new JsonMsg(MessageComm.S2C_ReBuild);
		Warehouse warehouse = player.warehouse();
		boolean isSeniorRebuild = false;
		if(warehouse.getSeniorRebuildCount()>0){//gaoji
			warehouse.reduceSeniorRebuildCount();
			player.playerBuffer().addBuff(BuffType.PROTECT, NumericalConstant.Senior_Rebuild_Protect_Second);
			worldBiz.broadWorldInfoChange(player);
		    isSeniorRebuild = true; 
		}else{
			player.playerBuffer().addBuff(BuffType.PROTECT, NumericalConstant.Rebuild_Protect_Second);
		    worldBiz.broadWorldInfoChange(player);
		}
		ReBuildType reBuildType = isSeniorRebuild?ReBuildType.Senior:ReBuildType.Normal;
		List<Items> returnItems = getReBuildRes(items, isSeniorRebuild);
		//需要在getReBuildRes方法后添加自动补防，不然还得去掉
		if(isSeniorRebuild&&player.getFortress().getLv()>0)returnItems.add(new Items(61006,player.getFortress().getLv(),ItemType.ITEM.getType()));//自动补防,单独添加
		ReBuildHouse reBuildHouse = warehouse.getReBuildHouse();//上次重建信息
		if(player.isOnline()){//在线直接通知
			msg.addProperty("rewards", returnItems);
			msg.addProperty("reBuildType", reBuildType.getType());
			player.sendMsg(msg);
		}else{
			if(reBuildHouse!=null){
				if(!warehouse.getReBuildHouse().isReceive()){//如果上一次的还没有领直接发放
					itemBiz.addItem(player, returnItems, LogType.ReBuildHouse);
				}
			}
		}
		player.warehouse().reBuild(reBuildType,returnItems);
	}
	*//**
	 * 重建家园资源返还
	 * @param bePlunderRes
	 * @param isSeniorRebuild 是否为高级重建
	 * @return
	 *//*
	public Map<CurrencyKind,Long> getReBuildRes(Map<Integer,Long> bePlunderRes,boolean isSeniorRebuild){
		Map<CurrencyKind,Long> resMap = Maps.newConcurrentMap();
		for(Map.Entry<Integer, Long> entry: bePlunderRes.entrySet()){
			double proportion  = isSeniorRebuild?1:0.5;//高级重建返回资源比例100%;普通重建返还50%
			long res = (long) (entry.getValue()<10000?10000:entry.getValue()*proportion);
			resMap.put(CurrencyKind.getCurrencyByIndex(entry.getKey()), res);
		}
		return resMap;
	}
	*//**
	 * 重建家园资源返还
	 * @param bePlunderRes
	 * @param isSeniorRebuild 是否为高级重建
	 * @return
	 *//*
	public Map<CurrencyKind,Long> getReBuildRes(List<Items> items,boolean isSeniorRebuild){
		Map<CurrencyKind,Long> resMap = Maps.newConcurrentMap();
		for(int i=items.size()-1;i>=0;i--){
			Items item = items.get(i);
			CurrencyKind kind = PlayerAssetEnum.getCurrencyByPlayerAssetEnum(item.getId());
			if(kind != CurrencyKind.PEOPLE||kind==null){
				long res = (long) (item.getCount()<10000?10000:item.getCount());//奖励数量=被掠夺数量；如果被掠夺数量<10000，则奖励10000
				if(!isSeniorRebuild){//奖励数量=被掠夺数量x50%；如果被掠夺数量x50%<10000，则奖励10000
					res = (long) (item.getCount()*0.5<10000?10000:item.getCount()*0.5);
				}
				resMap.put(kind, res);
			}
			
		}
		return resMap;
	}
	
	*//**
	 * 重建家园资源返还
	 * @param bePlunderRes
	 * @param isSeniorRebuild 是否为高级重建
	 * @return
	 *//*
	public List<Items> getReBuildRes(List<Items> items,boolean isSeniorRebuild){
		List<Items> returnItems = Lists.newArrayList();
		for(int i=items.size()-1;i>=0;i--){
			Items item = items.get(i).clone();
			CurrencyKind kind = PlayerAssetEnum.getCurrencyByPlayerAssetEnum(item.getId());
			if(kind != CurrencyKind.PEOPLE){
				long res = (long) (item.getCount()<10000?10000:item.getCount());//奖励数量=被掠夺数量；如果被掠夺数量<10000，则奖励10000
				if(!isSeniorRebuild){//奖励数量=被掠夺数量x50%；如果被掠夺数量x50%<10000，则奖励10000
					res = (long) (item.getCount()*0.5<10000?10000:item.getCount()*0.5);
				}
				item.setCount(res);
				returnItems.add(item);
			}
		}
		return returnItems;
	}
	
	//领取家园重建物资
	public void receiveReBuildRes(Player player) {
		ReBuildHouse reBuildHouse = player.warehouse().getReBuildHouse();
		if(reBuildHouse==null){
			return;
		}
		itemBiz.addItem(player, reBuildHouse.getRewards(), LogType.ReBuildHouse);
		player.warehouse().receiveReBuild();
	}
		
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.BuildLevelUp, this);		
	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		if(buildBiz.isBuildLvUp(observableEnum, player, argv, BuildType.Warehouse)){//如果是仓库升级
			player.warehouse().addSeniorRebuildCount();
			player.sendUserUpdateMsg();
		}
	}
	
	
	
	
}
*/