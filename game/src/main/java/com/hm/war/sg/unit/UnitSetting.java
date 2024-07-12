package com.hm.war.sg.unit;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.hm.config.excel.templaextra.PvpNpcTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.item.Items;
import com.hm.model.tank.Tank;
import com.hm.war.sg.AtkChoiceEnum;
import com.hm.war.sg.SettingManager;
import com.hm.war.sg.setting.TankSetting;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Data
public class UnitSetting {
	int tankId;
	long initHp;//初始血量
	long maxHp;//初始血量
	int maxMp;//最大蓝量
	int country;//国家阵营
	int amyType;//兵种类型
	int atkTimes;//一次普攻的子弹数
	int baseAtkCd;//基础攻击间隔
	int specialType;//专精类型
	int specialLv;//专精等级
	int magicBigsSkill;//魔改等级
	int superBigSkillId;//
	boolean minHurt = true;//是否有最小伤害  是否有最小伤害  
	private int fly_time;
	private Map<Integer, Integer> skillMap;
	private UnitInfo unitInfo;
	private List<Items> dropItems;
	//额外的坦克类型
	private Set<Integer> typeList = Sets.newHashSet();

	@Transient
	private transient AtkChoiceEnum atkChoiceEnum;
	@Transient
	private transient UnitAttr unitAttr;
	//是否满血
	private transient boolean isFullHp;
	
	//玩家坦克构造
	public UnitSetting(Tank tank,TankSetting setting) {
		this.unitInfo = new UnitInfo(tank, setting);
		this.unitAttr = UnitAttr.create(tank.getTotalAttrMap());
		this.fly_time = setting.getFly_time();
		this.atkChoiceEnum = setting.getAtkChoiceEnum();
		this.skillMap = Maps.newHashMap(tank.getSkillMap());
		this.initHp = this.unitAttr.getLongValue(TankAttrType.HP);
		this.tankId = setting.getId();
		this.country = setting.getCountry();
		this.amyType = setting.getType();
		this.atkTimes = setting.getAtk_times();
		this.maxMp = setting.getEnergy();
		this.baseAtkCd = setting.getAtk_cd();
		this.specialType = tank.getTankSpecial().getType();
		this.specialLv = tank.getTankSpecial().getLv();
		this.magicBigsSkill = tank.getTankMagicReform().getBigSkillId();
		this.superBigSkillId = tank.getTankMagicReform().getSuperBigSkillId();
		this.unitAttr.rebuildAtkCd();
	}
	
	//npc坦克构造
	public UnitSetting(PvpNpcTemplate npc,TankSetting setting) {
		this.unitInfo = new UnitInfo(npc);
		this.unitAttr = UnitAttr.create(npc.getNpcAttrInit(setting));
		this.fly_time = setting.getFly_time();
		this.atkChoiceEnum = setting.getAtkChoiceEnum();
		this.skillMap = npc.getSkillMap();
		this.initHp = this.unitAttr.getLongValue(TankAttrType.HP);
		this.tankId = setting.getId();
		this.country = setting.getCountry();
		this.amyType = setting.getType();
		this.atkTimes = setting.getAtk_times();
		this.maxMp = setting.getEnergy();
		this.dropItems = npc.getDropItems();
		this.baseAtkCd = setting.getAtk_cd();
		this.unitAttr.rebuildAtkCd();
	}
	
	//按照距离计算飞行时间
	public long getBulletFlyFrame(Unit atk,Unit def) {
		//四舍五入 取int
		return Math.round(fly_time*SettingManager.getInstance().getFlyFrameRate(atk,def));
	}
	
	public int getId() {
		return tankId;
	}
	
	public void addExtraSkillId(Map<Integer, Integer> tempMap) {
		this.skillMap.putAll(tempMap);
	}
	
	public void addUnitExtraType(UnitExtraType unitExtraType) {
		this.typeList.add(unitExtraType.getType());
	}
	public boolean haveUnitExtraType(UnitExtraType unitExtraType) {
		return this.typeList.contains(unitExtraType.getType());
	}
}
