package com.hm.config.city;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hm.action.troop.biz.CCLine;
import com.hm.config.excel.temlate.CityIsLandTemplate;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.libcore.annotation.FileConfig;
import com.hm.model.cityworld.WorldCity;
import com.hm.model.player.Player;
import com.hm.servercontainer.world.WorldItemContainer;
import com.hm.servercontainer.world.WorldServerContainer;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@FileConfig("city_island")
public class CityAreaTemplate extends CityIsLandTemplate {
    private transient Map<Integer, CityTemplate> cityMap = Maps.newConcurrentMap();
    private transient List<CityTemplate> cityList = Lists.newArrayList();
    //所有港口城池
    private transient List<CityTemplate> portList = Lists.newArrayList();

    public void loadCityList(List<CityTemplate> cityList) {
        this.cityList = Lists.newArrayList(cityList);
        this.cityMap = cityList.stream().collect(Collectors.toMap(CityTemplate::getId, e -> e));
        this.portList = cityList.stream().filter(e -> e.isPortCity()).collect(Collectors.toList());
    }

    //构建默认的移动路径
    public CityGraph buildDefaultCityGraph() {
        CityGraph cityGraph = new CityGraph();
        for (CityTemplate cityTemplate : cityList) {
            for (int linkId : cityTemplate.getLinkCityIds()) {
                cityGraph.addNeighborById(cityTemplate.getId(),linkId,100);
            }
        }
        //港口和任意港口连接
        for (CityTemplate cityTemplate : portList) {
            for (CityTemplate cityTemplate2 : portList) {
                if(cityTemplate != cityTemplate2) {
                    cityGraph.addNeighborById(cityTemplate.getId(),cityTemplate2.getId(),1);
                }
            }
        }
        return cityGraph;
    }

    public List<Integer> findWays(Player player,int startId,int endId,boolean checkPortFight) {
        WorldItemContainer worldItemContainer = WorldServerContainer.of(player);
        CityGraph cityGraph = buildDefaultCityGraph();
        for (CityTemplate cityTemplate : cityList) {
            if(!player.playerMission().isUnlockCity(cityTemplate.getId())) {
                cityGraph.removeVertex(cityTemplate.getId());
            }
            WorldCity worldCity = worldItemContainer.getWorldCity(cityTemplate.getId());
            if(checkPortFight || !cityTemplate.isPortCity()) {
                if(CollUtil.isNotEmpty(worldCity.getAtkCityTroop().getTroopList())) {
                    cityGraph.removeVertex(cityTemplate.getId());
                }
            }
        }
        cityGraph.buildGraph();
        List<Integer> wayList = cityGraph.getShortestPath(startId,endId);
        return wayList;
    }

    public CityGraph buildCityGraph(Player player) {
        Map<String, CCLine> lineMap = Maps.newConcurrentMap();
        for (CityTemplate cityTemplate : cityList) {
            for (int linkId : cityTemplate.getLinkCityIds()) {
                CCLine line = new CCLine(cityTemplate.getId(),linkId,100);
                if(!lineMap.containsKey(line.getId()) && line.isUnlock(player)) {
                    lineMap.put(line.getId(),line);
                }
            }
        }
        //港口和任意港口连接
        for (int i = 0; i < portList.size()-1; i++) {
            CityTemplate cityTemplate = portList.get(i);
            for (int j = i+1; j < portList.size(); j++) {
                CityTemplate toTemplate = portList.get(j);
                CCLine line = new CCLine(cityTemplate.getId(),toTemplate.getId(),1);
                if(!lineMap.containsKey(line.getId()) && line.isUnlock(player)) {
                    lineMap.put(line.getId(),line);
                }
            }
        }
        CityGraph cityGraph = new CityGraph();
        for (CCLine value : lineMap.values()) {
            cityGraph.addNeighborById(value.getStartId(),value.getToId(),value.getWeight());
        }

        WorldItemContainer worldItemContainer = WorldServerContainer.of(player);
        Map<Integer,Boolean> haveWar = Maps.newHashMap();
        for (CityTemplate cityTemplate : cityList) {
            WorldCity city = worldItemContainer.getWorldCity(cityTemplate.getId());
            haveWar.put(city.getId(),city.haveAtkCityTroop());
        }
        for (CityTemplate cityTemplate : cityList) {
            if(!haveWar.containsKey(cityTemplate.getId())) {
                continue;
            }
            if(cityTemplate.isPortCity()) {
                //找出所有包含的港口的
                lineMap.values().stream().filter(e -> e.containCity(cityTemplate.getId()))
                        .forEach(e -> e.changeMaxWeight());
            }
        }
        for (CityTemplate cityTemplate : cityList) {
            if(!haveWar.containsKey(cityTemplate.getId())) {
                continue;
            }
            if(!cityTemplate.isPortCity()) {
                cityGraph.removeVertex(cityTemplate.getId());
            }
        }
        cityGraph.buildGraph();
        return cityGraph;
    }

}
