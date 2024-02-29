package com.appsnipp.cloudcashier;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class recycleview extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_item);

        // Initialize Firebase reference (replace with your actual path)
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("transport");

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recycleid);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create an adapter and set it to the RecyclerView
        adapter = new MyAdapter(this, databaseReference);
        recyclerView.setAdapter(adapter);
    }
}
