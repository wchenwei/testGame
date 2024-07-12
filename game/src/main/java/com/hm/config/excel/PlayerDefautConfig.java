package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.action.item.ItemBiz;
import com.hm.config.excel.temlate.PlayerDefaultTemplate;
import com.hm.enums.ItemType;
import com.hm.enums.LogType;
import com.hm.model.player.Player;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Config
public class PlayerDefautConfig extends ExcleConfig{
	@Resource
    private ItemBiz itemBiz;
	private List<PlayerDefaultTemplate> defaultItemList = Lists.newArrayList();
	
	@Override
	public void loadConfig() {
		this.defaultItemList = ImmutableList.copyOf(loadFile());
	}
	
	private List<PlayerDefaultTemplate> loadFile() {
		return JSONUtil.fromJson(getJson(PlayerDefaultTemplate.class), new TypeReference<ArrayList<PlayerDefaultTemplate>>(){});
	}

	@Override
	public List<String> getDownloadFile() {
		return getConfigName(PlayerDefaultTemplate.class);
	}
	
	public void initNewPlayer(Player player) {
		for (PlayerDefaultTemplate template : defaultItemList) {
			itemBiz.addItem(player, ItemType.getType(template.getType()), template.getId(), template.getNum(), LogType.PLayer_Default);
		}
	}
	
}
