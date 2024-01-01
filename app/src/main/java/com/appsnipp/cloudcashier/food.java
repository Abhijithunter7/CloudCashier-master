package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class food extends AppCompatActivity {

    private EditText editTitle;
    private Spinner spinOptions;
    private EditText editPrice;
    private EditText editNote;
    private Button saveButton;

    private DatabaseReference transportRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food); // Replace with your XML layout filename

        // Initialize Firebase Database reference
        transportRef = FirebaseDatabase.getInstance().getReference().child("food");

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


                // Create a new object to represent the data
                fooddata foodda = new fooddata(title, selectedOption, price, note);

                // Save the data to Firebase
                transportRef.push().setValue(foodda);

                // Show a message or perform other actions if needed
                String message = "Data saved to Firebase!";
                Toast.makeText(food.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
