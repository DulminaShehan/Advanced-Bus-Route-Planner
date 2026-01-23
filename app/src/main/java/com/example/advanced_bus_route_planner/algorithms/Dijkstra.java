package com.example.advanced_bus_route_planner.algorithms;
import com.example.advanced_bus_route_planner.graph.Edge;
import com.example.advanced_bus_route_planner.graph.Graph;
import com.example.advanced_bus_route_planner.graph.Node;
import com.example.advanced_bus_route_planner.model.RouteResult;

import java.util.*;

public class Dijkstra {

    public static RouteResult findShortestPath(Graph graph, String sourceId, String destId) {

        if (!graph.hasNode(sourceId) || !graph.hasNode(destId)) {
            return new RouteResult(new ArrayList<>(), 0, false);
        }

        if (sourceId.equals(destId)) {
            List<String> path = new ArrayList<>();
            path.add(sourceId);
            return new RouteResult(path, 0, true);
        }

        Map<String, Double> distances = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(
                (a, b) -> Double.compare(a.distance, b.distance)
        );
        Set<String> visited = new HashSet<>();

        for (String nodeId : graph.getAllNodes().keySet()) {
            distances.put(nodeId, Double.POSITIVE_INFINITY);
        }

        distances.put(sourceId, 0.0);
        pq.offer(new NodeDistance(sourceId, 0.0));
        parent.put(sourceId, null);

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            String currentId = current.nodeId;

            if (visited.contains(currentId)) {
                continue;
            }

            visited.add(currentId);

            if (currentId.equals(destId)) {
                break;
            }

            Node currentNode = graph.getNode(currentId);
            if (currentNode == null) continue;

            for (Edge edge : currentNode.getEdges()) {
                String neighborId = edge.getDestination();

                if (visited.contains(neighborId)) {
                    continue;
                }

                double newDistance = distances.get(currentId) + edge.getWeight();

                if (newDistance < distances.get(neighborId)) {
                    distances.put(neighborId, newDistance);
                    parent.put(neighborId, currentId);
                    pq.offer(new NodeDistance(neighborId, newDistance));
                }
            }
        }

        List<String> path = reconstructPath(parent, sourceId, destId);

        if (path.isEmpty()) {
            return new RouteResult(new ArrayList<>(), 0, false);
        }

        double totalCost = distances.get(destId);

        return new RouteResult(path, totalCost, true);
    }

    private static List<String> reconstructPath(Map<String, String> parent,
                                                String source, String dest) {
        List<String> path = new ArrayList<>();

        if (!parent.containsKey(dest)) {
            return path;
        }

        String current = dest;
        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);

        if (!path.isEmpty() && !path.get(0).equals(source)) {
            return new ArrayList<>();
        }

        return path;
    }

    private static class NodeDistance {
        String nodeId;
        double distance;

        NodeDistance(String nodeId, double distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }
    }
}