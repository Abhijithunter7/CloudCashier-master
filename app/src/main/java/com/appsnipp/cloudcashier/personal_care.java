package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class personal_care extends AppCompatActivity {

    private EditText titleEditText, priceEditText, noteEditText;
    private Spinner optionsSpinner;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_care);

        // Initialize UI elements
        titleEditText = findViewById(R.id.personalcaretitleEditText);
        priceEditText = findViewById(R.id.personalcarepriceEditText);
        noteEditText = findViewById(R.id.personalcarenoteEditText);
        optionsSpinner = findViewById(R.id.personalcareoptionsSpinner);
        saveButton = findViewById(R.id.personalcaresaveButton);

        // Set up the options for the spinner
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
                String selectedOption = parentView.getItemAtPosition(position).toString();
                // You can do something with the selected option here
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Set up onClickListener for the save button
        // Handle save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the EditText fields
                String title = titleEditText.getText().toString();
                String option = optionsSpinner.getSelectedItem().toString();
                String price = priceEditText.getText().toString();
                String note = noteEditText.getText().toString();

                // Perform your logic here, e.g., save the data or show a message
                String message = "Title: " + title + "\nOption: " + option + "\nPrice: " + price + "\nNote: " + note;
                Toast.makeText(personal_care.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}