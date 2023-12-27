package com.appsnipp.cloudcashier;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class utilities_form extends AppCompatActivity {

    private EditText titleEditText;
    private Spinner optionsSpinner;
    private EditText priceEditText;
    private EditText noteEditText;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities_form);

        // Initialize UI elements
        titleEditText = findViewById(R.id.utiedit1);
        optionsSpinner = findViewById(R.id.utispin1);
        priceEditText = findViewById(R.id.utiedit2);
        noteEditText = findViewById(R.id.utiedit3);
        saveButton = findViewById(R.id.saveButton1);

        // Set an OnClickListener for the saveButton

        // Set up the Spinner (options)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.bill_options,
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

        // Handle save button click
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from the UI elements
                String title = titleEditText.getText().toString();
                String option = optionsSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                String note = noteEditText.getText().toString();



                // Perform your logic here, e.g., save the data or show a message
                String message = "Title: " + title + "\nOption: " + option + "\nPrice: " + price + "\nNote: " + note;
                Toast.makeText(utilities_form.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

