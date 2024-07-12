package com.hm.model.tank;

import com.google.common.collect.Maps;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.TankSoldierConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
/**
 * @Date 2020年7月7日09:42:28
 * @author xjt
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TankSoldier {
	private int lv;//奇兵等级
	private int subTankId;//对应奇兵坦克id
	//光环
	private Map<Integer,Integer> halos = Maps.newConcurrentMap();
	public void bindTank(int bindId) {
		this.subTankId = bindId;
	}
	public int getHalos(int type) {
		return halos.getOrDefault(type, 0);
	}
	public void changeHalos(Map<Integer, Integer> halos) {
		this.halos=halos;
	}
	public void unBind() {
		this.subTankId = 0;
		this.halos.clear();
	}
	public void lvUp() {
		TankSoldierConfig tankSoldierConfig = SpringUtil.getBean(TankSoldierConfig.class);
		int maxLv = tankSoldierConfig.getMaxLv();
		this.lv = Math.min(this.lv+1, maxLv);
	}
	
}
