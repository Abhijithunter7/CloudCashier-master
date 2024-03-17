package com.appsnipp.cloudcashier;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class home_maintenance extends AppCompatActivity {

    private EditText editTitle;
    private Spinner optionsSpinner;
    private EditText editPrice;
    private EditText editNote;
    private Button saveButton,pastexpense;
    private Button showDatePickerButton,graph;
    private DatePicker homemaintenancedatePicker;
    private String noteId;

    private FirebaseAuth auth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference transportRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maintenance);

        // Initialize Firebase Database reference
        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        FirebaseUser ccurrentUser = auth.getCurrentUser();
        if (ccurrentUser != null) {
            String userid = ccurrentUser.getUid();
            transportRef = mdatabase.getReference().child(userid).child("home_maintenance");
        }

        // Initialize UI elements
        editTitle = findViewById(R.id.homemaintenancetitleEditText);
        optionsSpinner = findViewById(R.id.homemaintenanceoptionsSpinner);
        editPrice = findViewById(R.id.homemaintenancepriceEditText);
        editNote = findViewById(R.id.homemaintenancenoteEditText);
        saveButton = findViewById(R.id.homemaintenancesaveButton);
        showDatePickerButton = findViewById(R.id.showDatePickerButton);
        homemaintenancedatePicker = findViewById(R.id.homemaintenancedatePicker);
        graph = findViewById(R.id.graphhomemaintenance);
        pastexpense = findViewById(R.id.homepast);

        // Populate the Spinner with homemaintenance options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.home_maintenance_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);

        // Retrieve noteId from intent
        Intent intent = getIntent();
        if (intent != null) {
            noteId = intent.getStringExtra("noteId");
            if (noteId != null) {
                // Fetch data for the given noteId from Firebase and populate the form fields
                fetchDataFromFirebase(noteId);
            }
        }

        pastexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_maintenance.this, homemainrecy.class));
            }
        });

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(home_maintenance.this, homemain_graph.class));
            }
        });

        // Set an OnClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values entered by the user
                String title = editTitle.getText().toString();
                String option = optionsSpinner.getSelectedItem().toString();
                String priceStr = editPrice.getText().toString();
                String note = editNote.getText().toString();
                String selectedDate = getSelectedDate();

                // Check if any field is empty
                if (title.isEmpty() || priceStr.isEmpty() || note.isEmpty() || selectedDate.isEmpty()) {
                    Toast.makeText(home_maintenance.this, "All fields are compulsory", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price;
                if (!priceStr.trim().isEmpty()) { // Check if priceStr is not empty or contains only whitespace
                    try {
                        price = Double.parseDouble(priceStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(home_maintenance.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(home_maintenance.this, "Price cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the data to Firebase
                if (noteId != null) {
                    // Update the existing data in Firebase
                    updateDataInFirebase(title, option, price, note, selectedDate);
                } else {
                    // Generate a unique ID for the entry
                    String entryId = transportRef.push().getKey();

                    FirebaseUser user = auth.getCurrentUser();
                    String userName = user != null ? user.getDisplayName() : "Unknown User";

                    // Create a new object to represent the data
                    TransportData foodda = new TransportData(entryId, title, option, price, note, selectedDate);

                    // Save the data to Firebase
                    transportRef.child(entryId).setValue(foodda);

                    // Clear the form fields
                    clearForm();

                    // Show a message or perform other actions if needed
                    String message = "Data saved to Firebase!";
                    Toast.makeText(home_maintenance.this, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set an OnClickListener for the Show Date Picker button
        showDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DatePicker
                homemaintenancedatePicker.setVisibility(View.VISIBLE);
                showDatePicker();
            }
        });
    }

    private void fetchDataFromFirebase(String noteId) {
        DatabaseReference noteRef = transportRef.child(noteId);
        noteRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TransportData transportData = snapshot.getValue(TransportData.class);
                    if (transportData != null) {
                        // Populate the form fields with existing data
                        editTitle.setText(transportData.getTitle());
                        // Set the selected item in the Spinner
                        ArrayAdapter adapter = (ArrayAdapter) optionsSpinner.getAdapter();
                        int position = adapter.getPosition(transportData.getselectedOption());
                        optionsSpinner.setSelection(position);
                        editPrice.setText(String.valueOf(transportData.getPrice()));
                        editNote.setText(transportData.getNote());
                        // Parse and set the date in the DatePicker
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        try {
                            Date date = sdf.parse(transportData.getselectedDate());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            homemaintenancedatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }

    private void clearForm() {
        editTitle.setText("");
        optionsSpinner.setSelection(0); // Reset spinner selection to the first item
        editPrice.setText("");
        editNote.setText("");
        // Clear the DatePicker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        homemaintenancedatePicker.init(year, month, day, null);
    }

    private void updateDataInFirebase(String title, String option, double price, String note, String selectedDate) {

        // Create a new object to represent the updated data
        TransportData transportData = new TransportData(noteId, title, option, price, note, selectedDate);

        // Update the data in Firebase
        transportRef.child(noteId).setValue(transportData);

        // Show a message or perform other actions if needed
        String message = "Data updated in Firebase!";
        Toast.makeText(home_maintenance.this, message, Toast.LENGTH_SHORT).show();
    }


    private void showDatePicker() {
        // Get the current date
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and show it
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // Set the selected date in the DatePicker
                        homemaintenancedatePicker.init(year, monthOfYear, dayOfMonth, null);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private String getSelectedDate() {
        // Get the selected date from the DatePicker
        int day = homemaintenancedatePicker.getDayOfMonth();
        int month = homemaintenancedatePicker.getMonth();
        int year = homemaintenancedatePicker.getYear();

        // Create a formatted date string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return sdf.format(calendar.getTime());
    }
}
