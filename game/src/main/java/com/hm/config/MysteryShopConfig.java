package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.templaextra.MysteryShopTemplate;
import com.hm.model.player.Player;
import com.hm.util.MathUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:神秘商店配置
 * User: xjt
 * Date: 2019年11月6日14:24:24
 */

@Config
public class MysteryShopConfig extends ExcleConfig {
    private Map<Integer, MysteryShopTemplate> map = Maps.newHashMap();

    @Override
    public void loadConfig() {
        List<MysteryShopTemplate> inviteTaskList = JSONUtil.fromJson(getJson(MysteryShopTemplate.class), new TypeReference<List<MysteryShopTemplate>>() {
        });
        inviteTaskList.forEach(t ->{
        	t.init();
        });
        map = ImmutableMap.copyOf(inviteTaskList.stream().collect(Collectors.toMap(MysteryShopTemplate::getId, Function.identity())));
    }

    
    public MysteryShopTemplate getMysteryShop(int id) {
    	return this.map.get(id);
    }
    
    public List<Integer> createShop(Player player){
    	List<Integer> goodsIds = Lists.newArrayList();
    	int lv = player.playerLevel().getLv();
    	for(int type=1;type<=3;type++){
    		goodsIds.add(randomGoodsId(type, lv));
    	}
    	return goodsIds;
    }
    //随机商品
    public int randomGoodsId(int type,int lv){
    	List<Integer> ids = this.map.values().stream().filter(t ->t.getType()==type&&t.isFit(lv)).map(t ->t.getId()).collect(Collectors.toList());
    	return ids.get(MathUtils.random(0, ids.size()));
    }
    

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(
        		MysteryShopTemplate.class
        );
    }
}
