package com.hm.config.excel;

import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.*;
import com.hm.config.excel.temlate.Active0801GiftTemplate;
import com.hm.config.excel.templaextra.*;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.util.RandomUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
/**
 * 
 * @ClassName:  Activity0801Config   
 * @Description:新版81活动
 * @author: zxj
 * @date:   2020年7月15日 下午3:36:46   
 *
 */
@Config
public class Activity0801Config extends ExcleConfig {
    /**
     * Active0801PrayRewardTemplateImpl::getId : Active0801PrayRewardTemplateImpl
     */
    private Map<Integer, Active0801PrayRewardTemplateImpl> prayRewardMap = Maps.newConcurrentMap();
    private Map<Integer, Active0801SpeaksignTemplateImpl> speaksignMap = Maps.newConcurrentMap();

    private Map<Integer, Active0801PrayTargetTemplateImpl> prayTargetMap = Maps.newConcurrentMap();
    /**
     * Active0801ShopTemplateImpl::getId : Active0801ShopTemplateImpl
     */
    private Map<Integer, Active0801ShopTemplateImpl> shopMap = Maps.newConcurrentMap();

    private List<Active0801TreasureRateFirstTemplateImpl> rateFirstList = Lists.newArrayList();
    private List<Active0801TreasureRateTemplateImpl> rateList = Lists.newArrayList();

    private Map<Integer,Active0801SignTemplateImpl> signMap = Maps.newConcurrentMap();
    //期数，计费点id，礼包信息
    private Table<Integer, Integer, Active0801GiftTemplate> giftTable = HashBasedTable.create();

    /**
     * 随机祈福奖励
     *
     * @param id Active0801PrayRewardTemplateImpl::getId
     * @return
     */
    public Active0801PrayRewardTemplateImpl calcPrayRewards(int id) {
        return prayRewardMap.getOrDefault(id, null);
    }

    public List<Integer> getSpeaksign(int stage, int days, int playerLv) {
        return speaksignMap.values().stream().filter(e->e.getStage() == stage && days >= e.getDay())
                .filter(e-> e.checkPlayerLv(playerLv))
                .map(Active0801SpeaksignTemplateImpl::getId)
                .collect(Collectors.toList());
    }

    public Active0801ShopTemplateImpl getShopCfg(int id) {
        return shopMap.getOrDefault(id, null);
    }
    
    public Active0801PrayTargetTemplateImpl getPrayTarget(int id) {
    	return prayTargetMap.getOrDefault(id, null);
    }

    /**
     * 获取赠送比例
     *
     * @param value
     * @return
     */
    public double getTreasureRate(int value) {
        Active0801TreasureRateTemplateImpl cfg = rateList.stream().
                filter(t -> t.getRecharge_down() <= value && value <= t.getRecharge_up()).
                findFirst().orElse(null);
        if (cfg == null) {
            return 0;
        }
        double v;
        // 上下限一样的
        if (cfg.getRate_down().equals(cfg.getRate_up())) {
            v = cfg.getRate_down();
        } else {
            v = RandomUtils.randomDouble(cfg.getRate_down(), cfg.getRate_up());
        }
        return v * cfg.getWeightMeta().random();
    }

    /**
     * 获取赠送比例(首轮)
     *
     * @param value
     * @return
     */
    public double getTreasureRateFirst(int value) {
        Active0801TreasureRateFirstTemplateImpl cfg = rateFirstList.stream().
                filter(t -> t.getRecharge_down() <= value && value <= t.getRecharge_up()).
                findFirst().orElse(null);
        if (cfg == null) {
            return 0;
        }
        double v;
        if (cfg.getRate_down().equals(cfg.getRate_up())) {
            v = cfg.getRate_down();
        } else {
            v = RandomUtils.randomDouble(cfg.getRate_down(), cfg.getRate_up());
        }
        return v * cfg.getWeightMeta().random();
    }
    
    public Active0801SignTemplateImpl getSign(int id){
		return signMap.get(id);
	}
    //根据玩家等级和天数找出对应的template
  	public Active0801SignTemplateImpl getSign(int playerLv, int day, int stage) {
  		Optional<Active0801SignTemplateImpl> optional = signMap.values().stream()
                .filter(t ->playerLv>=t.getLv_down()
                        &&playerLv<=t.getLv_up()
                        &&day==t.getDay()
                        &&t.getStage()==stage).findFirst();
  		if(optional.isPresent()){
  			return optional.get();
  		}
  		return null;
  	}

    public Active0801GiftTemplate getGiftid(Integer id, int stage) {
        return giftTable.get(stage, id);
    }

    // ===================================================================

    @Override
    public void loadConfig() {
        // =====================================================================
        /*Map<Integer, Active0801PrayRewardTemplateImpl> tmpPrayRewardMap = Maps.newConcurrentMap();
        List<Active0801PrayRewardTemplateImpl> list1 = JSONUtil.fromJson(getJson(Active0801PrayRewardTemplateImpl.class), new TypeReference<List<Active0801PrayRewardTemplateImpl>>() {
        });
        list1.forEach(e -> {
            e.init();
            tmpPrayRewardMap.put(e.getId(), e);
        });
        prayRewardMap = ImmutableMap.copyOf(tmpPrayRewardMap);
        // =====================================================================
        Map<Integer, Active0801PrayTargetTemplateImpl> tmpPrayTargetMap = Maps.newConcurrentMap();
        List<Active0801PrayTargetTemplateImpl> listTarget = JSONUtil.fromJson(getJson(Active0801PrayTargetTemplateImpl.class), new TypeReference<List<Active0801PrayTargetTemplateImpl>>() {
        });
        listTarget.forEach(e -> {
            e.init();
            tmpPrayTargetMap.put(e.getId(), e);
        });
        prayTargetMap = ImmutableMap.copyOf(tmpPrayTargetMap);
        // =====================================================================
        Map<Integer, Active0801ShopTemplateImpl> tmpShopMap = Maps.newConcurrentMap();
        List<Active0801ShopTemplateImpl> list3 = JSONUtil.fromJson(getJson(Active0801ShopTemplateImpl.class), new TypeReference<List<Active0801ShopTemplateImpl>>() {
        });
        list3.forEach(e -> {
            e.init();
            tmpShopMap.put(e.getId(), e);
        });
        shopMap = ImmutableMap.copyOf(tmpShopMap);
        // =====================================================================
        List<Active0801TreasureRateFirstTemplateImpl> list4 = JSONUtil.fromJson(getJson(Active0801TreasureRateFirstTemplateImpl.class), new TypeReference<List<Active0801TreasureRateFirstTemplateImpl>>() {
        });

        list4.forEach(Active0801TreasureRateFirstTemplateImpl::init);
        rateFirstList = ImmutableList.copyOf(list4);
        // =====================================================================
        List<Active0801TreasureRateTemplateImpl> list5 = JSONUtil.fromJson(getJson(Active0801TreasureRateTemplateImpl.class), new TypeReference<List<Active0801TreasureRateTemplateImpl>>() {
        });

        list5.forEach(Active0801TreasureRateTemplateImpl::init);
        rateList = ImmutableList.copyOf(list5);
        // =====================================================================
        List<Active0801SignTemplateImpl> list = JSONUtil.fromJson(getJson(Active0801SignTemplateImpl.class), new TypeReference<ArrayList<Active0801SignTemplateImpl>>(){});
		list.forEach(t -> t.init());
		Map<Integer,Active0801SignTemplateImpl> signMap = list.stream().collect(Collectors.toMap(Active0801SignTemplateImpl::getId, Function.identity()));
		this.signMap = ImmutableMap.copyOf(signMap);
        loadGiftConfig();
        loadSpeaksign();*/
    }
    private void loadGiftConfig() {
        List<Active0801GiftTemplate> list = JSONUtil.fromJson(getJson(Active0801GiftTemplate.class), new TypeReference<List<Active0801GiftTemplate>>() {});
        Table<Integer, Integer, Active0801GiftTemplate> tempGiftTable = HashBasedTable.create();
        list.forEach(e->{
            tempGiftTable.put(e.getStage(), e.getRecharge_gift_id(), e);
        });
        giftTable = ImmutableTable.copyOf(tempGiftTable);
    }
    private void loadSpeaksign() {
        List<Active0801SpeaksignTemplateImpl> list = JSONUtil.fromJson(getJson(Active0801SpeaksignTemplateImpl.class), new TypeReference<List<Active0801SpeaksignTemplateImpl>>() {});
        Map<Integer, Active0801SpeaksignTemplateImpl> tmpShopMap = Maps.newConcurrentMap();
        list.forEach(e->{
            e.init();
            tmpShopMap.put(e.getId(), e);
        });
        speaksignMap = ImmutableMap.copyOf(tmpShopMap);
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(Active0801PrayTargetTemplateImpl.class,
        		Active0801SignTemplateImpl.class,
                Active0801PrayRewardTemplateImpl.class,
                Active0801ShopTemplateImpl.class,
                Active0801TreasureRateFirstTemplateImpl.class,
                Active0801TreasureRateTemplateImpl.class,
                Active0801GiftTemplate.class,
                Active0801SpeaksignTemplateImpl.class
        );
    }
}
