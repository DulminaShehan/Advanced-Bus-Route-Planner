package com.example.advanced_bus_route_planner.graph;

import java.util.HashMap;
import java.util.Map;

public class Graph {
    private Map<String, Node> nodes;

    public Graph() {
        this.nodes = new HashMap<>();
    }

    public void addNode(String id) {
        if (!nodes.containsKey(id)) {
            nodes.put(id, new Node(id));
        }
    }

    public void addEdge(String source, String destination, double weight) {
        addNode(source);
        addNode(destination);
        nodes.get(source).addEdge(destination, weight);
    }

    public void addBidirectionalEdge(String node1, String node2, double weight) {
        addEdge(node1, node2, weight);
        addEdge(node2, node1, weight);
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public boolean hasNode(String id) {
        return nodes.containsKey(id);
    }

    public Map<String, Node> getAllNodes() {
        return nodes;
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public void printGraph() {
        System.out.println("=== Graph Structure ===");
        for (Map.Entry<String, Node> entry : nodes.entrySet()) {
            String nodeId = entry.getKey();
            Node node = entry.getValue();
            System.out.print(nodeId + ": ");
            for (Edge edge : node.getEdges()) {
                System.out.print(edge + " ");
            }
            System.out.println();
        }
    }
}