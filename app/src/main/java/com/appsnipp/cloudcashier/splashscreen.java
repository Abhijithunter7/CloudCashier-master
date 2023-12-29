package com.appsnipp.cloudcashier;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splashscreen extends AppCompatActivity {

    // Duration of the splash screen in milliseconds
    private static final int SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        // Using a handler to delay the start of the MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start the MainActivity
                Intent intent = new Intent(splashscreen.this, MainActivity.class);
                startActivity(intent);

                // Close the splash screen activity
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}
