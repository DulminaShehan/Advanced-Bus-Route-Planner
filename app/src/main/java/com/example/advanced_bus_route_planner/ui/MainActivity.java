package com.example.advanced_bus_route_planner.ui;




import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.advanced_bus_route_planner.R;
import com.example.advanced_bus_route_planner.algorithms.Dijkstra;
import com.example.advanced_bus_route_planner.graph.Graph;
import com.example.advanced_bus_route_planner.model.RouteResult;
import com.example.advanced_bus_route_planner.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText editTextSource;
    private EditText editTextDestination;
    private Button buttonFindRoute;
    private TextView textViewResult;
    private TextView textViewWelcome;

    private Graph busGraph;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if user is logged in
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            // Not logged in, redirect to login
            goToLoginActivity();
            return;
        }

        // Initialize UI components
        editTextSource = findViewById(R.id.editTextSource);
        editTextDestination = findViewById(R.id.editTextDestination);
        buttonFindRoute = findViewById(R.id.buttonFindRoute);
        textViewResult = findViewById(R.id.textViewResult);
        textViewWelcome = findViewById(R.id.textViewWelcome);

        // Load user data from Firestore
        loadUserData(firebaseUser.getUid());

        // Initialize the bus network graph
        initializeGraph();

        // Set button click listener
        buttonFindRoute.setOnClickListener(v -> findRoute());
    }

    private void loadUserData(String userId) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentUser = documentSnapshot.toObject(User.class);
                        if (currentUser != null) {
                            // Display welcome message
                            String welcomeText = "Welcome, " + currentUser.getName() +
                                    " (" + currentUser.getRole().toUpperCase() + ")";
                            textViewWelcome.setText(welcomeText);

                            // Show admin features if admin
                            if (currentUser.isAdmin()) {
                                Toast.makeText(this,
                                        "Admin mode: You can manage bus stops",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,
                            "Failed to load user data: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void initializeGraph() {
        busGraph = new Graph();

        busGraph.addNode("A");
        busGraph.addNode("B");
        busGraph.addNode("C");
        busGraph.addNode("D");
        busGraph.addNode("E");
        busGraph.addNode("F");

        busGraph.addBidirectionalEdge("A", "B", 3.0);
        busGraph.addBidirectionalEdge("A", "C", 2.0);
        busGraph.addBidirectionalEdge("B", "D", 4.0);
        busGraph.addBidirectionalEdge("C", "D", 1.0);
        busGraph.addBidirectionalEdge("D", "E", 2.0);
        busGraph.addBidirectionalEdge("D", "F", 5.0);

        busGraph.printGraph();

        Toast.makeText(this, "Graph initialized with 6 bus stops",
                Toast.LENGTH_SHORT).show();
    }

    private void findRoute() {
        String source = editTextSource.getText().toString().trim().toUpperCase();
        String destination = editTextDestination.getText().toString().trim().toUpperCase();

        if (source.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Please enter both source and destination",
                    Toast.LENGTH_SHORT).show();
            return;
        }

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

        RouteResult result = Dijkstra.findShortestPath(busGraph, source, destination);

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

    // Create options menu (logout button)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        goToLoginActivity();
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}