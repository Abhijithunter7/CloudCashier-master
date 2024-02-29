package com.appsnipp.cloudcashier;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.rxjava3.annotations.NonNull;

public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, option, price, note, date;

        public MyViewHolder(@NonNull View itemView){
                super(itemView);

                title = itemView.findViewById(R.id.tittlerecy);
                option = itemView.findViewById(R.id.optionrecy);
                price= itemView.findViewById(R.id.pricerecy);
                note = itemView.findViewById(R.id.noterecy);
                date = itemView.findViewById(R.id.datels);
        }
}
