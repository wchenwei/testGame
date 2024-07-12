package com.hm.action.tank.biz;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.TankSpecialityConfig;
import com.hm.config.excel.templaextra.TankSpecNameTemplateImpl;
import com.hm.config.excel.templaextra.TankSpecTempImpl;
import com.hm.enums.LogType;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.sysConstant.SysConstant;

import javax.annotation.Resource;
import java.util.List;
/**
 * ClassName: TankSpecialBiz. <br/>  
 * Function: 坦克专精biz. <br/>  
 * date: 2019年7月15日 下午6:09:08 <br/>  
 * @author zxj  
 * @version
 */
@Biz
public class TankSpecialBiz {

	@Resource
	private TankSpecialityConfig tankSpecConfig;
	@Resource
	private ItemBiz itemBiz;
	
	/**
	 * getSpecialAttr:(获取坦克专精属性加成). <br/>  
	 * @author zxj  
	 * @param tank
	 * @return  使用说明
	 */
	public TankAttr getSpecialAttr(Tank tank) {
		if(tank.getTankSpecial().getType()>0) {
			//开启专精后的初始化加成
			TankSpecNameTemplateImpl specName = tankSpecConfig.getTankSpecNameById(tank.getTankSpecial().getType());
			//专精升级后的加成
			TankSpecTempImpl tankSpec = tankSpecConfig.getTankSpecByTree(tank.getTankSpecial().getType(), tank.getTankSpecial().getLv());
			TankAttr tankAttr = new TankAttr();
			tankAttr.addAttr(null==tankSpec?null:tankSpec.getTankAttr());
			tankAttr.addAttr(null==specName?null:specName.getTankAttr());
			return tankAttr;
		}
		return null;
	}
	/**
	 * getSpecialSkill:(获取坦克专精，节能列表). <br/>  
	 * @author zxj  
	 * @param tank
	 * @return  使用说明
	 */
	public List<Integer> getSpecialSkill(Tank tank) {
		TankSpecTempImpl tankSpec = tankSpecConfig.getTankSpecByTree(tank.getTankSpecial().getType(), tank.getTankSpecial().getLv());
		return null==tankSpec?Lists.newArrayList():tankSpec.getSkillList();
	}

	public boolean lvUpdate(Player player, Tank tank, int upType) {
		int lv = tank.getTankSpecial().getLv();
		int type = tank.getTankSpecial().getType();
		int endLv = calEndLv(lv, upType);
		for (int i = lv+1; i <= endLv; i++) {
			TankSpecTempImpl nextTankSpec = tankSpecConfig.getTankSpecByTree(type, i);
			if (nextTankSpec == null){
				break;
			}
			if(!itemBiz.checkItemEnoughAndSpend(player, nextTankSpec.getCostList(), LogType.TankLvCost.value(tank.getId()+":"+i))) {
				if (i == lv+1){//首次升级就不足
					player.sendErrorMsg(SysConstant.PLAYER_RE_NOT);
					return false;
				}
				break;
			}
			tank.getTankSpecial().lvUpdate();
		}
		return true;
	}

	private int calEndLv(int lv, int upType) {
		if (upType <=0){
			return lv+1;
		}else {
			return (lv+10)/10*10;
		}
	}
}
