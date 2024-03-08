package com.appsnipp.cloudcashier;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class transportrecy extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransportDataAdapter transportDataAdapter;
    private List<TransportData> transportDataList;
    private DatabaseReference transportRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_item); // Replace with your actual layout file name

        // Initialize data list
        transportDataList = new ArrayList<>();

        // Initialize Firebase Database reference
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
        FirebaseUser ccurrentUser = auth.getCurrentUser();
        if (ccurrentUser != null) {
            String userid = ccurrentUser.getUid();
            transportRef = mdatabase.getReference().child(userid).child("transport");
        }

        // Initialize recycler view
        recyclerView = findViewById(R.id.recycleid); // Replace with the actual ID from your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set adapter
        transportDataAdapter = new TransportDataAdapter(transportDataList, this);  // Pass context to adapter
        recyclerView.setAdapter(transportDataAdapter);

        // Fetch data from Firebase and update the adapter
        fetchDataFromFirebase();
    }

    private void fetchDataFromFirebase() {
        transportRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                transportDataList.clear(); // Clear the existing data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Parse the data and add it to the list
                    TransportData transportData = dataSnapshot.getValue(TransportData.class);
                    if (transportData != null) {
                        transportDataList.add(transportData);
                    }
                }
                // Notify the adapter that the data set has changed
                transportDataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
    }
}
