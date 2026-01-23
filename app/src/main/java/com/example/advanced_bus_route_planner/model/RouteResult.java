package com.example.advanced_bus_route_planner.model;


import java.util.List;

public class RouteResult {
    private List<String> path;
    private double totalCost;
    private boolean found;

    public RouteResult(List<String> path, double totalCost, boolean found) {
        this.path = path;
        this.totalCost = totalCost;
        this.found = found;
    }

    public List<String> getPath() {
        return path;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public boolean isFound() {
        return found;
    }

    public String getFormattedRoute() {
        if (!found) {
            return "No route found!";
        }

        StringBuilder result = new StringBuilder();
        result.append("Route Found!\n\n");
        result.append("Path: ");

        for (int i = 0; i < path.size(); i++) {
            result.append(path.get(i));
            if (i < path.size() - 1) {
                result.append(" → ");
            }
        }

        result.append("\n\nTotal Distance: ");
        result.append(String.format("%.1f km", totalCost));
        result.append("\nStops: ").append(path.size());

        return result.toString();
    }
}