package com.hm.config.city;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 按照org.jgrapht 生成地图
 *
 * @author 司云龙
 * @Version 1.0.0
 * @date 2021/12/8 9:36
 */
@Data
public class CityGraph {
    //权重带方向的图
    private Graph<Integer, DefaultWeightedEdge> confGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private DijkstraShortestPath<Integer, DefaultWeightedEdge> dijkstraShortestPath;


    //删除顶角
    public void removeVertex(int id) {
        this.confGraph.removeVertex(id);
    }

    //添加连接点
    private void addNeighborByIdOnly(int cityId, int nearId, double weight) {
        if (!this.confGraph.containsVertex(cityId)) {
            this.confGraph.addVertex(cityId);
        }
        if (!this.confGraph.containsVertex(nearId)) {
            this.confGraph.addVertex(nearId);
        }
//        System.out.println(cityId+"->"+nearId);
        DefaultWeightedEdge edge = this.confGraph.addEdge(cityId, nearId);
        this.confGraph.setEdgeWeight(edge, weight);
    }

    //构建算法路径图
    public CityGraph buildGraph() {
        this.dijkstraShortestPath = new DijkstraShortestPath<>(this.confGraph);
        return this;
    }

    public List<Integer> getShortestPath(int start, int finish) {
        try {
            GraphPath graphPath = this.dijkstraShortestPath.getPath(start, finish);
            if (graphPath == null) {
                return Lists.newArrayList();
            }
            return graphPath.getVertexList();
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }


    //获取和城市相连的点
    public List<Integer> getNearIds(int id) {
        return Graphs.neighborListOf(confGraph, id);
    }

    //获取距离城市N的所有点
    public List<Integer> getRangeNearIdList(int id, int range) {
        List<Integer> filterIds = Lists.newArrayList(id);
        Set<Integer> ids = Sets.newHashSet(id);
        for (int i = 0; i < range; i++) {
            List<Integer> finalFilterIds = filterIds;
            ids = ids.stream().flatMap(t -> getNearIds(t).stream()).filter(t -> !finalFilterIds.contains(t)).collect(Collectors.toSet());
            filterIds.addAll(ids);
        }
        return Lists.newArrayList(filterIds);
    }

    public void addNeighborById(int cityId, int nearId, double weight) {
        addNeighborByIdOnly(cityId,nearId,weight);
        addNeighborByIdOnly(nearId,cityId,weight);
    }

    public static void main(String[] args) {
        CityGraph g = new CityGraph();
        g.addNeighborById(1, 2, 100);
        g.addNeighborById(1, 3, 100);

        g.addNeighborById(1, 4, 100);
        g.addNeighborById(1, 5, 100);

        g.addNeighborById(6, 2, 100);
        g.addNeighborById(6, 3, 100);

        g.addNeighborById(7, 4, 100);
        g.addNeighborById(7, 5, 100);

        g.addNeighborById(5, 6, 1);

        g.buildGraph();

        System.out.println(g.getShortestPath(6, 7));
    }
}

