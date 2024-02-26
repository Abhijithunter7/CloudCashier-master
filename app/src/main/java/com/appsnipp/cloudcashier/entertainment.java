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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class entertainment extends AppCompatActivity {

    private EditText editTitle;
    private Spinner optionsSpinner;
    private EditText editPrice;
    private EditText editNote;
    private Button saveButton;
    private FirebaseAuth auth;

    private DatabaseReference transportRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entertainment); // Replace with your XML layout filename

        // Initialize Firebase Database reference
        transportRef = FirebaseDatabase.getInstance().getReference().child("entertainment");
        auth = FirebaseAuth.getInstance();

        // Initialize UI elements
        editTitle = findViewById(R.id.entertainmenttitleEditText);
        optionsSpinner = findViewById(R.id.entertainmentoptionsSpinner);
        editPrice = findViewById(R.id.entertainmentpriceEditText);
        editNote = findViewById(R.id.entertainmentnoteEditText);
        saveButton = findViewById(R.id.entertainmentsaveButton);

        // Populate the Spinner with entertainment options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.entertainment_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);

        // Set an OnClickListener for the Save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values entered by the user
                String title = editTitle.getText().toString();
                String option = optionsSpinner.getSelectedItem().toString();
                double price = Double.parseDouble(editPrice.getText().toString());
                String note = editNote.getText().toString();

                String dateTime = getCurrentDateTime();
                FirebaseUser user = auth.getCurrentUser();
                String userName = user != null ? user.getDisplayName() : "Unknown User";
                // Create a new object to represent the data
                TransportData trans1 = new TransportData(title, option, price, note,dateTime,userName);

                // Save the data to Firebase
                transportRef.push().setValue(trans1);

                // Show a message or perform other actions if needed
                String message = "Data saved to Firebase!";
                Toast.makeText(entertainment.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
