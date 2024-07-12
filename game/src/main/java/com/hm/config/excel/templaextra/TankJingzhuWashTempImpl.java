package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankJingzhuWashTemplate;
import com.hm.model.tank.TankAttr;
import com.hm.util.StringUtil;

@FileConfig("tank_jingzhu_wash")
public class TankJingzhuWashTempImpl extends TankJingzhuWashTemplate{
	private TankAttr tankAttr = new TankAttr();
	public void init(){
		double[] attrArr = StringUtil.strToDoubleArray(this.getAttri(), ":");
		if(attrArr.length==2) {
			tankAttr.addAttr(new Double(attrArr[0]).intValue(), attrArr[1]);
		}
	}
	//获取属性加成
	public TankAttr getTankAttr() {
		return this.tankAttr;
	}
	
	public TankAttr getTankAttr(float addTimes) {
		TankAttr result = new TankAttr();
		this.tankAttr.getAttrMap().forEach((key, value)->{
			result.addAttr(key, (1+addTimes)*value);
		});
		return result;
	}
}
