package com.appsnipp.cloudcashier;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class mainadoptor extends FirebaseRecyclerAdapter<Mainmodel,mainadoptor.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public mainadoptor(@NonNull FirebaseRecyclerOptions<Mainmodel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Mainmodel model) {

        holder.tittle.setText(model.getTitle());
        holder.selectedoption.setText(model.getSelectedOptions());
        holder.price.setText((int) model.getPrice());
        holder.note.setText(model.getNote());

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);

        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        TextView tittle, selectedoption, price, note;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            tittle =(TextView)itemView.findViewById(R.id.tittlerecy);
            selectedoption=(TextView)itemView.findViewById(R.id.optionrecy);
            price =(TextView)itemView.findViewById(R.id.pricerecy);
            note =(TextView)itemView.findViewById(R.id.noterecy);
        }
    }
}
