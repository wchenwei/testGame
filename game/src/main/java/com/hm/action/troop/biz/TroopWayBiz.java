package com.hm.action.troop.biz;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.hm.libcore.annotation.Biz;
import com.hm.config.CityConfig;
import com.hm.config.city.CityAreaTemplate;
import com.hm.config.city.CityGraph;
import com.hm.config.excel.templaextra.CityTemplate;
import com.hm.model.player.Player;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Biz
public class TroopWayBiz {
    @Resource
    private CityConfig cityConfig;

    public List<Integer> findWayList(Player player, int startId,int toId) {
        //生成路线,判断是否时跨岛
        CityTemplate startTemplate = cityConfig.getCityById(startId);
        CityTemplate targetTemplate = cityConfig.getCityById(toId);

        if(startId <= 0) {
            //找出目标点最近的港口城池
            List<Integer> inList = findOutPortList(player,targetTemplate);
            if(CollUtil.isEmpty(inList)) {
                return null;//进不来
            }
            Collections.reverse(inList);
            return inList;
        }

        //港口->港口任意通行
        if(startTemplate.isPortCity() && targetTemplate.isPortCity()) {
            return Lists.newArrayList(startId,toId);
        }

        if(startTemplate.isSameArea(targetTemplate)) {
            //同一个区域
            CityAreaTemplate cityAreaTemplate = cityConfig.getCityAreaTemplate(targetTemplate.getArea());
            CityGraph cityGraph = cityAreaTemplate.buildCityGraph(player);
            return cityGraph.getShortestPath(startId,toId);
        }else{
            //找出出去最近的港口城路线
            List<Integer> outList = findOutPortList(player,startTemplate);
            if(CollUtil.isEmpty(outList)) {
                return null;//出不去
            }
            List<Integer> inList = findOutPortList(player,targetTemplate);
            if(CollUtil.isEmpty(inList)) {
                return null;//进不来
            }
            Collections.reverse(inList);
            outList.addAll(inList);
            return outList;
        }
    }

    //找出区域最近的港口城池列表
    public List<Integer> findOutPortList(Player player,CityTemplate cityTemplate) {
        if(cityTemplate.isPortCity()) {
            return Lists.newArrayList(cityTemplate.getId());
        }
        //所有通过
        List<TempWayLine> lineList = Lists.newArrayList();
        CityAreaTemplate cityAreaTemplate = cityConfig.getCityAreaTemplate(cityTemplate.getArea());
        CityGraph cityGraph = cityAreaTemplate.buildCityGraph(player);
        for (CityTemplate portTemplate : cityAreaTemplate.getPortList()) {
            List<Integer> wayList = cityGraph.getShortestPath(cityTemplate.getId(),portTemplate.getId());
            if(CollUtil.isNotEmpty(wayList)) {
                lineList.add(new TempWayLine(cityTemplate.getId(),portTemplate.getId(),wayList));
            }
        }
        if(lineList.isEmpty()) {
            return null;
        }
        return lineList.stream().min(Comparator.comparingInt(TempWayLine::getWaySize)).get().getWayList();
    }

}
