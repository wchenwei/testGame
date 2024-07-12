package com.hm.config;

import com.alibaba.fastjson.TypeReference;
import com.hm.libcore.annotation.Config;
import com.hm.libcore.json.JSONUtil;
import com.hm.config.excel.ExcleConfig;
import com.hm.config.excel.temlate.SeaTradeBuildingTemplate;
import com.hm.config.excel.temlate.SeaTradeRoadNewTemplate;
import com.hm.config.excel.temlate.SeaTradeRoadTemplate;
import com.hm.config.excel.temlate.SeaTradeShipTemplate;
import com.hm.config.excel.templaextra.SeaTradeCityTemplateImpl;
import com.hm.config.excel.templaextra.SeaTradeItemTemplateImpl;
import com.hm.util.RandomUtils;
import com.hm.util.WeightMeta;
import com.hm.util.dijkstras.Graph;
import com.hm.util.dijkstras.Vertex;

import java.util.*;
import com.google.common.collect.*;
/**
 * Description:
 * User: yang xb
 * Date: 2019-01-10
 */
@Config
public class TradeConfig extends ExcleConfig {
    // 起、止、路径长度
    private Table<Integer, Integer, Integer> roadConfig = HashBasedTable.create();
    private Graph mapGraph = new Graph();
    // 特产id:object
    private Map<Integer, SeaTradeItemTemplateImpl> itemMap = Maps.newHashMap();
    // ship level:object
    private Map<Integer, SeaTradeShipTemplate> shipMap = Maps.newHashMap();
    // 船坞等级:object
    private Map<Integer, SeaTradeBuildingTemplate> buildingMap = Maps.newHashMap();
    // city id : object
    private Map<Integer, SeaTradeCityTemplateImpl> cityMap = Maps.newHashMap();

    /**
     * 改版后，随机终点城市id
     */
    private WeightMeta<Integer> roadCityWM;
    private Map<Integer, SeaTradeRoadNewTemplate> roadMap = Maps.newConcurrentMap();

    @Override
    public void loadConfig() {
//        List<SeaTradeRoadTemplate> list = JSONUtil.fromJson(getJson(SeaTradeRoadTemplate.class), new TypeReference<List<SeaTradeRoadTemplate>>() {
//        });
//
//        Table<Integer, Integer, Integer> t = HashBasedTable.create();
//        for (SeaTradeRoadTemplate s : list) {
//            t.put(s.getStart_city(), s.getEnd_city(), s.getLongth());
//        }
//
//        roadConfig = ImmutableTable.copyOf(t);
//        // =============================================================================================================
//        List<SeaTradeCityTemplateImpl> seaTradeCityTemplateList = JSONUtil.fromJson(getJson(SeaTradeCityTemplateImpl.class), new TypeReference<List<SeaTradeCityTemplateImpl>>() {
//        });
//
//        Map<Integer, SeaTradeCityTemplateImpl> tmpCityMap = Maps.newHashMap();
//        Graph graph = new Graph();
//        for (SeaTradeCityTemplateImpl s : seaTradeCityTemplateList) {
//            s.init();
//            Integer from = s.getId();
//            tmpCityMap.put(from, s);
//            List<Vertex> vertexList = new ArrayList<>();
//            s.getLinkCityList().forEach(to -> {
//                int roadLength = getRoadLength(from, to);
//                if (roadLength < Integer.MAX_VALUE) {
//                    Vertex vertex = new Vertex(to, roadLength);
//                    vertexList.add(vertex);
//                }
//            });
//            graph.addNeighbor(from, vertexList);
//        }
//        mapGraph = graph;
//        cityMap = ImmutableMap.copyOf(tmpCityMap);
//        // =============================================================================================================
//        List<SeaTradeItemTemplateImpl> ls1 = JSONUtil.fromJson(getJson(SeaTradeItemTemplateImpl.class), new TypeReference<List<SeaTradeItemTemplateImpl>>() {
//        });
//
//        Map<Integer, SeaTradeItemTemplateImpl> tmpItemMap = Maps.newHashMap();
//        ls1.forEach(tmp -> {
//            tmp.init();
//            tmpItemMap.put(tmp.getId(), tmp);
//        });
//
//        itemMap = ImmutableMap.copyOf(tmpItemMap);
//        // =============================================================================================================
//        List<SeaTradeShipTemplate> ls2 = JSONUtil.fromJson(getJson(SeaTradeShipTemplate.class), new TypeReference<List<SeaTradeShipTemplate>>() {
//        });
//
//        Map<Integer, SeaTradeShipTemplate> tmpShipMap = Maps.newHashMap();
//        ls2.forEach(template -> tmpShipMap.put(template.getLevel(), template));
//        shipMap = ImmutableMap.copyOf(tmpShipMap);
//        // =============================================================================================================
//        List<SeaTradeBuildingTemplate> ls3 = JSONUtil.fromJson(getJson(SeaTradeBuildingTemplate.class), new TypeReference<List<SeaTradeBuildingTemplate>>() {
//        });
//
//        Map<Integer, SeaTradeBuildingTemplate> tmpBuildingMap = Maps.newHashMap();
//        ls3.forEach(template -> tmpBuildingMap.put(template.getLevel(), template));
//        buildingMap = ImmutableMap.copyOf(tmpBuildingMap);
//        // =============================================================================================================
//        List<SeaTradeRoadNewTemplate> ls4 = JSONUtil.fromJson(getJson(SeaTradeRoadNewTemplate.class), new TypeReference<List<SeaTradeRoadNewTemplate>>() {
//        });
//        Map<Integer, Integer> m = Maps.newConcurrentMap();
//        Map<Integer, SeaTradeRoadNewTemplate> rM = Maps.newConcurrentMap();
//        for (SeaTradeRoadNewTemplate template : ls4) {
//            m.put(template.getId(), template.getWeight());
//            rM.put(template.getId(), template);
//        }
//        roadCityWM = RandomUtils.buildWeightMeta(m);
//        roadMap = ImmutableMap.copyOf(rM);
    }

    /**
     * 特产列表
     *
     * @return
     */
    public Set<Integer> getAllItem() {
        return itemMap.keySet();
    }

    /**
     * 随机一个运输物品的id
     *
     * @return
     */
    public Integer pickItemId() {
        int rr = new Random().nextInt(itemMap.keySet().size());
        int i = 0;
        for (Integer obj : itemMap.keySet()) {
            if (i == rr) {
                return obj;
            }
            i++;
        }
        return null;
    }

    public Set<Integer> getAllCity() {
        return cityMap.keySet();
    }

    public SeaTradeItemTemplateImpl getItemCfg(int itemId) {
        return itemMap.getOrDefault(itemId, null);
    }

    public SeaTradeBuildingTemplate getBuildingConfig(int level) {
        return buildingMap.getOrDefault(level, null);
    }

    /**
     * 获取航运公司的最大等级
     *
     * @return
     */
    public int getMaxBuildingLv() {
        return buildingMap.keySet().stream().mapToInt(i -> i).max().orElse(0);
    }

    public int getMaxBoatLv() {
        return shipMap.keySet().stream().mapToInt(i -> i).max().orElse(0);
    }

    public SeaTradeShipTemplate getShipConfig(int level) {
        return shipMap.getOrDefault(level, null);
    }

    /**
     * 获取两城市间路径长度,只处理直连的
     *
     * @param cityIdA
     * @param cityIdB
     * @return
     */
    private int getRoadLength(int cityIdA, int cityIdB) {
        if (roadConfig.contains(cityIdA, cityIdB)) {
            return roadConfig.get(cityIdA, cityIdB);
        } else if (roadConfig.contains(cityIdB, cityIdA)) {
            return roadConfig.get(cityIdB, cityIdA);
        }
        return Integer.MAX_VALUE;
    }

    public int getCityRoadLength(int ca, int cb) {
        return getPathLength(getPath(ca, cb));
    }

    public List<Integer> getPath(int ca, int cb) {
        return mapGraph.getShortestPath(ca, cb);
    }

    public int getPathLength(List<Integer> path) {
        int length = 0;
        if (path.size() < 2) {
            return length;
        }
        for (int i = 0; i < (path.size() - 1); i++) {
            int roadLength = getRoadLength(path.get(i), path.get(i + 1));
            if (roadLength < Integer.MAX_VALUE) {
                length += roadLength;
            } else {
                return roadLength;
            }
        }
        return length;
    }

    @Override
    public List<String> getDownloadFile() {
        return getConfigName(SeaTradeRoadTemplate.class, SeaTradeCityTemplateImpl.class, SeaTradeItemTemplateImpl.class,
                SeaTradeShipTemplate.class, SeaTradeBuildingTemplate.class, SeaTradeRoadNewTemplate.class
        );
    }

    /**
     * 新版贸易随机一个目的城市
     *
     * @return
     */
    public SeaTradeRoadNewTemplate pickCity() {
        if (roadCityWM != null) {
            return roadMap.getOrDefault(roadCityWM.random(), null);
        }
        return null;
    }
}
