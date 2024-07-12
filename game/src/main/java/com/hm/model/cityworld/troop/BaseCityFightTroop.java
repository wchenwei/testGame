package com.hm.model.cityworld.troop;

import cn.hutool.core.util.StrUtil;
import com.hm.action.cityworld.vo.FightTroopVo;
import com.hm.action.cityworld.vo.SMovePlayerVo;
import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.CommValueConfig;
import com.hm.enums.CityTroopType;
import com.hm.enums.CommonValueType;
import com.hm.enums.TroopState;
import com.hm.model.cityworld.IWorldCity;
import com.hm.model.cityworld.WorldCity;
import com.hm.war.sg.troop.AbstractFightTroop;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseCityFightTroop {
	private String id;
	private int troopType;
	private int moraleMax;
	private int jt;//jsontype 用于json翻转
	
	public BaseCityFightTroop(CityTroopType troopType,String id) {
		this.id = id;
		this.troopType = troopType.getType();
		this.moraleMax = SpringUtil.getBean(CommValueConfig.class).getCommValue(CommonValueType.MoraleMax);
	}


	//获取部队状态
	public abstract int getCityTroopState(WorldCity worldCity);
	public abstract FightTroopVo createFightTroopVo(IWorldCity worldCity);
	public abstract SMovePlayerVo createSMovePlayerVo(IWorldCity worldCity);

	public String getId() {
		return id;
	}

	public int getTroopType() {
		return troopType;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}


	public boolean isNpcTroop() {
		return troopType == CityTroopType.NpcTroop.getType();
	}
	public boolean isCloneTroop() {
		return troopType == CityTroopType.ClonePlayer.getType();
	}
	
	public void changeState(TroopState state) {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		BaseCityFightTroop temp = (BaseCityFightTroop)obj;
		return StrUtil.equals(this.id, temp.getId());
	}

	public int getMoraleMax() {
		return moraleMax;
	}
	
	public void setMoraleMax(int moraleMax) {
		this.moraleMax = moraleMax;
	}

    public void reduceMorale() {
        reduceMorale(null);
    }

    public void reduceMorale(AbstractFightTroop fightTroop) {
        CommValueConfig commValueConfig = SpringUtil.getBean(CommValueConfig.class);
        int minValue = commValueConfig.getCommValue(CommonValueType.MoraleMin);
        int reduceValue = commValueConfig.getCommValue(CommonValueType.MoraleReduceFight);
        this.moraleMax = Math.max(this.moraleMax - reduceValue, minValue);
    }

	public void setId(String id) {
		this.id = id;
	}

}
