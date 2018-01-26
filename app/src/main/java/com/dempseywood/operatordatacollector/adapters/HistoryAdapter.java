package com.dempseywood.operatordatacollector.adapters;

import android.opengl.Visibility;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.models.Haul;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by musing on 27/09/2017.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private List<Haul> dataset;
    private  boolean isEditActivated = false;
    private  SparseBooleanArray selectionArray =  new SparseBooleanArray();;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView indexTextView;
        public TextView statusTextView;
        public TextView loadTimeTextView;
        public TextView unloadTimeTextView;
        public RadioButton radioButton;
        public ViewHolder(LinearLayout v) {
            super(v);
            indexTextView = v.findViewById(R.id.index);
            statusTextView = v.findViewById(R.id.status_text);
            loadTimeTextView = v.findViewById(R.id.load_time_text);
            unloadTimeTextView = v.findViewById(R.id.unload_time_text);
            radioButton = v.findViewById(R.id.radioButton);
        }


    }

    private  void onItemClick(View v, Integer position){
        if(isEditActivated){
            boolean isSelected = selectionArray.get(position);
            selectionArray.put(position, !isSelected);
            this.notifyItemChanged(position);
        }

    }

    public HistoryAdapter(List<Haul> data){
        this.dataset = data;


    }
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, final int position) {
        Haul haul = dataset.get(position);
        holder.indexTextView.setText(position + 1 + "");
        if(isEditActivated){
            holder.indexTextView.setVisibility(View.GONE);
            holder.radioButton.setVisibility(View.VISIBLE);
            holder.radioButton.setChecked(selectionArray.get(position));
        }else{
            holder.indexTextView.setVisibility(View.VISIBLE);
            holder.radioButton.setVisibility(View.GONE);
        }
        holder.statusTextView.setText(haul.getTask());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        holder.loadTimeTextView.setText(sdf.format(haul.getLoadTime()));
        String unloadTimeText = "";
        if(haul.getUnloadTime() != null) {
            unloadTimeText = sdf.format(haul.getUnloadTime());
        }
        holder.unloadTimeTextView.setText(unloadTimeText);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void startEditing(){
        this.isEditActivated = true;
        this.notifyItemRangeChanged(0, dataset.size());
    }

    public void stopEditing(){
        this.isEditActivated = false;
        selectionArray.clear();
        this.notifyItemRangeChanged(0, dataset.size());
    }

    public boolean isEditing(){
        return this.isEditActivated;
    }
}
