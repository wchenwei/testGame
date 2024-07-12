package com.hm.action.tank.biz;

import com.hm.libcore.annotation.Biz;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankDriverAdvanceConfig;
import com.hm.config.excel.TankFettersConfig;
import com.hm.config.excel.templaextra.DriverAdvanceTemplateImpl;
import com.hm.enums.PlayerFunctionType;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.sysConstant.SysConstant;
import com.hm.war.sg.setting.TankSetting;

import javax.annotation.Resource;
import java.util.List;

/**
 * 
 * @author zxj
 * date:2020年3月4日10:26:25
 * desc:坦克车长的，军职biz 
 */
@Biz
public class TankDriverAdvanceBiz{
	
	@Resource
	private TankDriverAdvanceConfig advanceConfig;
	@Resource
	private TankFettersConfig tankFettersConfig;
	@Resource
	private TankConfig tankConfig;

	//升级校验
	public int check(Player player, Tank tank, int chooseId) {
		if(null==tank) {
			return SysConstant.PARAM_ERROR;
		}
		int maxLv = tank.getDriver().getAdvanceMaxLv();
		DriverAdvanceTemplateImpl advance = advanceConfig.getTannkAdvance(maxLv);
		DriverAdvanceTemplateImpl nexAdv = advanceConfig.getTannkAdvance(maxLv+1);
		TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
		if(null==advance || !advance.contains(chooseId, tankSetting.getType(), tankSetting.getRare())
				|| null==nexAdv || tank.getDriver().getLv()<advance.getDriver_level()) {
			return SysConstant.PARAM_ERROR;
		}
		return 0;
	}
	//后去坦克，基础属性加成
	public TankAttr getDriverAdAttr(Player player, Tank tank) {
		TankAttr tankAttr = new TankAttr();
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankDriverAd)){
			return tankAttr; 
		}
		int maxLv = tank.getDriver().getAdvanceMaxLv();
		if(maxLv >0){
			//基础加成
			DriverAdvanceTemplateImpl advance = advanceConfig.getTannkAdvance(maxLv);
			TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
			tankAttr.addAttr(advance.getAttr(tankSetting.getRare()));
		}
		tank.getDriver().getAdvance().values().forEach(e->{
			if(e>0) {
				tankAttr.addAttr(advanceConfig.getTannkAdvanceShow(e).getAttr());
			}
		});
		return tankAttr; 
	}
	//后去坦克，车长羁绊属性加成
	public TankAttr getDriverAdFetterAttr(Player player, Tank tank) {
		TankAttr tankAttr = new TankAttr();
		if(!player.getPlayerFunction().isOpenFunction(PlayerFunctionType.TankDriverAd)){
			return tankAttr; 
		}
		int minLv = this.getMinLv(player, tank.getId());
		if(minLv >0){
			return advanceConfig.getAttrFetterAttr(minLv);
		}
		return tankAttr; 
	}
	//获取军职最小等级
	public int getMinLv(Player player, int tankId) {
		try {
			List<Integer> fetterTankIds = tankFettersConfig.getTankFetter(tankId);
			if(fetterTankIds.size() >= 2) {
				return fetterTankIds.stream().mapToInt(e->getAdvLv(player, e)).min().orElse(0);
			}
		} catch (Exception e) {
		}
		return 0;
	}
	
	//获取当前坦克的最大军职等级
	private int getAdvLv(Player player, int tankId) {
		Tank tank = player.playerTank().getTank(tankId);
		if(null==tank) {
			return 0;
		}
		return tank.getDriver().getAdvanceMaxLv();
	}
}



