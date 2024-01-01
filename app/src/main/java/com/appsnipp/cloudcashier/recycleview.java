package com.appsnipp.cloudcashier;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class recycleview extends AppCompatActivity {

    RecyclerView recyclerView;
    mainadoptor mainadoptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycleview);

        recyclerView =(RecyclerView)findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Mainmodel> options =
                new FirebaseRecyclerOptions.Builder<Mainmodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("transport"), Mainmodel.class)
                        .build();

        mainadoptor = new mainadoptor(options);
        recyclerView.setAdapter(mainadoptor);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mainadoptor.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainadoptor.stopListening();
    }
}