package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.temlate.ActiveSoldierBackGiftTemplate;
import com.hm.config.excel.templaextra.ActiveSoldierBackProjectImpl;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: ActiveSoldierConfig 
 * @Description: 老兵回归
 * @author zxj
 * @date 2019年11月11日 下午4:07:33 
 *
 */
@Config
public class ActiveSoldierConfig extends ExcleConfig {
   
    //跨服老兵回归，购买（rechargeid，obj）
    private Map<Integer, ActiveSoldierBackGiftTemplate> soldierGiftMap = Maps.newConcurrentMap();
    //跨服老兵回归，领取奖励
    private Map<Integer, ActiveSoldierBackProjectImpl> kfRewardMap = Maps.newConcurrentMap();

    public ActiveSoldierBackProjectImpl getKfReward(int id) {
        return kfRewardMap.getOrDefault(id, null);
    }

    @Override
    public void loadConfig() {
        //老兵回归，礼包
       /* List<ActiveSoldierBackGiftTemplate> listBackGift = JSONUtil.fromJson(getJson(ActiveSoldierBackGiftTemplate.class), new TypeReference<List<ActiveSoldierBackGiftTemplate>>() {
        });
        Map<Integer, ActiveSoldierBackGiftTemplate> tempBackGiftMap = Maps.newConcurrentMap();
        listBackGift.forEach(e -> {
            tempBackGiftMap.put(e.getRecharge_id(), e);
        });
        soldierGiftMap = ImmutableMap.copyOf(tempBackGiftMap);
        
        //跨服老兵回归，奖励领取
        List<ActiveSoldierBackProjectImpl> listProject = JSONUtil.fromJson(getJson(ActiveSoldierBackProjectImpl.class), new TypeReference<List<ActiveSoldierBackProjectImpl>>() {
        });
        Map<Integer, ActiveSoldierBackProjectImpl> tempProjectMap = Maps.newConcurrentMap();
        listProject.forEach(e -> {
            e.init();
            tempProjectMap.put(e.getId(), e);
        });
        kfRewardMap = ImmutableMap.copyOf(tempProjectMap);*/
    }

    public ActiveSoldierBackGiftTemplate getGift(int rechargeId) {
    	return soldierGiftMap.get(rechargeId);
    }
    
    @Override
    public List<String> getDownloadFile() {
        return getConfigName(ActiveSoldierBackProjectImpl.class, ActiveSoldierBackGiftTemplate.class);
    }
    
    public int kfRewardSize() {
    	return kfRewardMap.size();
    }
}





