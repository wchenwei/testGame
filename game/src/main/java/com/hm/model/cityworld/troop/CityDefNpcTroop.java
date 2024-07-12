package com.hm.model.cityworld.troop;

import com.hm.libcore.spring.SpringUtil;
import com.hm.action.cityworld.vo.FightTroopVo;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.enums.NpcConfType;
import com.hm.enums.NpcType;
import com.hm.model.cityworld.IWorldCity;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.json.TroopJsonType;
import com.hm.war.sg.troop.NpcTroop;
import com.hm.war.sg.troop.WorldCityNpcTroop;
import lombok.NoArgsConstructor;

/**
 * @Description: 城镇防守
 * @author siyunlong  
 * @date 2018年11月26日 下午5:19:11 
 * @version V1.0
 */
@NoArgsConstructor
public class CityDefNpcTroop extends NpcCityTroop{
	public CityDefNpcTroop(int npcId,int serverId,int guildId) {
		super(NpcType.CityDef,serverId,guildId);
		setNpcId(npcId);
		setJt(TroopJsonType.CityDefNpcTroop);
	}
	
	public CityDefNpcTroop(NpcConfType npcConfType,int npcId,int serverId,int guildId) {
		super(NpcType.CityDef,serverId,guildId);
		setNpcId(npcId);
		setJt(TroopJsonType.CityDefNpcTroop);
		setNpcConfType(npcConfType.getType());
	}

	@Override
	public NpcTroop createNpcTroop() {
		WorldCityNpcTroop npcTroop = new WorldCityNpcTroop(this);
		NpcTroopTemplate template = SpringUtil.getBean(NpcConfig.class).getNpcTroopTemplate(getNpcId());
		if(template == null) {
//			System.err.println("找不到npc:"+getNpcId());
			return null;
		}
		npcTroop.loadNpcInfo(template.createNpcArmy(), template.getId(), 
				template.getName(), template.getLevel(), template.getHead_icon(), template.getHead_frame(),0,getGuildId(),template.getType());
		return npcTroop;
	}

	@Override
	public FightTroopVo createNpcFightTroopVo(IWorldCity worldCity) {
		FightTroopVo vo = new FightTroopVo();
		NpcTroopTemplate template = SpringUtil.getBean(NpcConfig.class).getNpcTroopTemplate(getNpcId());
		LanguageCnTemplateConfig languageConfig = SpringUtil.getBean(LanguageCnTemplateConfig.class);
		vo.isNpc = true;
		vo.id = "npc"+template.getId();
		vo.state = getState();
		vo.index = 0;
		vo.name = languageConfig.getValue(template.getName());
		vo.lv = template.getLevel();
		vo.icon = template.getHead_icon()+"";
		vo.frameIcon = template.getHead_frame();
		vo.npcType = template.getType();
		vo.serverId = getServerId();
		vo.guildId = getGuildId();
		vo.loadGuild(getServerId());
		return vo;
	}
	
	
}
