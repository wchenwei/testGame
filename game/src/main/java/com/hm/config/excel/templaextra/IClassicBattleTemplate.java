package com.hm.config.excel.templaextra;

import com.hm.model.item.Items;

import java.util.List;

/**
 * 经典战役配置接口
 * @author Administrator
 *
 */
public interface IClassicBattleTemplate {
	public List<Items> getRewards();
	public int getTankExp();
	public int getPlayerExp();
	public int getStartWave();
	public int getMinCombat();
	public List<Items> getCheckRewards();
	public List<Items> checkRewards(List<Items> rewards);
}
