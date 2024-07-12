package com.hm.war.sg.unit;

import cn.hutool.core.collection.CollUtil;
import com.hm.util.MathUtils;
import com.hm.war.sg.Frame;
import com.hm.war.sg.SettingManager;
import com.hm.war.sg.bear.HurtBear;
import com.hm.war.sg.buff.*;
import com.hm.war.sg.event.ShieldEvent;
import com.hm.war.sg.skillnew.SkillType;

import java.util.List;

public class HpEngine {
	private transient Unit unit;
	private long hp;//当前血量
	private long lastReduceHpFrame;//上次减血的针id
	
	public HpEngine(Unit unit) {
		this.unit = unit;
		this.hp = unit.setting.getInitHp();
	}
	
	public void setInitHp(long initHp) {
		this.hp = initHp;
	}
	
	public long getHp() {
		return  this.hp;
	}
	
	public HurtHpResult reduceHp(Frame frame,int atkId,long hurt,boolean isLinkHurt,long skillId,boolean ignoreShield) {
		HurtHpResult hurtHpResult = new HurtHpResult();
		hurtHpResult.setHurt(hurt);
		if(!isDeath() && hurt > 0) {
			if(unit.getUnitBuffs().haveBuff(UnitBufferType.InvincibleBuff)) {
				return hurtHpResult;//有无敌buff
			}
			if(isLinkHurt) {//计算致命连接效果
				hurt = doHurtLink(frame,hurt);
				hurtHpResult.setHurt(hurt);
			}
			//计算蓝量
			double hurtAddMp = SettingManager.getInstance().getTankLoseHpMp();
			double addMp = MathUtils.div(hurt,this.unit.getMaxHp())*100*hurtAddMp;
			this.unit.getMpEngine().addMp(frame,addMp);
			//统计伤害
			this.unit.calBearHurt(atkId, hurt);
			//检查是否有护盾
			if(!ignoreShield) {//是否计算护盾
				hurt = doShieldBuff(frame, hurt);
			}
			hurtHpResult.setHurt(hurt);
			
			if(hurt > 0) {

				reduceHpForHurt(hurt);
				this.lastReduceHpFrame = frame.getId();
				if(isDeath()) {
					//触发额外判断
					this.unit.getMyGroup().getWarParam().doUnitDeath(frame,unit);
					//受到致命一击触发技能
					this.unit.getUnitSkills().doReleaseSkillForType(frame, SkillType.DeadlyHurt);
					if(!isDeath()) {
						return hurtHpResult;//没死
					}
					if(unit.getUnitBuffs().haveBuff(UnitBufferType.NoDeath)) {
						this.hp = 1;//有不死buff
						return hurtHpResult;
					}
					this.unit.doUnitDeath(frame,atkId);
				}
			}
		}
		return hurtHpResult;
	}
	
	/**
	 * 处理护盾buff
	 */
	public long doShieldBuff(Frame frame,long hurt) {
		List<BaseBuffer> buffList = unit.getUnitBuffs().getBuffList(UnitBufferType.ShieldBuff);
		if(CollUtil.isEmpty(buffList)) {
			return hurt;
		}
		for (int i = 0; i < buffList.size(); i++) {
			ShieldBuff shieldBuff = (ShieldBuff)buffList.get(i);
			hurt = shieldBuff.doHurt(frame,this.unit,hurt);//优先扣除护盾血量
			long endTime = shieldBuff.getEndFrame();
			long lastFrame = endTime < 0?1:endTime - frame.getId();
			if(lastFrame <= 0 || shieldBuff.getShieldValue() <= 0) {//护盾消失
				unit.getUnitBuffs().removeBuff(shieldBuff);
				lastFrame = 0;
			}
			frame.addEvent(new ShieldEvent(unit.getId(), lastFrame, shieldBuff.getShieldValue(),shieldBuff.getFuncId()));
			if(hurt <= 0) {
				return 0;
			}
		}
		return hurt;
	}
	
	
	//直接斩杀,不判断任何血量和buff
	public void doBeheaderKill(Frame frame,int atkId) {
		if(!isDeath()) {
			long hurt = this.hp;
			this.unit.calBearHurt(atkId, hurt);
			this.hp = 0;
			this.unit.doUnitDeath(frame,atkId);
		}
	}
	
	public void addHp(long add) {
		if(add == 0) return;
		this.hp = Math.min(this.hp+add, unit.getMaxHp());
	}
	public void reduceHpForHurt(long hurt) {
		if(hurt == 0) return;
		this.hp = Math.max(this.hp-hurt, 0);
	}
	
	public double getHpRate() {
		return MathUtils.div(this.hp, unit.getMaxHp());
	}
	
	public boolean isDeath() {
		return this.hp <= 0;
	}
	
	public long getLastReduceHpFrame() {
		return lastReduceHpFrame;
	}

	//处理致命连接
	public long doHurtLink(Frame frame,long hurt) {
		long totalHurt = hurt;
		//计算
		List<BaseBuffer> hurtSelfLinkList = unit.getUnitBuffs().getBuffList(UnitBufferType.HurtSelfLink);
		for (BaseBuffer baseBuffer : hurtSelfLinkList) {
			HurtSelfLinkBuff hurtLinkBuff = (HurtSelfLinkBuff)baseBuffer;
			if(hurt > 0 && hurtLinkBuff.getBuffSendId() != this.unit.getId()) {
				//伤害 给他
				Unit defUnit = this.unit.getMyGroup().getUnitById(hurtLinkBuff.getBuffSendId());
				if(defUnit != null) {
					long defHurt = (long)(totalHurt*Math.min(1, hurtLinkBuff.getValue()));
					//最大为他的血量
					defHurt = Math.min(defHurt, defUnit.getHp());
					hurt -= defHurt;
					HurtBear hurtBear = new HurtBear(unit.getId(), frame.getId()+1, defHurt, 0, 0);
					hurtBear.setLinkHurt(false);
					defUnit.addBear(frame, hurtBear);
				}
			}
		}
		final long linkHurt = Math.min(totalHurt, getHp());
		unit.getUnitBuffs().getBuffList(UnitBufferType.HurtLink).stream()
		.map(e -> (HurtLinkBuff)e).forEach(e -> e.doLinkHurt(frame,unit, linkHurt));
		
		return hurt;
	}
}
