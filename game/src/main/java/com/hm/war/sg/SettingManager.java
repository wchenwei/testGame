package com.hm.war.sg;

import com.hm.config.excel.*;
import com.hm.config.excel.templaextra.MilitaryExtraTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.enums.CommonValueType;
import com.hm.enums.TankAttrType;
import com.hm.model.tank.Tank;
import com.hm.war.sg.setting.SkillSetting;
import com.hm.war.sg.setting.TankSetting;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.unit.Unit;
import com.hm.war.sg.unit.UnitSetting;

import java.util.Map;

public class SettingManager {
	private static final SettingManager Instance = new SettingManager();
	public static SettingManager getInstance() {
		return Instance;
	}
	private TankConfig tankConfig;
	private TankSkillConfig tankSkillConfig;
	private CommValueConfig commConfig;
	private FightConfig fightConfig;

	//指挥官军衔技能伤害等级
	public static int[][] MSkillHurts;
	
	public void initServer() {
		this.tankConfig = SpringUtil.getBean(TankConfig.class);
		this.tankSkillConfig = SpringUtil.getBean(TankSkillConfig.class);
		this.commConfig = SpringUtil.getBean(CommValueConfig.class);
		this.fightConfig = SpringUtil.getBean(FightConfig.class);

		Map<Integer, MilitaryExtraTemplate> militaryMap = SpringUtil.getBean(CommanderConfig.class).getMilitaryMap();
		MSkillHurts = new int[2][militaryMap.size()];
		for (MilitaryExtraTemplate value : militaryMap.values()) {
			MSkillHurts[0][value.getNaskill_lv()] = value.getNaskill_num();
			MSkillHurts[1][value.getKaskill_lv()] = value.getKaskill_num();
		}
	}
	public void init() {
		this.tankConfig = new TankConfig();
		this.tankConfig.loadConfig();
		this.tankSkillConfig = new TankSkillConfig();
		this.tankSkillConfig.loadConfig();
		this.commConfig = new CommValueConfig();
		this.commConfig.loadConfig();
		this.fightConfig = new FightConfig();
		this.fightConfig.loadConfig();
	}
	
	public UnitSetting getUnitSetting(int id) {
		//构造一级坦克
		Tank tank = new Tank();
		tank.setId(id);
		tank.setLv(1);
		TankSetting setting = tankConfig.getTankSetting(id);
		for (Map.Entry<TankAttrType,Double> entry: setting.getTankAttrInit().entrySet()) {
			tank.getTotalAttrMap().put(entry.getKey().getType(), entry.getValue());
		}
		return new UnitSetting(tank,setting);
	}
	
	public Skill createSkill(int id, int lv) {
		SkillSetting setting = tankSkillConfig.getSkillSetting(id);
		return setting != null ? new Skill(1, setting) : null;
	}
	
	public Unit createBaseUnit(int index, int settingId, boolean isAtk) {
		UnitSetting setting = getUnitSetting(settingId);
		Unit unit = new Unit(setting);
		unit.setId(isAtk?index:index+10);
		unit.setIndex(index);
		return unit;
	}
	public TankSkillConfig getTankSkillConfig() {
		return tankSkillConfig;
	}


	//获取子弹距离飞行比例
	public double getFlyFrameRate(Unit atk,Unit def) {
		if(atk.getIndex() <0 || def.getIndex() < 0 
				|| atk.getId() < 10 && def.getId() < 10//是一方的
				|| atk.getId() > 10 && def.getId() > 10
				) {
			return 1;
		}
		double[] flyRate = this.commConfig.getConvertObj(CommonValueType.FlyDistanceRate);
		int index = atk.getIndex()/3 + def.getIndex()/3;
		return flyRate[index];
	}
	
	public TankSetting getTankSetting(int tankId) {
		return tankConfig.getTankSetting(tankId);
	}
	
	public double getSecondMp() {
		return this.commConfig.getDoubleCommValue(CommonValueType.TankSecondMp);
	}
	public int getSecondWill() {
		return this.commConfig.getCommValue(CommonValueType.TankSecondWill);
	}
	public double getTankLoseHpWill() {
		return this.commConfig.getDoubleCommValue(CommonValueType.TankLoseHpWill);
	}
	public double getTankLoseHpMp() {
		return this.commConfig.getDoubleCommValue(CommonValueType.TankLoseHpMp);
	}
	public double getKillAddMp() {
		return this.commConfig.getDoubleCommValue(CommonValueType.KillUnitAddMp);
	}
	public int getCommValue(CommonValueType type) {
		return this.commConfig.getCommValue(type);
	}
	public double getDoubleCommValue(CommonValueType type) {
		return this.commConfig.getDoubleCommValue(type);
	}
	public String getStrValue(CommonValueType type) {
		return this.commConfig.getStrValue(type);
	}
	public double getDamageModify(int atkType,int defType) {
		return this.fightConfig.getDamageModify(atkType, defType);
	}
}
