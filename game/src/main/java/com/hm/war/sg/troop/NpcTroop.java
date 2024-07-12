package com.hm.war.sg.troop;

import com.google.common.collect.Lists;
import com.hm.db.PlayerUtils;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.TankSkillConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.config.excel.templaextra.PvpNpcTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.player.Player;
import com.hm.war.sg.*;
import com.hm.war.sg.ctyterrain.CityTerrainAdapter;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitSetting;

import java.util.List;
import java.util.Map;

public class NpcTroop extends AbstractFightTroop{
	private int npcId;
	private String npcName;
	private int npcLv;
	private int npcHeadIcon;
	private int npcHeadFrame;
	private long npcCombat;
	private int guildId;
	private int npcType;
	private int serverId;

	public NpcTroop(String id,int serverId) {
		super();
		this.id = id;
		this.serverId = serverId;
		this.isNpc = true;
	}
	
	public NpcTroop(String id) {
		super();
		this.id = id;
		this.isNpc = true;
	}
	
	@Override
	public FightTroopType getFightTroopType() {
		return FightTroopType.NPC;
	}
	
	public void loadNpcInfo(NpcTroopTemplate template) {
		setTankList(template.createNpcArmy());
		this.npcId = template.getId();
		this.npcName = template.getName();
		this.npcLv = template.getLevel();
		this.npcHeadIcon = template.getHead_icon();
		this.npcHeadFrame = template.getHead_frame();
		this.npcType = template.getType();
	}
	
	public void loadNpcInfo(List<TankArmy> tankList, int npcId, String npcName, int npcLv, 
			int npcHeadIcon, int npcHeadFrame,long npcCombat) {
		loadNpcInfo(tankList, npcId, npcName, npcLv, npcHeadIcon, npcHeadFrame, npcCombat, 0,0);
	}
	
	public void loadNpcInfo(List<TankArmy> tankList, int npcId, String npcName, int npcLv, 
			int npcHeadIcon, int npcHeadFrame,long npcCombat,int guildId,int npcType) {
		setTankList(tankList);
		this.npcId = npcId;
		this.npcName = npcName;
		this.npcLv = npcLv;
		this.npcHeadIcon = npcHeadIcon;
		this.npcHeadFrame = npcHeadFrame;
		this.npcCombat = npcCombat;
		this.guildId = guildId;
		this.npcType = npcType;
	}
	
	public int getNpcId() {
		return npcId;
	}

	@Override
	public UnitGroup createUnitGroup() {
		NpcConfig npcConfig = SpringUtil.getBean(NpcConfig.class);
		TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
		TankSkillConfig tankSkillConfig = SpringUtil.getBean(TankSkillConfig.class);
		TankFightBiz tankFightBiz = SpringUtil.getBean(TankFightBiz.class);

		NpcTroopTemplate npcTroopTemplate = npcConfig.getNpcTroopTemplate(getNpcId());
		LanguageCnTemplateConfig languageConfig = SpringUtil.getBean(LanguageCnTemplateConfig.class);
		UnitGroup unitGroup = new UnitGroup();
		unitGroup.setPlayerId(-1);
		unitGroup.setTroopId(getId());
		unitGroup.setName(languageConfig.getValue(this.npcName));
		unitGroup.setCombat(this.npcCombat);
		unitGroup.setMoraleMax(moraleMax);
		unitGroup.setGuildId(this.guildId);
		unitGroup.setNpcType(this.npcType);
		unitGroup.setServerId(serverId);
		unitGroup.setNoCMorale(npcTroopTemplate.isNoCMorale());
		unitGroup.loadGuild(serverId);
		unitGroup.loadNpc(npcTroopTemplate);

		unitGroup.setTroopBuffList(Lists.newArrayList(getTroopBuffList()));
		// 技能
		this.extraSkillLvs.putAll(npcTroopTemplate.getSkillMap());
		this.extraSkillLvs.putAll(buildTroopBuffSkillMap());

		for (TankArmy tankArmy : tankList) {
			if(tankArmy.isDeath()) {
				continue;
			}
			int index = tankArmy.getIndex();
			int npcId = tankArmy.getId();
			PvpNpcTemplate npcTemplate = npcConfig.getPvpNpcTemplate(npcId);
			if(npcTemplate != null) {
				TankSetting setting = tankConfig.getTankSetting(npcTemplate.getModel());
				Unit unit = new Unit(new UnitSetting(npcTemplate,setting));
				unit.setIndex(index);
				//添加额外技能
				unit.getSetting().addExtraSkillId(this.extraSkillLvs);
				//设置技能
				Map<Integer, Integer> tankSkillMap = tankFightBiz.buildNpcTankSkillMap(npcTemplate);
				tankSkillMap.putAll(unit.getSetting().getSkillMap());
				unit.getUnitSkills().buildSkill(tankSkillConfig,tankSkillMap);
				//同步保留状态
				loadRetainState(unit, tankArmy);
				unitGroup.addUnit(unit);
			}
		}
		GroupHorse.loadHorse(npcTroopTemplate, unitGroup);//加载坐骑

		return unitGroup;
	}


	@Override
	public void doWarResult(UnitGroup unitGroup) {
		super.doWarResult(unitGroup);
		unitGroup.getUnits().forEach(e -> {
			TankArmy tankArmy = getTankArmy(e.getSetting().getUnitInfo().getNpcId(),e.getIndex());
			if(tankArmy == null) {
				return;
			}
			if(e.isDeath()) {
				tankArmy.setHp(0);
				return;
			}
			tankArmy.setUnitRetainType(UnitRetainType.HP, e.getHp());
			//注释蓝量继承
//			tankArmy.setUnitRetainType(UnitRetainType.MP, (long)e.getMpEngine().getMp());
		});
	}
	public TankArmy getTankArmy(int id,int index) {
		return tankList.stream().filter(e -> e.getId() == id && e.getIndex() == index).findFirst().orElse(null);
	}

	public String getNpcName() {
		return npcName;
	}

	@Override
	public int getGuildId() {
		return guildId;
	}

	public int getNpcType() {
		return npcType;
	}

	public int getServerId() {
		return serverId;
	}
}
