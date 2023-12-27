package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class home_maintenance extends AppCompatActivity {

    private EditText titleEditText, priceEditText, noteEditText;
    private Spinner optionsSpinner;
    private Button saveButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_maintenance);

        // Initialize views
        titleEditText = findViewById(R.id.HomemaintenancetitleEditText);
        optionsSpinner = findViewById(R.id.HomemaintenanceoptionsSpinner);
        priceEditText = findViewById(R.id.HomemaintenancepriceEditText);
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
        String price = priceEditText.getText().toString();
        String note = noteEditText.getText().toString();

        // Perform your logic here, e.g., save the data or show a message
        String message = "Title: " + title + "\nOption: " + options + "\nPrice: " + price + "\nNote: " + note;
        Toast.makeText(home_maintenance.this, message, Toast.LENGTH_SHORT).show();

        // Add your logic to save this data to your database or perform any other actions.
        // For example, you might want to create a HomeMaintenance class and save an instance of it.
    }
}