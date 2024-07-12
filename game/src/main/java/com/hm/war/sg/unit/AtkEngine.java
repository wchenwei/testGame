package com.hm.war.sg.unit;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.enums.TankAttrType;
import com.hm.util.MathUtils;
import com.hm.war.sg.ChoiceUtils;
import com.hm.war.sg.UnitGroup;
import com.hm.war.sg.buff.AddAtkTargetsBuff;
import com.hm.war.sg.buff.BaseBuffer;
import com.hm.war.sg.buff.RandomAddAtkTargetsBuff;
import com.hm.war.sg.buff.UnitBufferType;
import lombok.Getter;

import java.util.List;

@Getter
public class AtkEngine {
	private transient Unit unit;
	//最后一次攻击的帧数
    protected transient long nextFrame;
	//当前攻击目标
	protected transient List<Unit> curAtkTargets = Lists.newArrayList();
	//普攻次数
	protected transient long normalAtkNum;
	//基础攻击cd
	protected transient long baseAtkCD;
	//当前cd/基础攻击cd 的百分比
	protected transient double atkCDRate = 1;
	
	public AtkEngine(Unit unit) {
		this.unit = unit;
		this.baseAtkCD = unit.getSetting().getUnitAttr().getTureAtkCd();
	}
	
	//更新最后一次攻击帧
	public void updateLastFrame(long curFrame) {
        if (curFrame <= 0) {
            this.nextFrame = 0;
            return;
        }
        this.nextFrame = curFrame + getAtkCD();
	}
	//判断是否可以出手
    public boolean isCanAtk(long curFrame) {
        if (this.nextFrame == 0) {
            long firstAtkCd = unit.getSetting().getUnitAttr().getLongValue(TankAttrType.FirstAtkCd);
            return curFrame >= firstAtkCd;
        }
        return curFrame >= this.nextFrame;
    }

    private long getAtkCD() {
        UnitAttr atkAttr = unit.getUnitAttr();
        long atkCd = atkAttr.getTureAtkCd();
		this.atkCDRate = MathUtils.div(atkCd,this.baseAtkCD,2);
		return atkCd;
    }

	
	/**
	 * 选择攻击目标
	 * @param defGroup
	 * @return
	 */
	public List<Unit> choiceTarget(UnitGroup defGroup) {
		List<Unit> curAtkTargets = getCurAtkTargets(defGroup);
		int extraCount = 0;
		//获取攻击多人buff
		BaseBuffer addAtkTarget = this.unit.getUnitBuffs().getFirstBaseBufferByType(UnitBufferType.AddAtkTargets);
		if(addAtkTarget != null) {
			//判断攻击额外目标
			extraCount += ((AddAtkTargetsBuff)addAtkTarget).getAddCount();
		}
		//概率额外攻击目标
		BaseBuffer randomAtkTarget = this.unit.getUnitBuffs().getFirstBaseBufferByType(UnitBufferType.RandomAddAtkTargets);
		if(randomAtkTarget != null) {
			//判断攻击额外目标
			extraCount += ((RandomAddAtkTargetsBuff)randomAtkTarget).getAddCount();
		}
		if(extraCount > 0) {
			List<Unit> extraList = ChoiceUtils.randomExtraAtkTarget(defGroup.getLifeUnit(), curAtkTargets, extraCount);
			List<Unit> allList = Lists.newArrayList(curAtkTargets);
			allList.addAll(extraList);
			return allList;
		}
		return curAtkTargets;
	}
	
	public void addNormalAtkNum() {
		this.normalAtkNum ++;
	}

	public long getNormalAtkNum() {
		return normalAtkNum;
	}
	
	public List<Unit> getCurAtkTargets(UnitGroup defGroup) {
		//是否有嘲讽buff
		List<BaseBuffer> buffList = this.unit.getUnitBuffs().getBuffList(UnitBufferType.TauntBuff);
		for (int i = buffList.size()-1; i >= 0; i--) {
			Unit def = defGroup.getUnitById(buffList.get(i).getBuffSendId());
			if(def != null) {
				return Lists.newArrayList(def);
			}
		}
		for (int i = this.curAtkTargets.size()-1; i >= 0; i--) {
			Unit def = this.curAtkTargets.get(i);
			if(def.isDeath()) {
				this.curAtkTargets.remove(i);
			}
		}
		if(CollUtil.isEmpty(this.curAtkTargets)) {
			this.curAtkTargets = this.unit.setting.getAtkChoiceEnum().choiceTargets(unit, defGroup);
		}
		return this.curAtkTargets;
	}
	
	public List<Unit> getCurTrueAtkTargets() {
		return this.curAtkTargets;
	}
	
}
