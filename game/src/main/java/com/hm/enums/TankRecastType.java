package com.hm.enums;

import com.google.common.collect.Lists;
import com.hm.action.tank.biz.TankBiz;
import com.hm.config.excel.CommValueConfig;
import com.hm.config.excel.TankConfig;
import com.hm.config.excel.temlate.TankLevelTemplate;
import com.hm.config.excel.templaextra.DriverLvTemplate;
import com.hm.config.excel.templaextra.DriverTemplate;
import com.hm.config.excel.templaextra.ReformTemplate;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.item.Items;
import com.hm.model.tank.Tank;
import com.hm.war.sg.setting.TankSetting;

import java.util.List;

/**
 * @Description: 坦克重铸类型
 * @author siyunlong  
 * @date 2019年8月25日 下午8:08:49 
 * @version V1.0
 */
public enum TankRecastType {

    LvRecast(1,"tank重铸"){
        @Override
        public boolean checkRecast(Tank tank) {
            return tank.getLv() > 1;
        }

        @Override
        public void doRecast(Tank tank) {
            TankSetting tankSetting = SpringUtil.getBean(TankConfig.class).getTankSetting(tank.getId());
            tank.setLv(1);
            tank.setReLv(0);
            TankBiz tankBiz = SpringUtil.getBean(TankBiz.class);
            tank.initSkill(tankSetting.getSkillList());
            tankBiz.checkTankSkill(tank);
        }

        @Override
        public List<Items> recastItems(Tank tank) {
            List<Items> itemsList = Lists.newArrayList();
            if (checkRecast(tank)){
                TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
                int itemId = SpringUtil.getBean(CommValueConfig.class).getCommValue(CommonValueType.TankLvUpCostItemId);
                TankLevelTemplate tankLevelTemplate = tankConfig.getTankLevelTemplate(tank.getLv());
                // 等级消耗
                itemsList.add(new Items(itemId, tankLevelTemplate.getExp_total(), ItemType.ITEM));
                ReformTemplate reformTemplate = tankConfig.getTankReformTemplate(tank);
                // 突破消耗
                itemsList.addAll(reformTemplate.getTotalCost());
            }
            return itemsList;
        }
    },
    DriverRecast(2,"兽魂重铸"){
        @Override
        public boolean checkRecast(Tank tank) {
            return tank.getDriver().getLv() > 0;
        }

        @Override
        public void doRecast(Tank tank) {
            TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
            tank.initDriver(tankConfig.getDriverTemplate(tank.getId()));
        }

        @Override
        public List<Items> recastItems(Tank tank) {
            List<Items> itemsList = Lists.newArrayList();
            if (checkRecast(tank)){
                TankConfig tankConfig = SpringUtil.getBean(TankConfig.class);
                DriverLvTemplate driverLvTemplate = tankConfig.getDriverLvTemplate(tank);
                if (driverLvTemplate.getTotalCostSoul() > 0){
                    // 兽魂
                    int soulId = tankConfig.getDriverTemplate(tank.getId()).getSoul();
                    itemsList.add(new Items(soulId, driverLvTemplate.getTotalCostSoul(),ItemType.ITEM.getType()));
                }
                // 道具
                itemsList.addAll(driverLvTemplate.getTotalCostItems());
                DriverTemplate driverTemplate = tankConfig.getDriverTemplate(tank.getId());
                // 觉醒
                itemsList.addAll(driverTemplate.getRecastEvolveReward(tank.getDriver().getEvolveLv()));
            }
            return itemsList;
        }
    },
    ;

    private int type;
    private String desc;
    
    public abstract boolean checkRecast(Tank tank);
    public abstract void doRecast(Tank tank);
    public abstract List<Items> recastItems(Tank tank);

    TankRecastType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
    
    public static TankRecastType getType(int type) {
		for (TankRecastType stateType : TankRecastType.values()) {
			if(stateType.getType() == type) {
				return stateType;
			}
		}
		return null;
	}
}
