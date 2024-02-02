package com.appsnipp.cloudcashier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class forget_password extends AppCompatActivity {

    private Button forgetbut;
    private EditText txtemail;
    private String Email;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        auth = FirebaseAuth.getInstance();

        txtemail = findViewById(R.id.forgotemail);
        forgetbut = findViewById(R.id.continuebutton);

        forgetbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatedata();
            }
        });

    }

    private void validatedata(){

        Email = txtemail.getText().toString();
        if (Email.isEmpty()){
            txtemail.setError("Required");
        }else {
            forgetpass();
        }
    }

    private void forgetpass(){

        auth.sendPasswordResetEmail(Email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            Toast.makeText(forget_password.this, "Check your Email", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(forget_password.this, MainActivity.class));
                            finish();

                        }else {

                            Toast.makeText(forget_password.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}

