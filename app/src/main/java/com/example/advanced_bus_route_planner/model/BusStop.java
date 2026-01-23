package com.example.advanced_bus_route_planner.model;

/**
 * Represents a bus stop in our graph
 * Each bus stop has a unique name/ID
 */
public class BusStop {
    private String id;          // Bus stop identifier (e.g., "A", "B")
    private String name;        // Human-readable name (e.g., "Central Station")

    // Constructor - creates a new bus stop
    public BusStop(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter methods - retrieve values
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setter methods - update values
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Override toString for easy printing
    @Override
    public String toString() {
        return id + " (" + name + ")";
    }

    // Override equals for comparing bus stops
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BusStop busStop = (BusStop) obj;
        return id.equals(busStop.id);
    }

    // Override hashCode for using in HashMaps
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}