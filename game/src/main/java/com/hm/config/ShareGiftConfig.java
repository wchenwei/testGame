package com.hm.config;//package com.hm.config;
//
//import com.alibaba.fastjson.TypeReference;
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.Maps;
//import com.hm.libcore.annotation.Config;
//import com.hm.libcore.json.JSONUtil;
//import com.hm.config.excel.ExcleConfig;
//import com.hm.config.excel.templaextra.WechatShareGiftTemplateImpl;
//import com.hm.config.excel.templaextra.WechatShareTimeImpl;
//import com.hm.util.RandomUtils;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * @author wyp
// * @description
// * @date 2021/1/11 8:57
// */
//@Config
//public class ShareGiftConfig extends ExcleConfig {
//    private Map<Integer,WechatShareGiftTemplateImpl> map = Maps.newConcurrentMap();
//
//    private Map<Integer,WechatShareTimeImpl> mapShareTime = Maps.newConcurrentMap();
//    private Map<Integer, Integer> wmWeight = Maps.newConcurrentMap();
//
//    @Override
//    public void loadConfig() {
//        loadShopGoodsConfig();
//        loadWechatShareTime();
//    }
//
//    @Override
//    public List<String> getDownloadFile() {
//        return getConfigName(WechatShareGiftTemplateImpl.class, WechatShareTimeImpl.class);
//    }
//
//    private void loadShopGoodsConfig() {
//        Map<Integer,WechatShareGiftTemplateImpl> mapGift = Maps.newConcurrentMap();
//        for(WechatShareGiftTemplateImpl template: JSONUtil.fromJson(getJson(WechatShareGiftTemplateImpl.class), new TypeReference<ArrayList<WechatShareGiftTemplateImpl>>(){})){
//            template.init();
//            mapGift.put(template.getId(), template);
//        }
//        this.map = ImmutableMap.copyOf(mapGift);
//    }
//
//    private void loadWechatShareTime() {
//        Map<Integer,WechatShareTimeImpl> mapShare = Maps.newConcurrentMap();
//        for(WechatShareTimeImpl template: JSONUtil.fromJson(getJson(WechatShareTimeImpl.class), new TypeReference<ArrayList<WechatShareTimeImpl>>(){})){
//            template.init();
//            mapShare.put(template.getId(),template);
//        }
//        this.mapShareTime = ImmutableMap.copyOf(mapShare);
//        Map<Integer, Integer> wm = mapShareTime.values().stream().collect(Collectors.toMap(WechatShareTimeImpl::getId, WechatShareTimeImpl::getRate));
//        this.wmWeight = ImmutableMap.copyOf(wm);
//    }
//
//    /**
//     * @description
//     *          根据权重随机获取奖励
//     * @return com.hm.config.excel.templaextra.WechatShareTimeImpl
//     * @author wyp
//     * @date 2021/1/12 14:14
//     */
//    public WechatShareTimeImpl getTemplate(){
//        Integer randomId = RandomUtils.buildWeightMeta(wmWeight).random();
//        WechatShareTimeImpl wechatShareTime = mapShareTime.getOrDefault(randomId, null);
//        return wechatShareTime;
//    }
//
//
//    public WechatShareGiftTemplateImpl getReward(List<Integer> drawIds){
//        List<WechatShareGiftTemplateImpl> collect = map.values().stream()
//                  .filter(e -> !drawIds.contains(e.getId()))
//                  .sorted(Comparator.comparing(WechatShareGiftTemplateImpl::getId))
//                  .collect(Collectors.toList());
//
//        WechatShareGiftTemplateImpl shareGiftTemplate = collect.get(0);
//        if(Objects.isNull(shareGiftTemplate)){
//            return null;
//        }
//        List<Integer> ids = shareGiftTemplate.getIds();
//        ids.removeAll(drawIds);
//
//        int index = RandomUtils.randomInt(ids.size());
//        WechatShareGiftTemplateImpl template = map.get(ids.get(index));
//        return template;
//    }
//
//    public boolean checkCanReceive(List usedList){
//        return this.map.values().size() > usedList.size();
//    }
//
//}
