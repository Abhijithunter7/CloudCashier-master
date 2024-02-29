package com.appsnipp.cloudcashier;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<item> items;
    private DatabaseReference databaseReference;

    public MyAdapter(Context context, DatabaseReference databaseReference) {
        this.context = context;
        this.databaseReference = databaseReference;
        this.items = new ArrayList<>(); // Initialize empty list

        // Read data from Firebase
        fetchData();
    }

    private void fetchData() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        item newItem = childSnapshot.getValue(item.class);
                        items.add(newItem);
                    }
                    notifyDataSetChanged(); // Notify adapter about data change
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("MyAdapter", "Error fetching data", error.toException());
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.transport_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        item currentItem = items.get(position);
        holder.title.setText(currentItem.getTitle());
        holder.option.setText(currentItem.getOption());
        holder.price.setText((int) currentItem.getPrice());
        holder.note.setText(currentItem.getNote());
        holder.date.setText(currentItem.getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

