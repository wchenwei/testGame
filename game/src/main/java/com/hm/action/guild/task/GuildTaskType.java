package com.hm.action.guild.task;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.CityConfig;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.config.excel.templaextra.GuildTaskTypeTemplate;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.item.Items;
import com.hm.model.player.CurrencyKind;
import com.hm.observer.ObservableEnum;
import com.hm.war.sg.troop.AbstractFightTroop;
import com.hm.war.sg.troop.PlayerTroop;

import java.util.Arrays;
import java.util.List;

public enum GuildTaskType {
    Error(0, "error") {
        @Override
        public int chkEffect(GuildTaskTypeTemplate typeTemplate,ObservableEnum observableEnum, Object... argv) {
            return 0;
        }
        
        @Override
        public boolean isFitObservableEnum(ObservableEnum observableEnum) {
        	return false;
        }
    },
    Task1(1, "杀敌任务") {
        @Override
        public int chkEffect(GuildTaskTypeTemplate typeTemplate,ObservableEnum observableEnum, Object... argv) {
        	boolean isWin = Boolean.parseBoolean(argv[2].toString());
        	if(isWin) {
        		AbstractFightTroop defFightTroop = (AbstractFightTroop)argv[5];
        		int index = defFightTroop instanceof PlayerTroop ? 0:1;
        		return typeTemplate.getKillPoint(index);
        	} 
            return 0;
        }
        
        @Override
        public boolean isFitObservableEnum(ObservableEnum observableEnum) {
        	return ObservableEnum.CityBattleResult == observableEnum;
        }
    },
    
    Task2(2, "商店消耗对应货币") {
        @Override
        public int chkEffect(GuildTaskTypeTemplate typeTemplate,ObservableEnum observableEnum, Object... argv) {
        	int shopId = Integer.parseInt(argv[0].toString());
        	double shopRate = typeTemplate.getShopRate(shopId);
        	if(shopRate > 0) {
        		List<Items> costs = (List<Items>)argv[4];
        		long val = costs.stream().mapToLong(e -> e.getCount()).sum();
        		return (int)(shopRate*val);
        	}
            return 0;
        }
        
        @Override
        public boolean isFitObservableEnum(ObservableEnum observableEnum) {
        	return ObservableEnum.ShopBuy == observableEnum;
        }
    },
    
    Task3(3, "占领城市") {
        @Override
        public int chkEffect(GuildTaskTypeTemplate typeTemplate,ObservableEnum observableEnum, Object... argv) {
        	boolean isMainCity = (boolean)argv[2];
        	if(isMainCity) {
        		return typeTemplate.getCityPoint(2);//主城积分
        	}
        	CityConfig cityConfig = SpringUtil.getBean(CityConfig.class);
        	WorldCity worldCity =  (WorldCity)argv[0];
        	CityTemplate cityTemplate = cityConfig.getCityById(worldCity.getId());
        	int index = cityTemplate != null && cityTemplate.isBigCity() ? 1:0;
            return typeTemplate.getCityPoint(index);
        }
        
        @Override
        public boolean isFitObservableEnum(ObservableEnum observableEnum) {
        	return ObservableEnum.OccupyCity == observableEnum;
        }
    },
    
    Task4(4, "消耗钞票") {
        @Override
        public int chkEffect(GuildTaskTypeTemplate typeTemplate,ObservableEnum observableEnum, Object... argv) {
        	if((int)argv[0] == CurrencyKind.Cash.getIndex()) {
        		return typeTemplate.getResRate((long)argv[1]);
        	}
            return 0;
        }
        
        @Override
        public boolean isFitObservableEnum(ObservableEnum observableEnum) {
        	return ObservableEnum.CostRes == observableEnum;
        }
    },

    ;



    private int id;
    private String desc;

    GuildTaskType(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }
    
    public abstract int chkEffect(GuildTaskTypeTemplate typeTemplate,ObservableEnum observableEnum, Object... argv);
    public abstract boolean isFitObservableEnum(ObservableEnum observableEnum);
    
    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }

    public static GuildTaskType num2enum(int num) {
        return Arrays.stream(GuildTaskType.values()).filter(e -> e.getId() == num).findAny().orElse(Task1);
    }
    
    public static int getMaxType() {
    	return Arrays.stream(GuildTaskType.values()).mapToInt(e -> e.getId()).max().orElse(3);
    }
}
