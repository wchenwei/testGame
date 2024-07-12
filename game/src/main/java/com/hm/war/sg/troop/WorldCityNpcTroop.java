package com.hm.war.sg.troop;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.model.cityworld.troop.NpcCityTroop;

/**
 * @Description: 世界城池战斗npc
 * @author siyunlong  
 * @date 2018年12月14日 下午3:58:45 
 * @version V1.0
 */
public class WorldCityNpcTroop extends NpcTroop{
	
	public WorldCityNpcTroop(NpcCityTroop npcTroop) {
		super(npcTroop.getId(),npcTroop.getServerId());
	}
	
	public WorldCityNpcTroop(int npcId) {
		super(npcId+"");
		NpcTroopTemplate template = SpringUtil.getBean(NpcConfig.class).getNpcTroopTemplate(npcId);
		loadNpcInfo(template);
	}
}
