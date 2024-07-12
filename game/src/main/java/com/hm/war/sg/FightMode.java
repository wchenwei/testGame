package com.hm.war.sg;

import cn.hutool.core.util.RandomUtil;
import com.hm.libcore.spring.SpringUtil;
import com.hm.war.sg.aircraft.GroupAircraft;
import com.hm.war.sg.event.AircraftEvent;
import com.hm.war.sg.event.ShowAttrEvent;
import com.hm.war.sg.skillnew.Skill;
import com.hm.war.sg.skillnew.SkillType;
import com.hm.war.sg.unit.Unit;

import java.util.List;

public class FightMode {
	public WarResult fight(UnitGroup atkGroup,UnitGroup defGroup) {
		return fight(atkGroup, defGroup, -1);
	}
	
	public WarResult fight(UnitGroup atkGroup,UnitGroup defGroup,int maxFrame) {
		WarResult result = new WarResult(atkGroup, defGroup);
		atkGroup.loadDefGroup(defGroup);
		defGroup.loadDefGroup(atkGroup);
		//加载对比战斗属性
		TankFightBiz tankFightBiz = SpringUtil.getBean(TankFightBiz.class);
		tankFightBiz.loadFightUnitGroup(atkGroup, defGroup);
		
		final int MaxFrame = maxFrame > 0 ? maxFrame : WarComm.MaxFrame;
		
		int id = 1;
		while (true) {
			Frame frame = new Frame(id);
			if(checkMaxFrame(frame, result, atkGroup, defGroup,MaxFrame)) {
				break;//大于最大帧了
			}
			defGroup.setCurFrame(frame);
			atkGroup.setCurFrame(frame);

			if(id == 1) {
				//第一帧释放出场，光环技能等
				atkGroup.doReleaseSkillForType(frame, SkillType.ComOutSkill);
				defGroup.doReleaseSkillForType(frame,SkillType.ComOutSkill);
				//计算最大血量
				atkGroup.doCalMaxHp();
				defGroup.doCalMaxHp();
			}
			showTestAttr(frame,atkGroup);
			showTestAttr(frame,defGroup);

			fightFrame(atkGroup, defGroup, frame);
			fightFrame(defGroup, atkGroup, frame);
			result.addFrame(frame);
			
			
			boolean atkIsDeath = atkGroup.isAllDeath();
			boolean defIsDeath = defGroup.isAllDeath();
			if(atkIsDeath || defIsDeath) {
				result.setAtkWin(!atkIsDeath);
				//檢查最後狀態
				atkGroup.checkEndUnit(frame);
				defGroup.checkEndUnit(frame);
				break;
			}
			id ++;
		}
		result.setBattleFrame(id);
		return result;
	}
	
	private boolean checkMaxFrame(Frame frame,WarResult result,UnitGroup atkGroup,UnitGroup defGroup,int MaxFrame) {
		if(frame.getId() > MaxFrame) {//大于最大帧了
			//总血量多的一方获胜
//			boolean isAtkWin = atkGroup.getTotalTankHp() > defGroup.getTotalTankHp();
			boolean isAtkWin = false;//强制攻击方失败
			if(isAtkWin) {
				defGroup.tankAllDeath(frame);
			}else{
				atkGroup.tankAllDeath(frame);
			}
			result.setAtkWin(isAtkWin);
			return true;
		}
		return false;
	}
	
	private void fightFrame(UnitGroup atkGroup,UnitGroup defGroup,Frame frame) {
		//检查士气
		atkGroup.checkMorale(frame);
		//检查坐骑
		doCheckGroupMilitary(atkGroup, defGroup, frame);

		if(atkGroup.isCanReleaseSkill()) {
			//检查航母
			doCheckGroupAircraft(atkGroup, defGroup, frame);
		}
		//坦克分别攻击
		atkGroup.getUnits().stream()
		.filter(unit -> unit != null && !unit.isDeath())
		.forEach(unit -> unit.fight(frame));
	}
	//检查军衔技能
	public void doCheckGroupMilitary(UnitGroup atkGroup, UnitGroup defGroup, Frame frame) {
		//检查主公技能
		GroupHorse groupLeader = atkGroup.getGroupHorse();
		if(groupLeader != null) {//检查坐骑技能
			groupLeader.checkCanReleaseSkill(frame,atkGroup,defGroup);
		}
	}
	
	//航母检查
	public void doCheckGroupAircraft(UnitGroup atkGroup,UnitGroup defGroup,Frame frame) {
		//检查主公技能
		GroupAircraft groupAircraft = atkGroup.getGroupAircraft();
		if(groupAircraft != null&& groupAircraft.canAddMp(frame)) {
			groupAircraft.checkMp(frame);//增加蓝量
			Skill skill = groupAircraft.isCanReleaseSkill(frame.getId());
			if(skill != null) {
				List<Unit> unitList = atkGroup.getLifeUnit();
				if(unitList.size() > 0) {
					groupAircraft.doSkillAfter();
					skill.doActiveSkill(frame, RandomUtil.randomEle(unitList), atkGroup, defGroup);
					frame.addEvent(new AircraftEvent(atkGroup.getId(), groupAircraft.getIndex()));
                    //触发敌方技能
					defGroup.doRandomUnitReleaseSkillForType(frame, SkillType.AircraftSkill);
				}
			}
		}
	}
	
	
	public static Unit createUnit(int settingId,int index,boolean isAtk) {
		Unit unit = SettingManager.getInstance().createBaseUnit(index,settingId,isAtk);
		unit.getUnitSkills().buildSkill(SettingManager.getInstance().getTankSkillConfig(),unit.getSetting().getSkillMap());
		return unit;
	}

	public void showTestAttr(Frame frame,UnitGroup unitGroup) {
		if(unitGroup.getWarParam().isTest()) {
			for (Unit unit : unitGroup.getLifeUnit()) {
				frame.addEvent(new ShowAttrEvent(unit));
			}
		}
	}

}
