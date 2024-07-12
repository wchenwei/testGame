package com.hm.config.excel.templaextra;

import cn.hutool.core.util.StrUtil;
import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.PlayerTitleTemplate;
import com.hm.model.tank.TankAttr;

@FileConfig("player_title")
public class TitleTemplate extends PlayerTitleTemplate{
	private TankAttr tankAttr;//附加属性
	
	public void init() {
		TankAttr tankAttr = new TankAttr();
		String attrStr = getAttr();
		if(StrUtil.isNotEmpty(attrStr)) {
			String[] attrArr = attrStr.split(",");
			for(int i=0; i<attrArr.length; i++) {
				String tempAttrStr = attrArr[i];
				String[] tempStrArr = tempAttrStr.split(":");
				tankAttr.addAttr(Integer.parseInt(tempStrArr[0]), Double.parseDouble(tempStrArr[1]));
			}
		}
		this.tankAttr = tankAttr;
	}

	public TankAttr getTankAttr() {
		return tankAttr;
	}
	/**
	 * 是否是系统唯一称号
	 * @return
	 */
	public boolean isSystemOnly() {
		return this.getType() == 1;
	}
	
}
