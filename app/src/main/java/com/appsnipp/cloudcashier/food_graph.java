package com.appsnipp.cloudcashier;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class food_graph extends AppCompatActivity implements OnChartValueSelectedListener {

    private DatabaseReference databaseReference;
    private BarChart barChart;

    private FirebaseAuth auth;
    private FirebaseDatabase mdatabase;
    private List<BarEntry> entries;
    private List<String> dateLabels;
    private List<String> titles;
    private List<String> selectedOptions;
    private List<String> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        // Initialize Firebase reference
        auth = FirebaseAuth.getInstance();
        mdatabase=FirebaseDatabase.getInstance();
        FirebaseUser ccurrentUser=auth.getCurrentUser();
        if(ccurrentUser!=null) {
            String userid = ccurrentUser.getUid();
            databaseReference = mdatabase.getReference().child(userid).child("food");
        }

        // Initialize bar chart
        barChart = findViewById(R.id.bar_chart);
        barChart.getDescription().setEnabled(false); // Hide chart description
        barChart.setOnChartValueSelectedListener(this); // Set listener

        // Initialize data lists
        entries = new ArrayList<>();
        dateLabels = new ArrayList<>();
        titles = new ArrayList<>();
        selectedOptions = new ArrayList<>();
        notes = new ArrayList<>();

        // Fetch data from Firebase
        fetchData();
    }

    private void fetchData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int index = 0;
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        // Extract data from each child
                        String date = childSnapshot.child("selectedDate").getValue(String.class);
                        double price = childSnapshot.child("price").getValue(Double.class);
                        String title = childSnapshot.child("title").getValue(String.class); // Add title
                        String selectedOption = childSnapshot.child("selectedOption").getValue(String.class); // Add selected option
                        String note = childSnapshot.child("note").getValue(String.class); // Add note

                        // Create BarEntry object and add to list
                        BarEntry entry = new BarEntry(index, (float) price);
                        entries.add(entry);

                        // Add other data to respective lists
                        dateLabels.add(date);
                        titles.add(title);
                        selectedOptions.add(selectedOption);
                        notes.add(note);

                        index++;
                    }

                    // Create BarDataSet and set data for the chart
                    BarDataSet dataSet = new BarDataSet(entries, "Prices");
                    dataSet.setColor(Color.parseColor("#3F51B5")); // Set bar color

                    BarData barData = new BarData(dataSet);

                    // Set X-axis labels
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dateLabels));

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


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int index = (int) h.getX(); // Get index of selected bar

        // Construct information string with date
        String info = "Date: " + dateLabels.get(index) + "\n" +
                "Title: " + titles.get(index) + "\n" +
                "Selected Option: " + selectedOptions.get(index) + "\n" +
                "Note: " + notes.get(index);

        // Display information in a Toast or custom dialog
        Toast.makeText(this, info, Toast.LENGTH_LONG).show(); // Example using Toast

        // Optionally, prevent screen dimming during long press
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    @Override
    public void onNothingSelected() {
        // Handle no selection case (optional)
    }
}
