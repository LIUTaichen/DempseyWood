package com.dempseywood.operatordatacollector.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.models.Task;

import java.util.List;

/**
 * Created by Jason.Liu on 2/02/2018.
 */

public class TaskAdatpter extends ArrayAdapter<Task> {
    private List<Task> taskList;

    public TaskAdatpter(@NonNull Context context, int layout_resource, int textViewResourceId, List<Task> tasks) {
        super(context, layout_resource, textViewResourceId, tasks);
        this.taskList = tasks;

    }





    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(this.getContext()).inflate(R.layout.bottom_sheet_task_list_item_layout,parent,false);
        }


        Task currentTask = taskList.get(position);


        TextView name = (TextView) listItem.findViewById(R.id.taskName);
        name.setText(currentTask.getName());

        return listItem;
    }




}
