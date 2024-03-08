package com.appsnipp.cloudcashier;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class transport_form extends AppCompatActivity {

    private EditText editTitle;
    private Spinner optionsSpinner;
    private EditText editPrice;
    private EditText editNote;
    private Button saveButton;
    private Button showDatePickerButton,graph,pastexpense;
    private DatePicker fooddatePicker;

    private FirebaseAuth auth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference transportRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_form);

        // Initialize Firebase Database reference
        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        FirebaseUser ccurrentUser = auth.getCurrentUser();
        if (ccurrentUser != null) {
            String userid = ccurrentUser.getUid();
            transportRef = mdatabase.getReference().child(userid).child("transport");
        }

        // Initialize UI elements
        editTitle = findViewById(R.id.transporttitleEditText);
        optionsSpinner = findViewById(R.id.transportoptionsSpinner);
        editPrice = findViewById(R.id.transportpriceEditText);
        editNote = findViewById(R.id.transportnoteEditText);
        saveButton = findViewById(R.id.transportsaveButton);
        showDatePickerButton = findViewById(R.id.showDatePickerButton);
        fooddatePicker = findViewById(R.id.transportdatePicker);
        graph = findViewById(R.id.graphtransport);
        pastexpense = findViewById(R.id.transportpasterxpence);

        // Populate the Spinner with food options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.options_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(transport_form.this, transport_graph.class));
            }
        });

        pastexpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(transport_form.this,transportrecy.class));

            }
        });

        // Set an OnClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values entered by the user
                String title = editTitle.getText().toString();
                String option = optionsSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(editPrice.getText().toString());
                String note = editNote.getText().toString();
                String selectedDate = getSelectedDate();

                FirebaseUser user = auth.getCurrentUser();
                String userName = user != null ? user.getDisplayName() : "Unknown User";

                // Create a new object to represent the data
                TransportData foodda = new TransportData(title, option, price, note, selectedDate);

                // Save the data to Firebase
                transportRef.push().setValue(foodda);

                // Show a message or perform other actions if needed
                String message = "Data saved to Firebase!";
                Toast.makeText(transport_form.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Set an OnClickListener for the Show Date Picker button
        showDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the DatePicker
                fooddatePicker.setVisibility(View.VISIBLE);
                showDatePicker();
            }
        });
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
                        fooddatePicker.init(year, monthOfYear, dayOfMonth, null);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private String getSelectedDate() {
        // Get the selected date from the DatePicker
        int day = fooddatePicker.getDayOfMonth();
        int month = fooddatePicker.getMonth();
        int year = fooddatePicker.getYear();

        // Create a formatted date string
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return sdf.format(calendar.getTime());
    }
}
