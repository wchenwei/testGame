package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MissionDreamEliteTemplate;
import com.hm.war.sg.troop.TankArmy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@FileConfig("mission_dream_elite")
public class MissionDreamEliteExtraTemplate extends MissionDreamEliteTemplate{
	public List<Long> combats = Lists.newArrayList();
	public Map<Integer,List<TankArmy>> defTroops = Maps.newConcurrentMap();
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
			defTroops.put(i+1, tankList);
		}
		this.combats = Arrays.stream(this.getPower().split(",")).map(t->Long.parseLong(t)).collect(Collectors.toList());
		
	}
	
	public List<Long> getCombats(){
		return combats;
	}
	public Map<Integer, List<TankArmy>> getDefTroops() {
		return defTroops;
	}
	
	public int getMaxScore() {
		return defTroops.size()*10;
	}
}
