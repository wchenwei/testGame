package com.hm.model.guild.tactics;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.hm.enums.GuildTacticsType;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.guild.Guild;
import com.hm.model.guild.tactics.vo.BaseGuildTacticsVo;
import com.hm.model.guild.tactics.vo.FinanceTaticsVo;
import com.hm.model.item.Items;
import com.hm.model.item.WeightItem;
import com.hm.model.player.Player;
import com.hm.util.ItemUtils;
import com.hm.util.RandomUtils;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 敌对部落使用金融战
 * @author siyunlong  
 * @date 2019年1月17日 上午10:12:09 
 * @version V1.0
 */
@NoArgsConstructor
public class FinanceTatics extends AbstractCityTactics{
	private Items items;

	public FinanceTatics(int second) {
		super(GuildTacticsType.FinanceTatics, second);
	}

	@Override
	public boolean useCityTactics(Player player, Guild guild, WorldCity worldCity) {
		guild.getGuildTactics().addGuildTactics(this);
		return true;
	}
	
	@Override
	public void loadLvValue(String value) {
        List<WeightItem> weightItems = Lists.newArrayList();
        for (String temp : value.split(",")) {
        	weightItems.add(ItemUtils.str2WeightItem(temp, ":"));
		}
        Map<Items, Integer> rewardMap =  weightItems.stream().collect(Collectors.toMap(WeightItem::getItems, WeightItem::getWeight, (a, b) -> b));
		this.items = RandomUtils.buildWeightMeta(rewardMap).random();
	}

	public Items getItems() {
		return items;
	}

	public String getStartDate(){
        return DateUtil.date(this.getStartTime()).toDateStr();
    }

	@Override
	public BaseGuildTacticsVo createGuildTacticsVo() {
		return new FinanceTaticsVo(this);
	}

    public static void main(String[] args) {
        System.out.println(DateUtil.date(System.currentTimeMillis()).toDateStr());
    }
}
