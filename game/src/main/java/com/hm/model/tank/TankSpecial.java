package com.hm.model.tank;

import lombok.Data;

@Data
public class TankSpecial {
	private int type =0;	//专精--类型，0表示未初始化;其他表示专精类型
	private int lv = 0;	//专精--等级
	private int times;			//专精--当天切换次数（每天重置）
	
	public void setType(int typein) {
		if(this.type!=0) {
			this.times += 1;
		}
		this.type = typein;
	}

	public void lvUpdate() {
		lv+=1;
	}
	/**
	 * resetTimes:(每天重置一下次数). <br/>  
	 * @author zxj    使用说明
	 */
	public void resetTimes() {
		this.times=0;
	}
}
