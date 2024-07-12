package com.hm.model.weight;

import com.google.common.collect.Maps;
import com.hm.libcore.util.weight.WeightRandom;
import com.hm.util.RandomUtils;
import com.hm.util.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

public class GoodsDrop {
	private WeightRandom<Integer> weightRandom;
	private int itemSize;
	
	
	public GoodsDrop() {
		super();
	}
	
	public GoodsDrop(String str,String weightStr) {
		super();
		this.init(str,weightStr);
	}
	
	public GoodsDrop(String str,String weightStr,List<Integer> filterIds) {
		super();
		this.init(str,weightStr,filterIds);
	}

	public void init(String str,String weightStr) {
		if(StringUtils.isNotBlank(str)){
			Map<Integer,Integer> weightMap = Maps.newHashMap();
			int [] goodsIdArrary = StringUtil.strToIntArray(str, ",");
			int [] weightArrary = StringUtil.strToIntArray(weightStr, ",");
			
			for (int i=0;i<goodsIdArrary.length;i++) {
				weightMap.put(goodsIdArrary[i], weightArrary[i]);
				itemSize ++;
			}
			this.weightRandom = RandomUtils.buildWeightRandom(weightMap);
		}
	}
	
	public void init(String str,String weightStr,List<Integer> filterIds) {
		if(StringUtils.isNotBlank(str)){
			Map<Integer,Integer> weightMap = Maps.newHashMap();
			int [] goodsIdArrary = StringUtil.strToIntArray(str, ",");
			int [] weightArrary = StringUtil.strToIntArray(weightStr, ",");
			for (int i=0;i<goodsIdArrary.length;i++) {
				if(!filterIds.contains(goodsIdArrary[i])){
					weightMap.put(goodsIdArrary[i], weightArrary[i]);
					itemSize ++;
				}
			}
			this.weightRandom = RandomUtils.buildWeightRandom(weightMap);
		}
	}
	
	public int getItemSize() {
		return itemSize;
	}

	public int randomDropId() {
		if(weightRandom != null) {
			int result = weightRandom.next();
			return result;
		}
		return 1;
	}
}
