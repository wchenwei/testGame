package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.action.troop.client.ClientTroop;
import com.hm.config.excel.temlate.EnemyTotalWarTemplate;
import com.hm.war.sg.troop.TankArmy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@FileConfig("enemy_total_war")
public class OverallWarTemplate extends EnemyTotalWarTemplate {
	public List<Long> combats = Lists.newArrayList();
	public Map<Integer,ClientTroop> defTroops = Maps.newConcurrentMap();
	public void init(){
		String[] troopStr = this.getEnemy_config().split(";");
		for(int i=0;i<troopStr.length;i++){
			List<TankArmy> tankList = Lists.newArrayList();
			String[] troop = troopStr[i].split(",");
			for(int j=0;j<troop.length;j++){
				String str = troop[j];
				int pos = Integer.parseInt(str.split(":")[0]);
				int tankId = Integer.parseInt(str.split(":")[1]);
				tankList.add(new TankArmy(pos, tankId));
			}
			
			defTroops.put(i+1, ClientTroop.build(tankList));
		}
		this.combats = Arrays.stream(this.getPower().split(",")).map(t->Long.parseLong(t)).collect(Collectors.toList());
		
	}
	public List<Long> getCombats(){
		return combats;
	}
	public Map<Integer, ClientTroop> getDefTroops() {
		return defTroops;
	}
	
	
}
