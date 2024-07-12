package com.hm.war.sg.troop;

import com.hm.model.tank.TankVo;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StrangeTank {
	private TankVo tankInfo;
	
	public StrangeTank(TankVo tankInfo) {
		super();
		this.tankInfo = tankInfo;
	}
	
	public int getTankId() {
		if(tankInfo != null) {
			return tankInfo.getId();
		}
		return 0;
	}
}
