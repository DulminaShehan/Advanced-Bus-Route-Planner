package com.example.advanced_bus_route_planner.graph;




import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node (vertex) in the graph
 * Each node is a bus stop with connections to other stops
 */

import java.util.List;

public class Node {
    private String id;
    private List<Edge> edges;

    public Node(String id) {
        this.id = id;
        this.edges = new ArrayList<>();
    }

    public void addEdge(String destinationId, double weight) {
        edges.add(new Edge(destinationId, weight));
    }

    public String getId() {
        return id;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Node{" + id + ", edges=" + edges.size() + "}";
    }
}
