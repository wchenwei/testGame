package com.hm.model.player;

import com.hm.libcore.spring.SpringUtil;
import com.hm.config.excel.AircraftCarrierConfig;
import com.hm.libcore.mongodb.PrimaryKeyWeb;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Aircraft {
	private String uid; //唯一id
    private int id;  //飞机id
    private int star=1;
    
    public Aircraft(int id,int serverId) {
        this.id = id;
        uid = serverId+"_"+PrimaryKeyWeb.getPrimaryKey("Aircraft",serverId);
        star = 1;
    }
    
    public void starUp(int star) {
    	//根据飞机获取飞机能升的最大星级
    	AircraftCarrierConfig config = SpringUtil.getBean(AircraftCarrierConfig.class);
    	int maxStar = config.getMaxStar(id);
    	this.star = Math.min(maxStar, this.star+star);
    }
    
}
