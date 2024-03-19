package com.appsnipp.cloudcashier;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class utilities_graph extends AppCompatActivity implements OnChartValueSelectedListener {

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
        setContentView(R.layout.activity_utilities_graph);

        // Initialize Firebase reference
        auth = FirebaseAuth.getInstance();
        mdatabase=FirebaseDatabase.getInstance();
        FirebaseUser ccurrentUser=auth.getCurrentUser();
        if(ccurrentUser!=null) {
            String userid = ccurrentUser.getUid();
            databaseReference = mdatabase.getReference().child(userid).child("utilities");
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

        // Fetch data for the default mode (monthly)
        fetchDataMonthly();

        // Set click listeners for buttons
        Button dailyButton = findViewById(R.id.dailyexpense);
        Button monthlyButton = findViewById(R.id.monthly);
        Button yearlyButton = findViewById(R.id.yearly);

        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataDaily();
            }
        });

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataMonthly();
            }
        });

        yearlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataYearly();
            }
        });
    }

    private void fetchDataDaily() {
        // Clear previous data
        clearData();

        // Get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());

        // Fetch data for the current date
        fetchDataByDate(currentDate);
    }

    private void fetchDataMonthly() {
        // Clear previous data
        clearData();

        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0

        // Fetch data for the current month and year
        fetchDataByMonth(year, month);
    }

    private void fetchDataYearly() {
        // Clear previous data
        clearData();

        // Get current year
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        // Fetch data for the current year
        fetchDataByYear();
    }

    private void fetchDataByDate(String date) {
        // Query Firebase to fetch data for the given date
        databaseReference.orderByChild("selectedDate").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                processSnapshot(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors during data fetching
                Log.e("utility", "Error fetching data by date", error.toException());
            }
        });
    }

    private void fetchDataByMonth(int year, int month) {
        // Get the number of days in the selected month
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int numDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Fetch data for the entire month
        String startDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, 1);
        String endDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, numDaysInMonth);

        // Query Firebase to fetch data for the given month
        databaseReference.orderByChild("selectedDate").startAt(startDate).endAt(endDate).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                processSnapshot(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors during data fetching
                Log.e("utility", "Error fetching data by month", error.toException());
            }
        });
    }

    private void fetchDataByYear() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Initialize a map to store total prices for each year
                    Map<Integer, Float> yearlyTotals = new HashMap<>();

                    // Iterate through the snapshot to calculate yearly totals
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String date = childSnapshot.child("selectedDate").getValue(String.class);
                        double price = childSnapshot.child("price").getValue(Double.class);

                        // Parse year from the date
                        int year = Integer.parseInt(date.substring(0, 4));

                        // Update total price for the year
                        if (yearlyTotals.containsKey(year)) {
                            float total = yearlyTotals.get(year);
                            total += price;
                            yearlyTotals.put(year, total);
                        } else {
                            yearlyTotals.put(year, (float) price);
                        }
                    }

                    // Prepare data entries for the chart
                    List<BarEntry> entries = new ArrayList<>();
                    List<String> yearLabels = new ArrayList<>();

                    int index = 0;
                    for (Map.Entry<Integer, Float> entry : yearlyTotals.entrySet()) {
                        int year = entry.getKey();
                        float totalPrice = entry.getValue();

                        entries.add(new BarEntry(index, totalPrice));
                        yearLabels.add(String.valueOf(year));

                        index++;
                    }

                    // Create a BarDataSet and BarData
                    BarDataSet dataSet = new BarDataSet(entries, "Yearly Prices");
                    dataSet.setColor(Color.parseColor("#3F51B5")); // Set bar color

                    BarData barData = new BarData(dataSet);
                    barData.setBarWidth(0.5f); // Set custom bar width

                    // Set X-axis labels as year values
                    barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(yearLabels));

                    // Set the data to the chart
                    barChart.setData(barData);
                    barChart.invalidate(); // Refresh the chart

                    // Set listener for the yearly graph
                    barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                        @Override
                        public void onValueSelected(Entry e, Highlight h) {
                            int index = (int) h.getX(); // Get index of selected bar
                            onYearlyValueSelected(index, entries, yearLabels);
                        }

                        @Override
                        public void onNothingSelected() {
                            // Handle no selection case (optional)
                        }
                    });
                } else {
                    // Handle no data scenario
                    Log.d("utility", "No data found in Firebase");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors during data fetching
                Log.e("utility", "Error fetching data by year", error.toException());
            }
        });
    }



    private void processSnapshot(DataSnapshot snapshot) {
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
            Log.d("utility", "No data found in Firebase");
        }
    }

    private void clearData() {
        // Clear previous data lists
        entries.clear();
        dateLabels.clear();
        titles.clear();
        selectedOptions.clear();
        notes.clear();
    }

    private void onYearlyValueSelected(int index, List<BarEntry> entries, List<String> yearLabels) {
        // Check if the index is within bounds and lists are not empty
        if (index >= 0 && index < entries.size() && !yearLabels.isEmpty()) {
            // Access elements from lists
            int year = Integer.parseInt(yearLabels.get(index));
            float totalPrice = entries.get(index).getY();

            // Construct information string with year and total price
            String info = "Year: " + year + "\n" +
                    "Total Price: Rs" + totalPrice;

            // Display information in a Toast
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        } else {
            // Log an error if lists are empty or index is out of bounds
            Log.e("utility", "Index out of bounds or empty lists");
        }

        // Optionally, prevent screen dimming during long press
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        int index = (int) h.getX(); // Get index of selected bar

        // Check if the index is within bounds and lists are not empty
        if (index >= 0 && index < entries.size() && !dateLabels.isEmpty() && !titles.isEmpty() && !selectedOptions.isEmpty() && !notes.isEmpty()) {
            // Access elements from lists
            String info = "Date: " + dateLabels.get(index) + "\n" +
                    "Title: " + titles.get(index) + "\n" +
                    "Selected Option: " + selectedOptions.get(index) + "\n" +
                    "Note: " + notes.get(index) + "\n" +
                    "Total Price: Rs" + entries.get(index).getY();

            // Display information in a Toast
            Toast.makeText(this, info, Toast.LENGTH_LONG).show();
        } else {
            // Log an error if lists are empty or index is out of bounds
            Log.e("utility", "Index out of bounds or empty lists");
        }

        // Optionally, prevent screen dimming during long press
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }



    @Override
    public void onNothingSelected() {
        // Handle no selection case (optional)
    }
}
