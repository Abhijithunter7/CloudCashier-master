package com.appsnipp.cloudcashier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TransportDataAdapter extends RecyclerView.Adapter<TransportDataAdapter.TransportDataViewHolder> {

    private List<TransportData> transportDataList;
    private final Context context;
    private OnItemClickListener listener; // Listener for item click events

    // Interface for handling item click events
    public interface OnItemClickListener {
        void onLongClickListener(int position);
        void onNoteClickListener(int position);
    }

    // Constructor to initialize the adapter with data and context
    public TransportDataAdapter(List<TransportData> transportDataList, Context context) {
        this.transportDataList = transportDataList;
        this.context = context;
    }

    // Setter method for setting the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransportDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_item, parent, false);
        return new TransportDataViewHolder(itemView);
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

        // Set click listeners
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onLongClickListener(position); // Notify listener about the long click
                }
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNoteClickListener(position); // Notify listener about the item click
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return transportDataList.size();
    }

    public static class TransportDataViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView, optionTextView, priceTextView, noteTextView, dateTextView;

        public TransportDataViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tittlerecy);
            optionTextView = itemView.findViewById(R.id.optionrecy);
            priceTextView = itemView.findViewById(R.id.pricerecy);
            noteTextView = itemView.findViewById(R.id.noterecy);
            dateTextView = itemView.findViewById(R.id.datels);
        }
    }
}