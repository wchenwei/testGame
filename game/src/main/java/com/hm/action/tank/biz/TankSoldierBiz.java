package com.hm.action.tank.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.config.TankSoldierConfig;
import com.hm.config.excel.templaextra.TankSoldierCircleExtraTemplate;
import com.hm.enums.TankAttrType;
import com.hm.enums.TankRecastType;
import com.hm.enums.TankSoldierHalosType;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.model.tank.TankSoldier;
import com.hm.observer.IObserver;
import com.hm.observer.ObservableEnum;
import com.hm.observer.ObserverRouter;
import com.hm.util.MathUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
/**
 * @Date 2020年7月7日09:42:28
 * @author xjt
 *
 */
@Biz
public class TankSoldierBiz implements IObserver{
	@Resource
	private TankSoldierConfig tankSoldierConfig;
	
	public void bind(Player player,int tankId,int bindId){
		Tank tank = player.playerTank().getTank(tankId);
		Tank bindTank = player.playerTank().getTank(bindId);
		if(tank==null||bindTank==null){
			return;
		}
		tank.bindTank(bindId);
		//重新计算光环
		calSoldierHalos(player, tank, bindTank);
		bindTank.bindMainTank(tankId);
		player.playerTank().SetChanged();
	}
	/**
	 * 重新计算光环
	 * @param tank
	 * @param bindTank
	 * @return 光环是否发生变化
	 */
	public boolean calSoldierHalos(Player player,Tank tank,Tank bindTank){
		TankSoldier tankSoldier = tank.getTankSoldier();
		Map<Integer,Integer> halos = Maps.newConcurrentMap();
		if(tankSoldier==null) {
			return false;
		}
		boolean flag = false;
		for(TankSoldierHalosType type:TankSoldierHalosType.values()){
			int oldLv = tankSoldier.getHalos(type.getType());
			int newLv = getHalosLv(tank,bindTank,type);
			if(newLv!=oldLv){
				halos.put(type.getType(), newLv);
				if(newLv==0){
					halos.remove(type.getType());
				}
				flag = true;
			}
		}
		if(flag) {
			tankSoldier.changeHalos(halos);
		}
		return flag;
	}
	//获取激活的某个类型的光环等级
	public int getHalosLv(Tank tank,Tank bindTank,TankSoldierHalosType type){
		int maxLv = tankSoldierConfig.getMaxCricleLv(type.getType());
		for(int circleLv=maxLv;circleLv>0;circleLv--){
			//根据配置获取共鸣等级对应的所需装备强化等级
			TankSoldierCircleExtraTemplate template = tankSoldierConfig.getCircle(type.getType(), circleLv);
			int needLv = template.getRequest_case();
			//所有等级必须>=needLv
			if(tank.getLv(type)>=needLv&&bindTank.getLv(type)>=needLv){
				return circleLv;
			}
		}
		return 0;
	}
	public void unBind(Player player, int tankId) {
		Tank tank = player.playerTank().getTank(tankId);
		TankSoldier tankSoldier = tank.getTankSoldier();
		if(tankSoldier==null) {
			return;
		}
		Tank bindTank = player.playerTank().getTank(tankSoldier.getSubTankId());
		if(bindTank==null){
			return;
		}
		//主坦克和奇兵解绑
		tank.getTankSoldier().unBind();
		//奇兵解绑
		bindTank.unBindMainTank();
		player.playerTank().SetChanged();
	}
	public TankAttr getTankSoldierAddAttr(Player player, Tank tank) {
		TankAttr tankAttr = new TankAttr(); 
		if(tank.getTankSoldier()==null) {
			return tankAttr;
		}
		int subTankId = tank.getTankSoldier().getSubTankId();
		if(subTankId<=0){
			return tankAttr;
		}
		Tank subTank = player.playerTank().getTank(subTankId);
		//增加奇兵提供的属性加成
		double rate = tankSoldierConfig.getAddRate(tank.getTankSoldier().getLv());
		tankAttr.addAttr(TankAttrType.ATK, MathUtils.mul(subTank.getTotalAttr(TankAttrType.ATK), rate));
		tankAttr.addAttr(TankAttrType.DEF, MathUtils.mul(subTank.getTotalAttr(TankAttrType.DEF), rate));
		tankAttr.addAttr(TankAttrType.HP, MathUtils.mul(subTank.getTotalAttr(TankAttrType.HP), rate));
		//增加奇兵提供的光环属性
		tankAttr.addAttr(tankSoldierConfig.getAddRate(tank.getTankSoldier().getHalos()));
		return tankAttr;
	}
	/**
	 * 重新计算tank 对应的主战坦克的奇兵光环
	 * @param player
	 * @param tank
	 */
	public void calMainTankSoldierCricle(Player player,int tankId){
		Tank tank = player.playerTank().getTank(tankId);
		if(tank==null){
			return;
		}
		
		Tank mainTank = null;
		Tank subTank = null;
		if(tank.getMainTank()>0) {
			mainTank = player.playerTank().getTank(tank.getMainTank());
			subTank = tank;
		}
		if(tank.getTankSoldier()!=null&&tank.getTankSoldier().getSubTankId()>0) {
			mainTank = tank;
			subTank = player.playerTank().getTank(tank.getTankSoldier().getSubTankId());
		}
		if(mainTank==null||subTank==null) {
			return;
		}
		boolean flag = calSoldierHalos(player, mainTank, subTank);
		if(flag){
			player.playerTank().SetChanged();
		}
	}
	@Override
	public void registObserverEnum() {
		ObserverRouter.getInstance().registObserver(ObservableEnum.MagicReformTransfer, this,1);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MagicReform, this,1);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankEvolveStarUp, this,1);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankTechUpdate, this,1);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankRecast, this,1);
		ObserverRouter.getInstance().registObserver(ObservableEnum.TankSpecialLvUp, this,1);
		ObserverRouter.getInstance().registObserver(ObservableEnum.MagicReformRest, this,1);
	}
	@Override
	public void invoke(ObservableEnum observableEnum, Player player,
			Object... argv) {
		List<Integer> tankIds = Lists.newArrayList();
		switch(observableEnum){
			case MagicReformTransfer:
			case MagicReformRest:
				tankIds.addAll((List<Integer>)argv[0]);
				break;
			case TankEvolveStarUp:
			case TankTechUpdate:
			case MagicReform:
			case TankSpecialLvUp:
			case TankRecast:
				tankIds.add((int)(argv[0]));
				break;
		}
		if(!CollUtil.isEmpty(tankIds)){
			tankIds.forEach(id ->calMainTankSoldierCricle(player, id));
		}
	}

}
