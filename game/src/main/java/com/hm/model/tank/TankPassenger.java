package com.hm.model.tank;

import com.google.common.collect.Lists;
import com.hm.model.passenger.Passenger;
import com.hm.model.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TankPassenger {
	private String passengers[] = new String[]{"","","","","",""}; //乘员
	private List<Integer> skillIds = Lists.newArrayList();
	
	public String[] getpassengers(){
		return passengers;
	}

	public void calSkillIds(List<Integer> skillIds) {
		this.skillIds = skillIds;
	}

	public TankPassengerVo createPassengerVo(Player player) {
		Passenger[] passengers = new Passenger[6];
		for(int i=0;i<6;i++){
			String uid = this.passengers[i];
			passengers[i]=StringUtils.isNotBlank(uid)?player.playerPassenger().getPassenger(uid):null;
		}
		return new TankPassengerVo(passengers, this.skillIds);
	}

}
