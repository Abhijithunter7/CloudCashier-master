package com.appsnipp.cloudcashier;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class registration extends AppCompatActivity {

    TextInputEditText editTextusername, editTextemail, editTextpassword, editTextconfirmPassword;
    Button signUp;
    TextView alreadyUser;
    FirebaseAuth mAuth;
    ProgressBar progressBar;


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(registration.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.regspro1);

        signUp = findViewById(R.id.regsign_up_button);
        editTextusername = findViewById(R.id.reguser);
        editTextemail = findViewById(R.id.regemail);
        editTextpassword = findViewById(R.id.regpass);
        editTextconfirmPassword = findViewById(R.id.regconf_pass);
        alreadyUser = findViewById(R.id.already_user_textview);


        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registration.this, MainActivity.class);
                startActivity(intent);
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String username, email, password, confirmpassword;
                username = String.valueOf(editTextusername.getText());
                email = String.valueOf(editTextemail.getText());
                password = String.valueOf(editTextpassword.getText());
                confirmpassword = String.valueOf(editTextconfirmPassword.getText());

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(registration.this, "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(registration.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(registration.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confirmpassword)) {
                    Toast.makeText(registration.this, "Confirm the password", Toast.LENGTH_SHORT).show();
                    return;

                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration success
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    // Check if the user is not null
                                    if (user != null) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username) // Set the username
                                                .build();

                                        user.updateProfile(profileUpdates) // Update user profile with username
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.setVisibility(View.GONE);
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(registration.this, "Account Created", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    // If sign-in fails, display a message to the user.
                                    Toast.makeText(registration.this, "Registration failed: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }


        });
    }
}