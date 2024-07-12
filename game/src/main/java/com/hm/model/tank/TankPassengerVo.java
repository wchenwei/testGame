package com.hm.model.tank;

import com.google.common.collect.Lists;
import com.hm.model.passenger.Passenger;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TankPassengerVo {
	private Passenger passengers[] = new Passenger[6]; //乘员
	private List<Integer> skills = Lists.newArrayList();
	
	

}
