package com.hm.action.shop.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.GameConstants;
import com.hm.config.MysteryShopConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.ShopConfig;
import com.hm.enums.CommonValueType;
import com.hm.enums.ShopType;
import com.hm.model.player.Player;
import com.hm.model.shop.LevelShop;
import com.hm.model.shop.PlayerShopValue;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Biz
public class ShopBiz implements IObserver{
	@Resource
	private ShopConfig shopConfig;
	@Resource
	private CommValueConfig commValueConfig;
	@Resource
	private MysteryShopConfig mysteryShopConfig;

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.PlayerLevelUp, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.ArsenalSweep, this);
		ObserverRouter.getInstance().registObserver(ObservableEnum.RandomTaskComplete, this);
		
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		switch(observableEnum){
			case PlayerLevelUp:
				checkShopOpen(player);
				checkLevelShop(player);
				break;
			case ArsenalSweep:
			case RandomTaskComplete:
				//扫荡兵工厂一次进度加4，完成民情事件一次进度+30
				int mysteryShopSweepScore = commValueConfig.getCommValue(CommonValueType.MysteryShopSweepScore);
				int mysteryShopTaskScore = commValueConfig.getCommValue(CommonValueType.MysteryShopTaskScore);
				int progress = observableEnum== ObservableEnum.ArsenalSweep?Integer.parseInt(argv[0].toString())*mysteryShopSweepScore:mysteryShopTaskScore;
				checkPlayerMysteryShop(player,progress);
				break;
		}
		
	}
	private void checkPlayerMysteryShop(Player player,int progress) {
		if(player.playerMission().getOpenCity()<GameConstants.PeaceId){
			return;
		}
		int shopLimit = commValueConfig.getCommValue(CommonValueType.MysteryShopLimit);
		//3次以后不再触发
		if(player.playerMysteryShop().getCount()>=shopLimit){
			return;
		}
		player.playerMysteryShop().addProgress(progress);
		//上个商店在有效期内不再触发
		if(player.playerMysteryShop().getEndTime()>System.currentTimeMillis()){
			return;
		}
		if(player.playerMysteryShop().getProgress()>100){
			//根据商品生成规则生成商店物品
			player.playerMysteryShop().createGoods(mysteryShopConfig.createShop(player));
		}
	}

	private void checkLevelShop(Player player) {
		String str = commValueConfig.getStrValue(CommonValueType.LevelShopContinueDay);
		int playerLevel = player.playerLevel().getLv();
		PlayerShopValue levelShop = player.playerShop().getPlayerShop(ShopType.LevelShop.getType());
		if(levelShop==null){
			return;
		}
		LevelShop shop = (LevelShop)levelShop;
		//满足条件的
		List<String> filterStrs = StringUtil.splitStr2StrList(str, ",").stream().filter(s ->{
			int lv = Integer.parseInt(s.split(":")[0]);
			return playerLevel>=lv&&lv>shop.getLv();
		}).collect(Collectors.toList());
		if(filterStrs.isEmpty()){
			return;
		}
		String filterStr = filterStrs.get(filterStrs.size()-1);
		levelShop.unlockRepeat(player,filterStr);
		levelShop.checkReset(player);
		player.playerShop().SetChanged();
	}

	public void checkShopOpen(Player player) {
		try {
			shopConfig.checkShopOpen(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
