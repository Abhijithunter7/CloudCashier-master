package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class food extends AppCompatActivity {

    private EditText editTitle;
    private Spinner spinOptions;
    private EditText editPrice;
    private EditText editNote;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food); // Replace with your XML layout filename

        // Initialize UI elements
        editTitle = findViewById(R.id.foodtitleEditText);
        spinOptions = findViewById(R.id.foodoptionsSpinner);
        editPrice = findViewById(R.id.foodpriceEditText);
        editNote = findViewById(R.id.foodnoteEditText);
        saveButton = findViewById(R.id.foodsaveButton);

        // Populate the Spinner with food options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.food_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinOptions.setAdapter(adapter);

        // Set an OnClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values entered by the user
                String title = editTitle.getText().toString();
                String selectedOption = spinOptions.getSelectedItem().toString();
                double price = Double.parseDouble(editPrice.getText().toString());
                String note = editNote.getText().toString();

                // Do something with the user's input, e.g., save it to a database
                // You can add your logic here

                // Clear the input fields if needed
                editTitle.setText("");
                editPrice.setText("");
                editNote.setText("");
            }
        });
    }
}
