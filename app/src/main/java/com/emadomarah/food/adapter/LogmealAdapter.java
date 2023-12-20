package com.emadomarah.food.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emadomarah.food.R;
import com.emadomarah.food.model.LogmealItemModel;

import java.util.ArrayList;

public class LogmealAdapter extends RecyclerView.Adapter<LogmealAdapter.LogmealViewHolder> {

    private ArrayList<LogmealItemModel> foodList;

    public LogmealAdapter(ArrayList<LogmealItemModel> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public LogmealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LogmealViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.logmeal_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull LogmealViewHolder holder, int position) {
        LogmealItemModel logmealItemModel = foodList.get(position);

        holder.mealName.setText(logmealItemModel.getMealName());
        holder.mealinfo.setText(String.valueOf(logmealItemModel.getMealCal()) + " Cal, " +String.valueOf(logmealItemModel.getMealAmount())+ " g" );
    }


    @Override
    public int getItemCount() {
        return foodList.size();
    }


    public class LogmealViewHolder extends RecyclerView.ViewHolder{

        TextView mealName , mealinfo ;
        TextView totalCal ;

        public LogmealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.mealName_txt);
            mealinfo = itemView.findViewById(R.id.mealInfo_txt);
            totalCal = itemView.findViewById(R.id.logmeal_total_cal);

        }
    }


}
