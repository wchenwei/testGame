package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.GameConstants;
import com.hm.config.excel.temlate.ActiveSmashEggTemplate;
import com.hm.model.item.Items;
import com.hm.util.PubFunc;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;

@FileConfig("active_smash_egg")
public class ActiveJzEggTemplate extends ActiveSmashEggTemplate{
	private long startTime;
	private int trueItemId;
	private WeightMeta<Items> randomItems;
	
	public void init() {
		this.trueItemId = PubFunc.parseInt(getReward());
		if(this.trueItemId > 0) {
			this.startTime = toTime(getTime());
		}else{
			this.startTime = System.currentTimeMillis();
		}
		this.randomItems = RandomUtils.buildItemWeightMeta(getReward_item());
	}
	
	public long toTime(String mh) {
		return (getActive_id()-1)*GameConstants.DAY+Integer.parseInt(mh.split(":")[0])*GameConstants.HOUR
				+Integer.parseInt(mh.split(":")[1])*GameConstants.MINUTE;
	}
	
	public boolean isFitTime(long activeTime) {
		return trueItemId > 0 && activeTime > this.startTime;
	}

	public int getTrueItemId() {
		return trueItemId;
	}

	public WeightMeta<Items> getRandomItems() {
		return randomItems;
	}
	
	
}
