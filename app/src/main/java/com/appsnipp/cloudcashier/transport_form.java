package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class transport_form extends AppCompatActivity {

    private EditText titleEditText;
    private Spinner optionsSpinner;
    private EditText priceEditText;
    private EditText noteEditText;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_form);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        optionsSpinner = findViewById(R.id.optionsSpinner);
        priceEditText = findViewById(R.id.priceEditText);
        noteEditText = findViewById(R.id.noteEditText);
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
                // Get the values from the EditText fields
                String title = titleEditText.getText().toString();
                String selectedOption = optionsSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(priceEditText.getText().toString());
                String note = noteEditText.getText().toString();

                // Perform your logic here, e.g., save the data or show a message
                String message = "Title: " + title + "\nOption: " + selectedOption + "\nPrice: " + price + "\nNote: " + note;
                Toast.makeText(transport_form.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
