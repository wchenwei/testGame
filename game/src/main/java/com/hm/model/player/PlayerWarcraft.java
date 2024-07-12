package com.hm.model.player;

import com.google.common.collect.Maps;
import com.hm.libcore.msg.JsonMsg;

import java.util.Map;

/**  
 * ClassName: PlayerFix. <br/>  
 * Function: 玩家兵法信息. <br/>  
 * date: 2020年2月4日 上午10:01:13 <br/>  
 *  
 * @author zxj  
 * @version   
 */
public class PlayerWarcraft extends PlayerDataContext{
	//兵法的仅能等级(兵法id, 技能等级)
	private Map<Integer, Integer> skillMap = Maps.newConcurrentMap();

	//当前兵法的等级
	private int lv = 0;
	
	public Map<Integer, Integer> getSkillMap() {
		return skillMap;
	}
	public int getLv() {
		return lv;
	}
	public int getSkillLv(int type) {
		return skillMap.getOrDefault(type, 0);
	}

	public void lvUpdate(BasePlayer player, int lv) {
		this.lv = lv;
		player.playerWarcraft.SetChanged();
	}
	public void skillUpdate(BasePlayer player, int type, int lv) {
		skillMap.put(type, lv);
		player.playerWarcraft.SetChanged();
	}
	
	@Override
	protected void fillMsg(JsonMsg msg) {
		msg.addProperty("PlayerWarcraft", this);
	}
}


