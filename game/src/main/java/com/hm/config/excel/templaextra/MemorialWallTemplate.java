package com.hm.config.excel.templaextra;

import com.hm.libcore.annotation.FileConfig;
import com.hm.config.excel.temlate.MuseumPhotoWallTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.tank.TankAttr;
import com.hm.util.TankAttrUtils;

import java.util.Map;

@FileConfig("museum_photo_wall")
public class MemorialWallTemplate extends MuseumPhotoWallTemplate{
	private TankAttr[] attrs;
	
	public void init() {
		String[] attrInfos = getArrti().split(";");
		this.attrs = new TankAttr[attrInfos.length];
		for (int i = 0; i < this.attrs.length; i++) {
			Map<TankAttrType, Double> tempMap = TankAttrUtils.str2TankAttrMap(attrInfos[i], ",", ":");
			TankAttr tankAttr = new TankAttr(tempMap);
			if(i > 0) {
				tankAttr.addAttr(this.attrs[i-1]);
			}
			this.attrs[i] = tankAttr;
		}
	}
	
	public TankAttr getTankAttr(int index) {
		if(index <= 0) {
			return null;
		}
		return this.attrs[index-1];
	}
	
	@Override
	public Integer getCost() {
		return this.attrs.length;
	}
	
	public int getWallIndex() {
		return getWall_id();
	}
}
