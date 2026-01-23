package com.example.advanced_bus_route_planner.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// ✅ CORRECTED: Use your actual package name
import com.example.advanced_bus_route_planner.R;
import com.example.advanced_bus_route_planner.algorithms.Dijkstra;
import com.example.advanced_bus_route_planner.graph.Graph;
import com.example.advanced_bus_route_planner.model.RouteResult;

/**
 * Main Activity - The first screen users see
 * Handles user input and displays route results
 */
public class MainActivity extends AppCompatActivity {

    // UI Components (connect to XML layout)
    private EditText editTextSource;
    private EditText editTextDestination;
    private Button buttonFindRoute;
    private TextView textViewResult;

    // Graph object - stores our bus network
    private Graph busGraph;

    /**
     * Called when the app starts
     * Sets up UI and initializes the graph
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Load the layout

        // Connect Java variables to XML elements
        editTextSource = findViewById(R.id.editTextSource);
        editTextDestination = findViewById(R.id.editTextDestination);
        buttonFindRoute = findViewById(R.id.buttonFindRoute);
        textViewResult = findViewById(R.id.textViewResult);

        // Initialize the bus network graph
        initializeGraph();

        // Set button click listener
        buttonFindRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findRoute();  // Call our route finding method
            }
        });
    }

    /**
     * Creates a sample bus network with 6 stops
     * Graph structure:
     *
     *     A ---3--- B
     *     |         |
     *     2         4
     *     |         |
     *     C ---1--- D ---2--- E
     *               |
     *               5
     *               |
     *               F
     */
    private void initializeGraph() {
        busGraph = new Graph();

        // Add nodes (bus stops)
        busGraph.addNode("A");
        busGraph.addNode("B");
        busGraph.addNode("C");
        busGraph.addNode("D");
        busGraph.addNode("E");
        busGraph.addNode("F");

        // Add bidirectional edges (roads between stops)
        // Format: (stop1, stop2, distance in km)
        busGraph.addBidirectionalEdge("A", "B", 3.0);
        busGraph.addBidirectionalEdge("A", "C", 2.0);
        busGraph.addBidirectionalEdge("B", "D", 4.0);
        busGraph.addBidirectionalEdge("C", "D", 1.0);
        busGraph.addBidirectionalEdge("D", "E", 2.0);
        busGraph.addBidirectionalEdge("D", "F", 5.0);

        // Print graph to console (for debugging)
        busGraph.printGraph();

        // Show success message
        Toast.makeText(this, "Graph initialized with 6 bus stops",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Main method that finds and displays the route
     * Called when user clicks "Find Shortest Route" button
     */
    private void findRoute() {
        // Get user input
        String source = editTextSource.getText().toString().trim().toUpperCase();
        String destination = editTextDestination.getText().toString().trim().toUpperCase();

        // Validate input
        if (source.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Please enter both source and destination",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if nodes exist in graph
        if (!busGraph.hasNode(source)) {
            Toast.makeText(this, "Source bus stop '" + source + "' not found!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!busGraph.hasNode(destination)) {
            Toast.makeText(this, "Destination bus stop '" + destination + "' not found!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Run Dijkstra's algorithm
        RouteResult result = Dijkstra.findShortestPath(busGraph, source, destination);

        // Display result
        if (result != null && result.isFound()) {
            textViewResult.setText(result.getFormattedRoute());
            Toast.makeText(this, "Route calculated successfully!",
                    Toast.LENGTH_SHORT).show();
        } else {
            textViewResult.setText("No route found between " + source + " and " + destination);
            Toast.makeText(this, "No path exists!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}