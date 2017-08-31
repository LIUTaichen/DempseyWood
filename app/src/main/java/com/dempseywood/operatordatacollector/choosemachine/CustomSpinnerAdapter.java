package com.dempseywood.operatordatacollector.choosemachine;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.operatordetail.Machine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by musing on 03/08/2017.
 */

public class CustomSpinnerAdapter extends BaseAdapter implements Filterable {


    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Machine> items;
    private List<Machine> filteredMachines = new ArrayList<Machine>();
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
                ArrayList<Machine> tempList=new ArrayList<Machine>();
                //constraint is the result from text you want to filter against.
                //objects is your data set you will filter from
                if(constraint != null && items!=null) {
                    int length=items.size();
                    int i=0;
                    while(i<length){
                        Machine item=items.get(i);
                        //do whatever you wanna do here
                        //adding result set output array
                        if(item.getPlateNo().toLowerCase().contains((constraint.toString().toLowerCase()) )|| item.getDesc().toLowerCase().contains(constraint.toString().toLowerCase()) ){
                            tempList.add(item);
                            Log.e("CustomSpinnerAdapter", item.getPlateNo() + " is added to filtered result");
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
                filteredMachines = (ArrayList<Machine>) results.values;
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
        Machine machineData = filteredMachines.get(position);
        if(filteredMachines.contains(machineData)){
            final View view = mInflater.inflate(mResource, parent, false);
            TextView text = (TextView) view.findViewById(R.id.textView);
            text.setText(machineData.getPlateNo());
            return view;
        }
        return convertView;

    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }


}
