package com.hm.action.mastery.biz;

import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.config.MasteryConfig;
import com.hm.enums.PlayerFunctionType;
import com.hm.enums.TankAttrType;
import com.hm.model.player.BasePlayer;
import com.hm.model.player.Mastery;
import com.hm.model.player.Player;
import com.hm.model.tank.TankAttr;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;

import javax.annotation.Resource;
import java.util.Map;

@Biz
public class MasteryBiz implements IObserver{
	@Resource
	private MasteryConfig masteryConfig;

	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.FunctionUnlock, this);
	}

	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		int functionId = Integer.parseInt(argv[0].toString());
		if(functionId==PlayerFunctionType.Mastery.getType()){
			player.playerMastery().init();
		}
	}

	public Map<Integer,TankAttr> calAttr(Player player) {
		Map<Integer,TankAttr> map = Maps.newConcurrentMap();
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Mastery)){
			return map;
		}
		for(Map.Entry<Integer, Map<TankAttrType,Double>> entry:calMasteryAttr(player).entrySet()){
			TankAttr tankAttr = new TankAttr();
			tankAttr.addAttr(entry.getValue());
			map.put(entry.getKey(), tankAttr);
		} 
		//专精属性加成
		return map;
	}
	/**
	 * 计算专精加成
	 *
	 * @author xjt 
	 * @param player
	 * @return  
	 *
	 */
	public Map<Integer, Map<TankAttrType,Double>> calMasteryAttr(Player player){
		Map<Integer, Map<TankAttrType,Double>> attrMap = Maps.newConcurrentMap();
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.Mastery)){
			return attrMap; 
		}
		for(int i = 1;i<=player.playerMastery().getMasterys().length;i++){
			Mastery mastery = player.playerMastery().getMastery(i);
			Map<TankAttrType,Double> masteryAttrMap = Maps.newConcurrentMap();
			//精炼本身的属性加成
			for(int j=1;j<= mastery.getLvs().length;j++){
				//当前位置属性加成
				Map<TankAttrType,Double> nowAttrMap = masteryConfig.getMasteryAttrAdd(mastery.getType(), j, mastery.getLv(j));
				nowAttrMap.forEach((key,value)->masteryAttrMap.merge(key, value, (x,y)->(x+y)));
			}
			//光环属性
			Map<Integer,Integer> circleMap = mastery.getCricleMap();
			circleMap.forEach((id,lv)->{
				if(lv>0){
					Map<TankAttrType,Double> nowCircleAttrMap = masteryConfig.getMasteryCricleAttr(mastery.getType(), id, lv);
					nowCircleAttrMap.forEach((key,value)->masteryAttrMap.merge(key, value, (x,y)->(x+y)));
				}
			});
			attrMap.put(mastery.getType(), masteryAttrMap);
		}
		return attrMap;
	}

	/**
	 * 检查光环是否发生变化
	 * @param player
	 */
	public void checkMasterCircle(BasePlayer player){
		if (player.playerMastery().checkIsChanged()){
			player.notifyObservers(ObservableEnum.MasteryLvUp, 0);
		}
	}

}
