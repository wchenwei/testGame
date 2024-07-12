package com.hm.util.dijkstras;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yang xb
 * Date: 2018-11-12
 */
public class Graph {
    // city id : neighbor city id list
    private final Map<Integer, List<Vertex>> cityMap;

    public Graph() {
        this.cityMap = new HashMap<>();
    }

    public void addNeighborById(Integer cityId, List<Integer> neighborCityIdList) {
        List<Vertex> neighborCityList = neighborCityIdList.stream().map(Vertex::new).collect(Collectors.toList());
        this.cityMap.put(cityId, neighborCityList);
    }

    public void addNeighbor(Integer cityId, List<Vertex> neighborCityList) {
        this.cityMap.put(cityId, neighborCityList);
    }

    public List<Integer> getShortestPath(int start, int finish) {
        final Map<Integer, Integer> distances = new HashMap<>();
        final Map<Integer, Vertex> previous = new HashMap<>();
        PriorityQueue<Vertex> nodes = new PriorityQueue<>();

        for (Integer vertex : cityMap.keySet()) {
            if (vertex == start) {
                distances.put(vertex, 0);
                nodes.add(new Vertex(vertex, 0));
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
                nodes.add(new Vertex(vertex, Integer.MAX_VALUE));
            }
            previous.put(vertex, null);
        }

        while (!nodes.isEmpty()) {
            Vertex smallest = nodes.poll();
            if (smallest.getCityId() == finish) {
                final List<Integer> path = new ArrayList<>();
                while (previous.get(smallest.getCityId()) != null) {
                    path.add(smallest.getCityId());
                    smallest = previous.get(smallest.getCityId());
                }
                if (smallest.getCityId().equals(start)) {
                    path.add(smallest.getCityId());
                }
                return path;
            }

            if (distances.get(smallest.getCityId()) == Integer.MAX_VALUE) {
                break;
            }

            for (Vertex neighbor : cityMap.get(smallest.getCityId())) {
                int alt = distances.get(smallest.getCityId()) + neighbor.getDistance();
                if (alt < distances.getOrDefault(neighbor.getCityId(), Integer.MAX_VALUE)) {
                    distances.put(neighbor.getCityId(), alt);
                    previous.put(neighbor.getCityId(), smallest);

                    forloop:
                    for (Vertex n : nodes) {
                        if (n.getCityId().equals(neighbor.getCityId())) {
                            nodes.remove(n);
                            n.setDistance(alt);
                            nodes.add(n);
                            break forloop;
                        }
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addNeighborById(1, Arrays.asList(2, 3));
        g.addNeighborById(2, Arrays.asList(1, 6));
        g.addNeighborById(3, Arrays.asList(1, 6, 7));
        g.addNeighborById(4, Arrays.asList(6));
        g.addNeighborById(5, Arrays.asList(8));
        g.addNeighborById(6, Arrays.asList(2, 3, 4, 7, 8));
        g.addNeighborById(7, Arrays.asList(3, 6));
        g.addNeighborById(8, Arrays.asList(5, 6));
        System.out.println(g.getShortestPath(4, 1));

        // Map<Integer, CityTemplate> cityMap = cityConfig.getCityMap();
        // Graph graph = new Graph();
        // for (Map.Entry<Integer, CityTemplate> entry : cityMap.entrySet()) {
        //     graph.addNeighbor(entry.getKey(), entry.getValue().getLinkCityIds());
        // }
        //
        // List<Integer> shortestPath = graph.getShortestPath(1, 50);
        // if (!shortestPath.isEmpty()) {
        //     System.out.println(shortestPath);
        // }
    }
}

