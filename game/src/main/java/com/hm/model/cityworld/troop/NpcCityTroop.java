package com.hm.model.cityworld.troop;

import com.hm.action.cityworld.vo.FightTroopVo;
import com.hm.action.cityworld.vo.SMovePlayerVo;
import com.hm.config.excel.LanguageCnTemplateConfig;
import com.hm.config.excel.NpcConfig;
import com.hm.config.excel.templaextra.NpcTroopTemplate;
import com.hm.enums.CityTroopType;
import com.hm.enums.NpcType;
import com.hm.enums.TroopState;
import com.hm.libcore.spring.SpringUtil;
import com.hm.libcore.util.GameIdUtils;
import com.hm.model.cityworld.IWorldCity;
import com.hm.model.cityworld.WorldCity;
import com.hm.war.sg.troop.NpcTroop;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data
@NoArgsConstructor
public abstract class NpcCityTroop extends BaseCityFightTroop{
	private int npcId;
	private int npcType;
	private int guildId;
	private int serverId;
	private int state = TroopState.None.getType();
	private int npcConfType;//策划配置类型

	@Transient
	public transient FightTroopVo fightTroopVo;
	@Transient
	public transient SMovePlayerVo sMovePlayerVo;
	
	public NpcCityTroop(NpcType type,int serverId,int guildId) {
		super(CityTroopType.NpcTroop,"npc_"+GameIdUtils.nextStrId());
		this.npcType = type.getType();
		this.guildId = guildId;
		this.serverId = serverId;
	}

	@Override
	public void changeState(TroopState state) {
		this.state = state.getType();
	}
	
	public abstract NpcTroop createNpcTroop();

	public abstract FightTroopVo createNpcFightTroopVo(IWorldCity worldCity);

	@Override
	public int getCityTroopState(WorldCity worldCity) {
		return this.state;
	}

	@Override
	public FightTroopVo createFightTroopVo(IWorldCity worldCity) {
		if(fightTroopVo == null) {
			fightTroopVo = createNpcFightTroopVo(worldCity);
		}
		fightTroopVo.loadGuild(worldCity.getServerId());
		fightTroopVo.state = getState();
		return fightTroopVo;
	}

	@Override
	public SMovePlayerVo createSMovePlayerVo(IWorldCity worldCity) {
		if(sMovePlayerVo == null) {
			SMovePlayerVo vo = new SMovePlayerVo();
			NpcTroopTemplate template = SpringUtil.getBean(NpcConfig.class).getNpcTroopTemplate(getNpcId());
			LanguageCnTemplateConfig languageConfig = SpringUtil.getBean(LanguageCnTemplateConfig.class);
			vo.name = languageConfig.getValue(template.getName());
			vo.carLv = template.getCar_lv();
			vo.equQuality = template.getEquQuality();
			this.sMovePlayerVo = vo;
		}
		return sMovePlayerVo;
	}
}
