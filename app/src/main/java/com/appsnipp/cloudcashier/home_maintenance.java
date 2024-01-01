package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home_maintenance extends AppCompatActivity {

    private EditText titleEditText, editPrice, noteEditText;
    private Spinner optionsSpinner;
    private Button saveButton;

    private DatabaseReference transportRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maintenance);

        // Initialize Firebase Database reference
        transportRef = FirebaseDatabase.getInstance().getReference().child("home_maintenance");

        // Initialize views
        titleEditText = findViewById(R.id.HomemaintenancetitleEditText);
        optionsSpinner = findViewById(R.id.HomemaintenanceoptionsSpinner);
        editPrice = findViewById(R.id.HomemaintenancepriceEditText);
        noteEditText = findViewById(R.id.HomemaintenancenoteEditText);
        saveButton = findViewById(R.id.HomemaintenancesaveButton);

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
        String title = titleEditText.getText().toString();
        String options = optionsSpinner.getSelectedItem().toString();
        double price = Double.parseDouble(editPrice.getText().toString());
        String note = noteEditText.getText().toString();


        // Create a new object to represent the data
        home_maintenance_data homeData = new home_maintenance_data(title, options, price, note);

        // Save the data to Firebase
        transportRef.push().setValue(homeData);

        // Show a message or perform other actions if needed
        String message = "Data saved to Firebase!";
        Toast.makeText(home_maintenance.this, message, Toast.LENGTH_SHORT).show();
    }
}