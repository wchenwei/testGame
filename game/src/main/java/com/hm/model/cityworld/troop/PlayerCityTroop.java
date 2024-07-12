package com.hm.model.cityworld.troop;

import com.hm.action.cityworld.vo.FightTroopVo;
import com.hm.action.cityworld.vo.SMovePlayerVo;
import com.hm.enums.CityTroopType;
import com.hm.enums.TroopState;
import com.hm.model.cityworld.IWorldCity;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.cityworld.troop.json.TroopJsonType;
import com.hm.model.worldtroop.WorldTroop;
import com.hm.servercontainer.troop.TroopServerContainer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PlayerCityTroop extends BaseCityFightTroop{
	public PlayerCityTroop(String worldTroopId) {
		super(CityTroopType.PlayerTroop,worldTroopId);
		setJt(TroopJsonType.PlayerCityTroop);
	}


	@Override
	public FightTroopVo createFightTroopVo(IWorldCity worldCity) {
		WorldTroop worldTroop = TroopServerContainer.of(worldCity.getServerId()).getWorldTroop(getId());
		if(worldTroop == null || worldTroop.getCityId() != worldCity.getWorldCityId()) {
			return null;
		}
		return new FightTroopVo(worldTroop);
	}

	@Override
	public SMovePlayerVo createSMovePlayerVo(IWorldCity worldCity) {
		WorldTroop worldTroop = TroopServerContainer.of(worldCity.getServerId()).getWorldTroop(getId());
		if(worldTroop == null || worldTroop.getCityId() != worldCity.getWorldCityId()) {
			return null;
		}
		return new SMovePlayerVo(worldTroop.getPlayerId());
	}

	@Override
	public int getCityTroopState(WorldCity worldCity) {
		WorldTroop worldTroop = TroopServerContainer.of(worldCity).getWorldTroop(getId());
		if(worldTroop != null) {
			return worldTroop.getState();
		}
		return TroopState.None.getType();
	}
}
