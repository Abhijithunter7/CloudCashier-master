package com.appsnipp.cloudcashier;

// transport_form.java
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class transport_form extends AppCompatActivity {

    private EditText titleEditText;
    private Spinner optionsSpinner;
    private EditText priceEditText;
    private EditText editNote;
    private Button saveButton;

    private DatabaseReference transportRef;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_form);

        // Initialize Firebase Database reference
        transportRef = FirebaseDatabase.getInstance().getReference().child("transport");

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance();

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        optionsSpinner = findViewById(R.id.optionsSpinner);
        priceEditText = findViewById(R.id.priceEditText);
        editNote = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);

        // Set up the Spinner (options)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.options_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);

        // Handle Spinner item selection
        optionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // You can do something with the selected option here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Handle save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the EditText fields
                String title = titleEditText.getText().toString();
                String option = optionsSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                String note = editNote.getText().toString();

                // Get the current date and time
                String dateTime = getCurrentDateTime();

                // Get the current user's name
                FirebaseUser user = auth.getCurrentUser();
                String userName = user != null ? user.getDisplayName() : "Unknown User";

                // Create a new object to represent the data
                TransportData transportData = new TransportData(title, option, price, note, dateTime, userName);

                // Save the data to Firebase
                transportRef.push().setValue(transportData);

                // Show a message or perform other actions if needed
                String message = "Data saved to Firebase!";
                Toast.makeText(transport_form.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to get current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}