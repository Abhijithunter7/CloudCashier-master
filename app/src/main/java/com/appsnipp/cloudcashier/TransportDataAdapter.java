package com.appsnipp.cloudcashier;

import android.content.Context; // Import Context for adapter
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransportDataAdapter extends RecyclerView.Adapter<TransportDataAdapter.TransportDataViewHolder> {

    private List<TransportData> transportDataList;
    private final Context context; // Store context passed to constructor

    public TransportDataAdapter(List<TransportData> transportDataList, Context context) {
        this.transportDataList = transportDataList;
        this.context = context; // Save the context
    }

    @NonNull
    @Override
    public TransportDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false);
        return new TransportDataViewHolder(itemView, context); // Pass context to ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull TransportDataViewHolder holder, int position) {
        TransportData transportData = transportDataList.get(position);

        // Bind data to the ViewHolder
        holder.titleTextView.setText(transportData.getTitle());
        holder.optionTextView.setText(transportData.getselectedOption());
        holder.priceTextView.setText(String.valueOf(transportData.getPrice()));
        holder.noteTextView.setText(transportData.getNote());
        holder.dateTextView.setText(transportData.getselectedDate());
    }

    @Override
    public int getItemCount() {
        return transportDataList.size();
    }

    public static class TransportDataViewHolder extends RecyclerView.ViewHolder { // Make inner class static for better memory management

        private final TextView titleTextView, optionTextView, priceTextView, noteTextView, dateTextView;

        public TransportDataViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tittlerecy);
            optionTextView = itemView.findViewById(R.id.optionrecy);
            priceTextView = itemView.findViewById(R.id.pricerecy);
            noteTextView = itemView.findViewById(R.id.noterecy);
            dateTextView = itemView.findViewById(R.id.datels);

            // Consider adding click and long click listeners here if needed, using the provided context
        }
    }
}
