package com.hm.config.excel.templaextra;

import com.google.common.collect.Lists;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.MuseumChapterTemplate;
import com.hm.enums.TankAttrType;
import com.hm.model.tank.TankAttr;
import com.hm.util.TankAttrUtils;

import java.util.List;
import java.util.Map;

@FileConfig("museum_chapter")
public class MemorialChapterTemplate extends MuseumChapterTemplate{
	private int[] photoNums;
	private int[] markVals;
	private TankAttr[] attrs;
	
	private List<MemorialWallTemplate> wallList = Lists.newArrayList();
	
	public void init() {
		this.photoNums = StringUtil.splitStr2IntArray(getPhoto_num(), ",");
		this.markVals = StringUtil.splitStr2IntArray(getMark_value(), ",");
		String[] attrInfos = getAttri().split(";");
		this.attrs = new TankAttr[this.photoNums.length];
		for (int i = 0; i < attrs.length; i++) {
			Map<TankAttrType, Double> tempMap = TankAttrUtils.str2TankAttrMap(attrInfos[i], ",", ":");
			TankAttr tankAttr = new TankAttr(tempMap);
			if(i > 0) {
				tankAttr.addAttr(this.attrs[i-1]);
			}
			this.attrs[i] = tankAttr;
		}
	}
	
	public TankAttr getTankAttr(int lv) {
		return this.attrs[lv-1];
	}

	public List<MemorialWallTemplate> getWallList() {
		return wallList;
	}
	
	public void addMemorialWallTemplate(MemorialWallTemplate template) {
		this.wallList.add(template);
	}
	
	public int getMarkvalue(int lv) {
		return this.markVals[lv-1];
	} 
	
	public int getMaxLv(int photoNum) {
		for (int i = this.photoNums.length-1; i >= 0; i--) {
			if(photoNum >= this.photoNums[i]) {
				return i+1;
			}
		}
		return 0;
	}
	
}
