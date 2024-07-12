/**  
 * Project Name:SLG_GameHot.  
 * File Name:tank.java  
 * Package Name:com.hm.model.tank  
 * Date:2018年3月5日下午2:20:31  
 * Copyright (c) 2018, 北京中科奥科技有限公司 All Rights Reserved.  
 *  
*/  
  
package com.hm.model.tank;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**  
 * ClassName: tank. <br/>  
 * Function: TODO ADD FUNCTION. <br/>  
 * Reason: TODO ADD REASON(可选). <br/>  
 * date: 2018年3月5日 下午2:20:31 <br/>  
 *  
 * @author yanpeng  
 * @version   
 */
@Data
@NoArgsConstructor
public class Driver{
	private int id; 
	private int lv; // 等级
	private int evolveLv;// 进化等级
	private Map<Integer, Double> attrMap = Maps.newHashMap(); //车长属性
	private Map<Integer, Integer> advance = Maps.newHashMap(); //军职<等级,筛选属性>

	public Driver(int id){
		this.id = id;
		//默认1级
		advance.put(1, 0);
	}

	public void addEvolveLv(){
		this.evolveLv ++;
	}

	public void cleanAdvance(int tankId) {
		advance.clear();
	}
	
	public int getAdvanceMaxLv() {
		return advance.keySet().stream().reduce(Integer::max).orElse(1);
	}
	
	public void advanceLvup(int chooseId) {
		int maxLv = getAdvanceMaxLv();
		advance.put(maxLv, chooseId);
		advance.put(maxLv+1, 0);
	}
}
  
