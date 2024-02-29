package com.appsnipp.cloudcashier;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class bar extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private BarChart barChart;
    private List<BarEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("your_data_path");

        // Initialize bar chart
        barChart = findViewById(R.id.bar_chart);
        barChart.getDescription().setEnabled(false); // Hide chart description

        // Initialize data list
        entries = new ArrayList<>();

        // Fetch data from Firebase
        fetchData();
    }

    private void fetchData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        // Extract date and price from each child
                        String date = childSnapshot.child("date").getValue(String.class);
                        double price = childSnapshot.child("price").getValue(Double.class);

                        // Create BarEntry object and add to list
                        BarEntry entry = new BarEntry(entries.size(), (float) price);
                        entries.add(entry);
                    }

                    // Create BarDataSet and set data for the chart
                    BarDataSet dataSet = new BarDataSet(entries, "Prices");
                    dataSet.setColor(Color.parseColor("#3F51B5")); // Set bar color

                    BarData barData = new BarData(dataSet);
                    barChart.setData(barData);
                    barChart.invalidate(); // Refresh the chart
                } else {
                    // Handle no data scenario (e.g., display a message)
                    Log.d("BarChartActivity", "No data found in Firebase");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors during data fetching (e.g., display an error message)
                Log.w("BarChartActivity", "Error fetching data from Firebase", error.toException());
            }
        });
    }
}

