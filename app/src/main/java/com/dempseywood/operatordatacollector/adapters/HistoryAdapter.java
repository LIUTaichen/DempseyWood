package com.dempseywood.operatordatacollector.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView indexTextView;
        public TextView statusTextView;
        public TextView loadTimeTextView;
        public TextView unloadTimeTextView;
        public ViewHolder(LinearLayout v) {
            super(v);
            indexTextView = v.findViewById(R.id.index);
            statusTextView = v.findViewById(R.id.status_text);
            loadTimeTextView = v.findViewById(R.id.load_time_text);
            unloadTimeTextView = v.findViewById(R.id.unload_time_text);
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
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        Haul haul = dataset.get(position);
        holder.indexTextView.setText(position + 1 + "");
        holder.statusTextView.setText(haul.getTask());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        holder.loadTimeTextView.setText(sdf.format(haul.getLoadTime()));
        String unloadTimeText = "";
        if(haul.getUnloadTime() != null) {
            unloadTimeText = sdf.format(haul.getUnloadTime());
        }
            holder.unloadTimeTextView.setText(unloadTimeText);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
