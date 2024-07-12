package com.hm.config.excel;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveDiscountRandomTemplate;
import com.hm.config.excel.templaextra.ActiveDiscountShopImpl;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ActiveDiscountConfig 
 * @Description: 抽折扣活动
 * @author zxj
 * @date 2019年11月13日 下午2:44:38 
 */
@Config
public class ActiveDiscountConfig extends ExcleConfig {
   
    private Map<Integer, ActiveDiscountShopImpl> discountShopMap = Maps.newConcurrentMap();
    private List<ActiveDiscountRandomTemplate> discountRandomList = Lists.newArrayList();

    public ActiveDiscountShopImpl getDiscountShop(int id) {
        return discountShopMap.getOrDefault(id, null);
    }

    @Override
    public void loadConfig() {
    	/*//抽折扣活动，商店
        List<ActiveDiscountShopImpl> listDiscountShop = JSONUtil.fromJson(getJson(ActiveDiscountShopImpl.class), new TypeReference<List<ActiveDiscountShopImpl>>() {
        });
        Map<Integer, ActiveDiscountShopImpl> tempDiscountShopMap = Maps.newConcurrentMap();
        listDiscountShop.forEach(e -> {
            e.init();
            tempDiscountShopMap.put(e.getId(), e);
        });
        discountShopMap = ImmutableMap.copyOf(tempDiscountShopMap);
        
        //抽折扣活动，折扣随机规则
        List<ActiveDiscountRandomTemplate> listDiscountRandom = JSONUtil.fromJson(getJson(ActiveDiscountRandomTemplate.class), new TypeReference<List<ActiveDiscountRandomTemplate>>() {
        });
        discountRandomList=ImmutableList.copyOf(listDiscountRandom);*/
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveDiscountShopImpl.class, ActiveDiscountRandomTemplate.class);
    }
    
    public double getRandom(int times) {
    	double result = 1;
    	for(ActiveDiscountRandomTemplate random: discountRandomList) {
    		if(random.getTimes_down()<=times && random.getTimes_up()>=times) {
    			if(random.getDiscount_down()==random.getDiscount_up()) {
    				return random.getDiscount_down();
    			}
    			return RandomUtil.randomDouble(random.getDiscount_down(), random.getDiscount_up());
    		}
    	}
    	return result;
    }
}





