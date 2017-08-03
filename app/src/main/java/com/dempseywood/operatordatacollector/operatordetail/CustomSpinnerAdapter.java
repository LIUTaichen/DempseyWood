package com.dempseywood.operatordatacollector.operatordetail;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dempseywood.operatordatacollector.R;

import java.util.List;

/**
 * Created by musing on 03/08/2017.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Machine> items;
    private final int mResource;

    public CustomSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull List objects){
        super(context, resource, 0, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
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
        final View view = mInflater.inflate(mResource, parent, false);

        TextView text = (TextView) view.findViewById(R.id.textView);

        Machine machineData = items.get(position);

        text.setText(machineData.getPlateNo());

        return view;
    }
}
