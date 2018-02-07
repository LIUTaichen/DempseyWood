package com.dempseywood.operatordatacollector.adapters;

import android.opengl.Visibility;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.activities.HistoryActivity;
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
    private  SparseBooleanArray selectionArray =  new SparseBooleanArray();
    private static final String tag = "historyAdapter";
    private HistoryActivity activity;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView indexTextView;
        public TextView statusTextView;
        public TextView loadTimeTextView;
        public TextView unloadTimeTextView;
        public RadioButton radioButton;
        public LinearLayout rightLayout;
        public ViewHolder(LinearLayout v) {
            super(v);
            indexTextView = v.findViewById(R.id.index);
            statusTextView = v.findViewById(R.id.status_text);
            loadTimeTextView = v.findViewById(R.id.load_time_text);
            unloadTimeTextView = v.findViewById(R.id.unload_time_text);
            radioButton = v.findViewById(R.id.radioButton);
            rightLayout = v.findViewById(R.id.rightLayout);
        }


    }

    private  void onItemClick(View v, Integer position){
        if(isEditActivated){
            boolean isSelected = selectionArray.get(position);
            Log.d(tag, "old state: " + isSelected + " size: " + selectionArray.size());
            selectionArray.put(position, !isSelected);
            Log.d(tag, "new  state: " + selectionArray.get(position) + " size: " + selectionArray.size());
            this.notifyItemChanged(position);
            boolean isAllFalse = true;
            for (int i = 0; i <selectionArray.size(); i ++){
                int key = selectionArray.keyAt(i);
                if(selectionArray.get(key)){
                    isAllFalse = false;
                }
            }
            activity.showChangeTaskButton(!isAllFalse);
        }


    }

    public HistoryAdapter(List<Haul> data, HistoryActivity activity){
        this.dataset = data;
        this.activity = activity;

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
                Log.i(tag, "radio button event.");
                onItemClick(v,position);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(tag, "view event.");
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
        activity.showChangeTaskButton(false);
        this.notifyItemRangeChanged(0, dataset.size());
    }

    public boolean isEditing(){
        return this.isEditActivated;
    }

    public SparseBooleanArray getSelectionArray(){
        return selectionArray;
    }

    public List<Haul> getSelectedHauls(){
        List<Haul> selectedHauls = new ArrayList<>();
        SparseBooleanArray selected = this.getSelectionArray();
        Log.d(tag, "size:" +  selected.size());
        for (int i = 0; i <selected.size(); i ++){
            int key = selected.keyAt(i);
            if(selected.get(key)){
                selectedHauls.add(dataset.get(key));
            }
        }
        return selectedHauls;
    }
}
