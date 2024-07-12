package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.lv.ILvValue;
import com.hm.config.excel.temlate.MilitaryProjectLevelTemplate;
import com.hm.model.tank.TankAttr;
import org.apache.commons.lang.StringUtils;

@FileConfig("military_project_level")
public class MilitaryProjectLevelImpl extends MilitaryProjectLevelTemplate implements ILvValue{

	private TankAttr tankAttr = new TankAttr();

	public void init() {
		String attrStr = this.getAttr();
		if(StringUtils.isNotEmpty(attrStr)) {
			String[] attrArr = attrStr.split(",");
			for(int i=0; i<attrArr.length; i++) {
				String tempAttrStr = attrArr[i];
				String[] tempStrArr = tempAttrStr.split(":");
				tankAttr.addAttr(Integer.parseInt(tempStrArr[0]), Double.parseDouble(tempStrArr[1]));
			}
		}
	}
	
	@Override
	public int getLv() {
		return this.getLevel();
	}
	
	@Override
	public long getExp() {
		return getExp_total();
	}

	public TankAttr getTankAttr() {
		return tankAttr;
	}

}
