package com.iot23.indoorsensing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassroomGraph {
    public Map<String, List<Edge>> adjacencyList;

    public ClassroomGraph() {
        adjacencyList = new HashMap<>();
    }

    public void addVertex(String vertex) {
        adjacencyList.put(vertex, new ArrayList<>());
    }

    public void addEdge(String source, String destination, int weight) {
        adjacencyList.get(source).add(new Edge(destination, weight));
        adjacencyList.get(destination).add(new Edge(source, weight));
    }

    public List<Edge> getNeighbors(String vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    public static class Edge {
        private String destination;
        private int weight;

        public Edge(String destination, int weight) {
            this.destination = destination;
            this.weight = weight;
        }
    }

    //

    public Map<String, Integer> dijkstra(String source) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previousVertices = new HashMap<>();

        for (String vertex : adjacencyList.keySet()) {
            // Log.i("datas", "vertex : " + vertex );
            if (vertex.equals(source)) {
                distances.put(vertex, 0);
            } else {
                distances.put(vertex, Integer.MAX_VALUE);
            }
        }

        Set<String> visited = new HashSet<>();

        while (visited.size() < adjacencyList.size()) {
            String currentVertex = null;
            int minDistance = Integer.MAX_VALUE;

            for (String vertex : adjacencyList.keySet()) {
                if (!visited.contains(vertex) && distances.get(vertex) < minDistance) {
                    currentVertex = vertex;
                    minDistance = distances.get(vertex);
                }
            }

            if (currentVertex == null) {
                break;
            }

            visited.add(currentVertex);

            List<Edge> neighbors = getNeighbors(currentVertex);

            for (Edge edge : neighbors) {
                int newDistance = distances.get(currentVertex) + edge.weight;
                if (newDistance < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDistance);
                    previousVertices.put(edge.destination, currentVertex);
                }
            }
        }

        return distances;
    }

    public List<String> getShortestPath(String source, String destination) {
        Map<String, Integer> distances = dijkstra(source);
        Map<String, String> previousVertices = new HashMap<>();
        List<String> shortestPath = new ArrayList<>();

        String currentVertex = destination;
        while (currentVertex != null && !currentVertex.equals(source)) {
            shortestPath.add(currentVertex);

            // Find the neighbor vertex with the smallest distance
            int minDistance = Integer.MAX_VALUE;
            String nextVertex = null;
            List<Edge> neighbors = getNeighbors(currentVertex);
            for (Edge edge : neighbors) {
                int neighborDistance = distances.get(edge.destination);
                if (neighborDistance < minDistance) {
                    minDistance = neighborDistance;
                    nextVertex = edge.destination;
                }
            }

            currentVertex = nextVertex;
        }

        shortestPath.add(source);
        Collections.reverse(shortestPath);

        return shortestPath;
    }

}
