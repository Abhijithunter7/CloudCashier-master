package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class home_maintenance extends AppCompatActivity {

    private EditText editTitle, editPrice, noteEditText;
    private Spinner optionsSpinner;
    private Button saveButton;
    private FirebaseAuth auth;

    private DatabaseReference transportRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maintenance);

        // Initialize Firebase Database reference
        transportRef = FirebaseDatabase.getInstance().getReference().child("home_maintenance");
        auth = FirebaseAuth.getInstance();

        // Initialize views
        editTitle = findViewById(R.id.HomemaintenancetitleEditText);
        optionsSpinner = findViewById(R.id.HomemaintenanceoptionsSpinner);
        editPrice = findViewById(R.id.HomemaintenancepriceEditText);
        noteEditText = findViewById(R.id.HomemaintenancenoteEditText);
        saveButton = findViewById(R.id.HomemaintenancesaveButton);

        // Populate the Spinner with food options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.home_maintenance_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle save button click event
                saveData();
            }
        });
    }

    private void saveData() {
        // Implement your logic to save data
        String title = editTitle.getText().toString();
        String option = optionsSpinner.getSelectedItem().toString();
        double price = Double.parseDouble(editPrice.getText().toString());
        String note = noteEditText.getText().toString();

        String dateTime = getCurrentDateTime();
        FirebaseUser user = auth.getCurrentUser();
        String userName = user != null ? user.getDisplayName() : "Unknown User";

        // Create a new object to represent the data
        TransportData homeData = new TransportData(title, option,price, note,dateTime,userName);

        // Save the data to Firebase
        transportRef.push().setValue(homeData);

        // Show a message or perform other actions if needed
        String message = "Data saved to Firebase!";
        Toast.makeText(home_maintenance.this, message, Toast.LENGTH_SHORT).show();
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}