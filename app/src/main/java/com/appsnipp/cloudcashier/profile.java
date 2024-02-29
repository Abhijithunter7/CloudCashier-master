package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class profile extends AppCompatActivity {

    private EditText editName, editDOB, editEmergencyContact, editAddress, spinnerBloodGroup;
    private TextView editEmail;  // Change from EditText to TextView
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale, radioButtonFemale;
    private Button buttonSave;

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Realtime Database
        databaseRef = FirebaseDatabase.getInstance().getReference("user_profiles");

        // Initialize UI elements
        editName = findViewById(R.id.EditName);
        editEmail = findViewById(R.id.user_email_text_view);
        editDOB = findViewById(R.id.EditDOB);
        editEmergencyContact = findViewById(R.id.EditEmergencyContact);
        editAddress = findViewById(R.id.EditAddress);
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        radioButtonMale = findViewById(R.id.radioButtonMale);
        radioButtonFemale = findViewById(R.id.radioButtonFemale);
        buttonSave = findViewById(R.id.buttonSave);

        // Load user data if available
        loadUserData();

        // Set click listener for Save button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            // Display user email in TextView
            editEmail.setText(userEmail);

            // Retrieve user profile data from Firebase Realtime Database
            String userId = currentUser.getUid();
            DatabaseReference userRef = databaseRef.child(userId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "profile updated", Toast.LENGTH_SHORT).show();
                    profiledata userProfile = task.getResult().getValue(profiledata.class);
                    if (userProfile != null) {
                        // Populate EditText fields with user data
                        editName.setText(userProfile.getName());
                        editDOB.setText(userProfile.getDob());
                        editEmergencyContact.setText(userProfile.getEmergencyContact());
                        editAddress.setText(userProfile.getAddress());
                        spinnerBloodGroup.setText(userProfile.getBloodGroup());

                        // Set radio button based on gender
                        if (userProfile.getGender().equals("Male")) {
                            radioButtonMale.setChecked(true);
                        } else {
                            radioButtonFemale.setChecked(true);
                        }
                    }
                }
            });
        }
    }

    private void saveUserProfile() {
        String name = editName.getText().toString().trim();
        String dob = editDOB.getText().toString().trim();
        String emergencyContact = editEmergencyContact.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String bloodGroup = spinnerBloodGroup.getText().toString().trim();
        String gender = (radioButtonMale.isChecked()) ? "Male" : "Female";

        Toast.makeText(this, "profile updated", Toast.LENGTH_SHORT).show();

        // Get the user's email from loadUserData()
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser.getEmail();

        // Create a User object
        profiledata userProfile = new profiledata(name, email, dob, emergencyContact, address, bloodGroup, gender);

        // Push the user data to Firebase Realtime Database
        String userId = currentUser.getUid();
        databaseRef.child(userId).setValue(userProfile);
    }
}
