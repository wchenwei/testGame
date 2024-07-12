package com.hm.action.tank.biz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Biz;
import com.hm.config.MagicReformConfig;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.TankMagicReformTemplate;
import com.hm.enums.CommonValueType;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.model.tank.Tank;
import com.hm.model.tank.TankAttr;
import com.hm.model.tank.TankMagicReform;
import com.hm.util.ItemUtils;
import com.hm.util.MathUtils;
import com.hm.war.sg.setting.TankSetting;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Biz
public class MagicReformBiz {
	@Resource
	private MagicReformConfig magicReformConfig;
	@Resource
	private TankConfig tankConfig;
	@Resource
	private CommValueConfig commValueConfig;
	/**
	 * 魔改出技能
	 * @param player
	 * @param tank
	 * @return -1表示魔改出大招
	 */
	public int reform(Player player,Tank tank){
        //一阶魔改等级上限
        int  lvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformLimit);
        if(tank.getTankMagicReform().getReformLv()<lvLimit&&tank.getTankMagicReform().getBigSkillId()>0){
            return tankConfig.randomMagicSkill(tank);
        }
        if(tank.getTankMagicReform().getReformLv()>=lvLimit&&tank.getTankMagicReform().getSuperBigSkillId()>0){
            return tankConfig.randomMagicSkill(tank);
        }
		int lv = tank.getTankMagicReform().getReformLv()+1;
		TankMagicReformTemplate template = magicReformConfig.getMagicReformTemplate(tank.getId(), lv>lvLimit?lv-lvLimit:lv);
		int weight = template.getWeight();
		if(MathUtils.random(1, 10001)<weight){
			return -1;
		}
		return tankConfig.randomMagicSkill(tank);
	}

	/**
	 * 获取魔改加成的属性
	 * @param player
	 * @param tank
	 * @return
	 */
	public TankAttr getMagicReformAttr(Player player, Tank tank) {
		TankAttr tankAttr = new TankAttr();
		TankMagicReform tankMagicReform = tank.getTankMagicReform();
		if(tankMagicReform.getReformLv()<=0){
			return tankAttr;
		}
		//小技能附加属性
		for(Map.Entry<Integer, Integer> entry:tank.getTankMagicReform().getSkills().entrySet()){
			tankAttr.addAttr(magicReformConfig.getSkillAttr(entry.getKey(),entry.getValue()));
		}
		//魔改本身的属性加成
		tankAttr.addAttr(commValueConfig.getMagicReformAttr(tank.getTankMagicReform().getReformLv()));
		return tankAttr;
	}
	/**
	 * 替换技能
	 * @param player
	 * @param tankId
	 * @param skillMap
	 */
	public void calTankSkill(Player player, int tankId,Map<Integer, Integer> skillMap) {
		Tank tank = player.playerTank().getTank(tankId);
		calTankSkill(tank, skillMap);
	}
	public void calTankSkill(Tank tank,Map<Integer, Integer> skillMap) {
		if(tank == null) {
			return;
		}
		int bigSkill = tank.getTankMagicReform().getBigSkillId();
		if(bigSkill<=0){
			return ;
		}
		TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
		int beReplaceSkillId = tankSetting.getMagic_reform_oldbigskill();
		int lv = 1;
		if(beReplaceSkillId>0&&skillMap.containsKey(beReplaceSkillId)){
		    lv = skillMap.remove(beReplaceSkillId);
		}
        skillMap.put(bigSkill, lv);
        int superBigSkill = tank.getTankMagicReform().getSuperBigSkillId();
        if(superBigSkill<=0){
            return ;
        }
        int beReplaceSkillId2 = tankSetting.getMagic_reform_superoldbigskill();
        int lv2 =1;
        if(beReplaceSkillId2>0&&skillMap.containsKey(beReplaceSkillId2)){
            lv2 = skillMap.remove(beReplaceSkillId2);
        }
        skillMap.put(superBigSkill,lv2);

	}
	/**
	 * 转移
	 * @param player
	 * @param tankId
	 * @param desTankId
	 */
	public void transfer(Player player, int tankId, int desTankId) {
		Tank tank = player.playerTank().getTank(tankId);
		Tank desTank = player.playerTank().getTank(desTankId);
		int reformLvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformLimit);
		//交换后tank的魔改大招
		int tankMagicBigId = desTank.getTankMagicReform().getBigSkillId()>0?tankConfig.getTankSetting(tankId).getMagic_reform_bigskill():0;
        int superMagicBigId = desTank.getTankMagicReform().getSuperBigSkillId()>0?tankConfig.getTankSetting(tankId).getSuper_reform_bigskill():0;
		//交换后desTank的魔改大招
		int desTankMagicBigId = tank.getTankMagicReform().getBigSkillId()>0?tankConfig.getTankSetting(desTankId).getMagic_reform_bigskill():0;
        int desSuperMagicBigId = tank.getTankMagicReform().getSuperBigSkillId()>0?tankConfig.getTankSetting(desTankId).getSuper_reform_bigskill():0;
		TankSetting tankSetting = tankConfig.getTankSetting(tankId);
        TankSetting tankSetting2 = tankConfig.getTankSetting(desTankId);
        List<Integer> tankSkillIds = tankConfig.getTankSetting(tankId).getMagicSkillIds();
		List<Integer> desTankSkillIds = tankConfig.getTankSetting(desTankId).getMagicSkillIds();
		Map<Integer,Integer> tankSkillMap = Maps.newConcurrentMap();
		Map<Integer,Integer> desTankSkillMap = Maps.newConcurrentMap();
		for(int i=0;i<5;i++){
			tankSkillMap.put(tankSetting.getMagicSkillIds().get(i), desTank.getTankMagicReform().getSkillLv(desTankSkillIds.get(i)));
			desTankSkillMap.put(tankSetting2.getMagicSkillIds().get(i), tank.getTankMagicReform().getSkillLv(tankSkillIds.get(i)));
		}
		if(tank.getTankMagicReform().getReformLv()>reformLvLimit||desTank.getTankMagicReform().getReformLv()>reformLvLimit){
            //二阶普通技能
            for(int i=0;i<5;i++){
                tankSkillMap.put(tankSetting2.getSuperMagicSkillIds().get(i), desTank.getTankMagicReform().getSkillLv(tankSetting2.getSuperMagicSkillIds().get(i)));
                desTankSkillMap.put(tankSetting2.getSuperMagicSkillIds().get(i), tank.getTankMagicReform().getSkillLv(tankSetting.getSuperMagicSkillIds().get(i)));
            }
        }
		TankMagicReform tankMagicReform = new TankMagicReform(desTank.getTankMagicReform().getReformLv(),tankSkillMap,tankMagicBigId,superMagicBigId);
		TankMagicReform desTankMagicReform = new TankMagicReform(tank.getTankMagicReform().getReformLv(),desTankSkillMap,desTankMagicBigId,desSuperMagicBigId);
		player.playerTank().changeMagic(tankId, tankMagicReform);
		player.playerTank().changeMagic(desTankId, desTankMagicReform);
	}
    //是否可以转移
    public boolean isCantransfer(List<Tank> tanks){
	    //魔改等级最高的坦克
        Tank maxTank = tanks.stream().max(Comparator.comparing(t->t.getTankMagicReform().getReformLv())).orElse(null);
	    //魔改等级最低的坦克
        Tank minTank = tanks.stream().min(Comparator.comparing(t->t.getTankMagicReform().getReformLv())).orElse(null);
	    TankSetting tankSettingMax = tankConfig.getTankSetting(maxTank.getId());
	    TankSetting tankSettingMin = tankConfig.getTankSetting(minTank.getId());
	    //一阶魔改上限
	    int reformLvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformLimit);
	    if(maxTank.getTankMagicReform().getReformLv()>reformLvLimit&&minTank.getTankMagicReform().getReformLv()<=reformLvLimit){
	        //处于不同的魔改阶段
            if(tankSettingMin.getSuper_reform_bigskill()<=0){
                return false;
            }
            return true;
        }
        return true;
    }

    public List<Items> getMagicReformCost(Tank tank) {
	    TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
	    int reformLv = tank.getTankMagicReform().getReformLv();
	    //一阶魔改等级上限
	    int reformLvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformLimit);
	    if(reformLv<reformLvLimit){
	        return tankSetting.getXianding_type()>0?commValueConfig.getListItem(CommonValueType.SuperMagicReformCost):commValueConfig.getListItem(CommonValueType.MagicReformCost);
        }
	    return tankSetting.getXianding_type()>0?commValueConfig.getListItem(CommonValueType.SuperMagicReformCost2):commValueConfig.getListItem(CommonValueType.MagicReformCost2);
    }


	public List<Items> resetMagicReward(Tank tank) {
		TankSetting tankSetting = tankConfig.getTankSetting(tank.getId());
		int reformLv = tank.getTankMagicReform().getReformLv();
		//一阶魔改等级上限
		int reformLvLimit = commValueConfig.getCommValue(CommonValueType.MagicReformLimit);
		boolean isXianDing = tankSetting.getXianding_type() > 0;
		List<Items> list = Lists.newArrayList();
		int lv = Math.min(reformLv, reformLvLimit);
		List<Items> items = isXianDing ? commValueConfig.getListItem(CommonValueType.SuperMagicReformCost) : commValueConfig.getListItem(CommonValueType.MagicReformCost);
		list.addAll(ItemUtils.calItemRateReward(items, lv));

		if(reformLv > reformLvLimit){
			// 二阶魔改消耗
			List<Items> itemsList = isXianDing ? commValueConfig.getListItem(CommonValueType.SuperMagicReformCost2) : commValueConfig.getListItem(CommonValueType.MagicReformCost2);
			list.addAll(ItemUtils.calItemRateReward(itemsList,  reformLv - reformLvLimit));
		}
		return ItemUtils.mergeItemList(list);
	}
}
