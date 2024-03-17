package com.appsnipp.cloudcashier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class personalrecy extends AppCompatActivity implements TransportDataAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TransportDataAdapter transportDataAdapter;
    private List<TransportData> transportDataList;
    private DatabaseReference personalref;

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
            personalref = mdatabase.getReference().child(userid).child("personal care");
        }

        // Initialize recycler view
        recyclerView = findViewById(R.id.recycleid); // Replace with the actual ID from your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set adapter
        transportDataAdapter = new TransportDataAdapter(transportDataList, this);  // Pass context to adapter
        recyclerView.setAdapter(transportDataAdapter);

        // Fetch data from Firebase and update the adapter
        fetchDataFromFirebase();
        // Set item click listener
        transportDataAdapter.setOnItemClickListener(this);
    }

    private void fetchDataFromFirebase() {
        personalref.addValueEventListener(new ValueEventListener() {
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

    @Override
    public void onNoteClickListener(int position) {
        TransportData clickedNote = transportDataList.get(position);
        Intent intent = new Intent(personalrecy.this, personal_care.class);
        intent.putExtra("noteId", clickedNote.getId());
        startActivity(intent);
    }
    @Override
    public void onLongClickListener(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteNote(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteNote(int position) {
        TransportData noteToDelete = transportDataList.get(position);
        String noteId = noteToDelete.getId();
        DatabaseReference noteRef = personalref.child(noteId);
        noteRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    Toast.makeText(personalrecy.this, "Failed to delete data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(personalrecy.this, "data deleted successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
