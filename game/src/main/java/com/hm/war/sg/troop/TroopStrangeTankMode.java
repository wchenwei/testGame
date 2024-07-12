package com.hm.war.sg.troop;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankVo;

import java.util.List;
import java.util.Map;

/**
 * @Description: 部队奇兵系统
 * @author siyunlong  
 * @date 2020年7月3日 上午10:10:03 
 * @version V1.0
 */
public class TroopStrangeTankMode {
	private List<StrangeTank> strangeTankList = Lists.newArrayList();
	private int randomIndex;

	public TroopStrangeTankMode(Player player,Map<Integer,Integer> strangeTankMap,List<Integer> tankIdsList) {
		super();
		for (int tankId : tankIdsList) {
			if(strangeTankMap.containsKey(tankId)) {
				int luckTankId = strangeTankMap.get(tankId);
				Tank tank = player.playerTank().getTank(luckTankId);
				if(tank != null) {
					this.strangeTankList.add(new StrangeTank(tank.createTankVo()));
				}else{
					this.strangeTankList.add(new StrangeTank());
				}
			}else{
				this.strangeTankList.add(new StrangeTank());
			}
		}
		buildIdleTank();
	}
	
	public TroopStrangeTankMode(List<TankVo> strangeTankList) {
		super();
		buildIdleTank();
	}
	
	public void buildIdleTank() {
		int MaxSize = 5;
		int size = this.strangeTankList.size();
		for (int i = 0; i < MaxSize-size; i++) {
			this.strangeTankList.add(new StrangeTank());
		}
		this.randomIndex = RandomUtil.randomInt(this.strangeTankList.size());
	}
	
	public int getLuckTankId() {
		return this.strangeTankList.get(randomIndex).getTankId();
	}
	
	
}
