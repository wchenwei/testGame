package com.hm.config.excel.templaextra;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.FileConfig;
import com.hm.libcore.util.string.StringUtil;
import com.hm.config.excel.temlate.CityBaseTemplate;
import com.hm.enums.MoraleReduceType;
import com.hm.model.item.Items;
import com.hm.model.weight.Drop;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@FileConfig("city_base")
public class CityTemplate extends CityBaseTemplate{
	private List<Drop> dropList = Lists.newArrayList();
	private int npcLv;
	private List<Integer> linkCityIds = Lists.newArrayList();
	private List<Items> resourceItem = Lists.newArrayList();
	private List<Items> specialItem = Lists.newArrayList();
	private List<Items> releaseRewards =Lists.newArrayList();
	private List<Integer> typeNpcIndexs = Lists.newArrayList();
	/**
	 * 资源征收、基础产出。hour
	 */
	private List<Items> levyResPerHour = Lists.newArrayList();
	/**
	 * 6:61002:1:0.09,6:60002:1:0.04,6:60008:1:0.01,6:60007:1:0.02
	 */
	private transient Map<Items, Double> unitItemMap = Maps.newConcurrentMap();
	
	public void init(){
		List<String> dropStr = Lists.newArrayList(this.getTurn_card().split(";"));
		this.dropList = dropStr.stream().map(t -> new Drop(t)).collect(Collectors.toList());
		this.npcLv = getGuardin_level();
		this.linkCityIds = StringUtil.splitStr2IntegerList(this.getLink_city(), ",");
		this.resourceItem = ItemUtils.str2ItemList(this.getResource(), ",", ":");
		this.specialItem = ItemUtils.str2ItemList(this.getSpecial_resource(), ",", ":");
		this.releaseRewards = ItemUtils.str2ItemList(this.getRelease_reward(), ",", ":");
		this.typeNpcIndexs = StringUtil.splitStr2IntegerList(getGuardin_type(), ",");
		this.levyResPerHour = ItemUtils.str2DefaultItemImmutableList(getNew_levy_res());
		initUnitItemMap();
	}


	public List<Drop> getDropList() {
		return dropList;
	}

	public int getNpcLv() {
		return npcLv;
	}

	public int getNpcNum() {
		return this.typeNpcIndexs.size();
	}

	public List<Integer> getLinkCityIds() {
		return linkCityIds;
	}
	
	public List<Items> getResourceItem() {
		return resourceItem.stream().map(e->e.clone()).collect(Collectors.toList());
	}
	public List<Items> getSpecialItem() {
		return specialItem.stream().map(e->e.clone()).collect(Collectors.toList());
	}

	public List<Items> getReleaseRewards() {
		return releaseRewards.stream().map(e->e.clone()).collect(Collectors.toList());
	}

	public int getNpcType(int npcIndex) {
		int index = npcIndex%this.typeNpcIndexs.size();
		return this.typeNpcIndexs.get(index);
	}
	

	public boolean isBigCity() {
		return getCity_type() >= 2;
	}

	// 6:61002:1:0.09,6:60002:1:0.04,6:60008:1:0.01,6:60007:1:0.02
	private void initUnitItemMap() {
		unitItemMap.clear();
		for (String s : StrUtil.split(getNew_levy_item(), ",")) {
			String[] split = s.split(":");
			if (split.length != 4) {
				continue;
			}

			int type = Convert.toInt(split[0]);
			int id = Convert.toInt(split[1]);
			long count = Convert.toLong(split[2]);
			Double unit = Convert.toDouble(split[3]);
			if (unit > 0) {
				Items items = new Items(id, count, type);
				unitItemMap.put(items, unit);
			}
		}
	}

	// 新征收稀有道具掉落（分）
	public List<Items> getLevyItems(int minutes) {
		List<Items> result = Lists.newArrayList();
		for (Map.Entry<Items, Double> entry : unitItemMap.entrySet()) {
			long cnt = calcUnit(minutes, entry.getValue());
            if (cnt <= 0) {
				continue;
            }
			Items clone = entry.getKey().clone();
			if (cnt > 1) {
				clone.addCountRate(cnt - 1);
			}
			result.add(clone);
        }

		return result;
	}

	/**
	 * 1. 计算总概率P = 配置概率 x 累积分钟数
	 * 2. 算出来的P 分为整数部分P1 和 小数部分P2
	 * 3. 按照小数部分概率P2 取随（0,1）随机数，小于等于P2, 则本次稀有道具产出数量 = P1 + 1  否则 = P1
	 * @param n
	 * @param r
	 * @return
	 */
	private static long calcUnit(int n, double r) {
		double v = n * r;
		long result = (long)v;
		if (RandomUtils.randomIsRate(v%1)) {
			result++;
		}
		return result;
	}


	public boolean isPortCity() {
		return getSeaside() == 1;
	}

	public boolean isSameArea(CityTemplate toTemplate) {
		return getArea() == toTemplate.getArea();
	}
}
