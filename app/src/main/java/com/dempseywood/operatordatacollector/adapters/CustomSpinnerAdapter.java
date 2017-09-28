package com.dempseywood.operatordatacollector.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.models.Equipment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by musing on 03/08/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements Filterable {


    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Equipment> items;
    private List<Equipment> filteredMachines = new ArrayList<Equipment>();
    private final int mResource;
    private Filter myFilter;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull List objects){
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
        filteredMachines.addAll(items);
        myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                ArrayList<Equipment> tempList=new ArrayList<Equipment>();
                //constraint is the result from text you want to filter against.
                //objects is your data set you will filter from
                if(constraint != null && items!=null) {
                    int length=items.size();
                    int i=0;
                    while(i<length){
                        Equipment item=items.get(i);
                        //do whatever you wanna do here
                        //adding result set output array
                        if(item.getName().toLowerCase().contains((constraint.toString().toLowerCase()) )|| item.getCategory().toLowerCase().contains(constraint.toString().toLowerCase()) ){
                            tempList.add(item);
                            Log.e("CustomSpinnerAdapter", item.getName() + " is added to filtered result");
                        }
                        i++;
                    }
                    //following two lines is very important
                    //as publish result can only take FilterResults objects
                    filterResults.values = tempList;
                    filterResults.count = tempList.size();
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                filteredMachines = (ArrayList<Equipment>) results.values;
                if (results.count > 0) {
                    Log.e("CustomSpinnerAdapter"," notify called");

                }
                notifyDataSetChanged();
            }
        };
        if(getFilter() == null){
            Log.e("CustomSpinnerAdapter", "filter is null");
        }
        Log.e("CustomSpinnerAdapter", "initialized");
    }


    @Override
    public int getCount() {
        return filteredMachines.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredMachines.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        Equipment machineData = filteredMachines.get(position);
        if(filteredMachines.contains(machineData)){
            final View view = mInflater.inflate(mResource, parent, false);
            TextView text = (TextView) view.findViewById(R.id.textView);
            text.setText(machineData.getName());
            return view;
        }
        return convertView;

    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }


}
