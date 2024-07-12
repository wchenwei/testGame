package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.TankSpecialityNameTemplate;
import com.hm.model.tank.TankAttr;
import com.hm.util.StringUtil;

/**
 * ClassName: TankSpecNameTemplateImpl. <br/>  
 * Function: 坦克专精. <br/>  
 * date: 2019年7月18日 下午5:05:55 <br/>  
 * @author zxj  
 * @version
 */
@FileConfig("tank_speciality_name")
public class TankSpecNameTemplateImpl extends TankSpecialityNameTemplate{
	
	private TankAttr tankAttr = new TankAttr();
	
	public void init() {
		if(!StringUtil.isNullOrEmpty(this.getAttribute())) {
			String[] strArr = this.getAttribute().split(",");
			for(String temp : strArr) {
				double[] attr =StringUtil.strToDoubleArray(temp, ":");
				tankAttr.addAttr(new Double(attr[0]).intValue(), attr[1]);
			}
		}
	}

	public TankAttr getTankAttr() {
		return tankAttr;
	}
}
