package com.hm.enums;

import com.google.common.collect.Lists;
import com.hm.config.excel.ActivityConfig;
import com.hm.libcore.spring.SpringUtil;
import com.hm.model.item.Items;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;

import java.util.List;

/**
 * Description:资源找回类型
 * User: xjt
 * Date: 2019年6月12日18:49:38
 */
public enum ResBackType {
    CityWar(2, "城战模块") {
		@Override
		public List<Items> getBackRes(Player player) {
//			HonorBiz honorBiz = SpringUtil.getBean(HonorBiz.class);
//			return honorBiz.getResBack(player);
			return Lists.newArrayList();
		}

		@Override
		public List<Items> getBackResForDay(Player player, int day) {
//			HonorBiz honorBiz = SpringUtil.getBean(HonorBiz.class);
//			return honorBiz.getResBack(player,day);
			return Lists.newArrayList();
		}
	},
    Supply(3, "每日补给模块") {
		@Override
		public List<Items> getBackRes(Player player) {
			ActivityConfig activityConfig = SpringUtil.getBean(ActivityConfig.class);
			return activityConfig.getArmyFeteReward(player.playerLevel().getLv());
		}

		@Override
		public List<Items> getBackResForDay(Player player, int day) {
			ActivityConfig activityConfig = SpringUtil.getBean(ActivityConfig.class);
			return ItemUtils.calItemRateReward(activityConfig.getArmyFeteReward(player.playerLevel().getLv()),day*3);
		}
	},


    ;

    private int type;
    private String desc;

    ResBackType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
    public static ResBackType getBackType(int type){
    	for(ResBackType resBackType:ResBackType.values()){
    		if(resBackType.getType()==type){
    			return resBackType;
    		}
    	}
    	return null;
    }
    public abstract List<Items> getBackRes(Player player);
    public abstract List<Items> getBackResForDay(Player player,int day);

}
