package com.example.advanced_bus_route_planner.graph;



/**
 * Represents an edge (connection) between two nodes
 * Contains destination and weight (distance/time)
 */
public class Edge {
    private String destination;
    private double weight;

    public Edge(String destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public String getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "→ " + destination + " (" + weight + "km)";
    }
}