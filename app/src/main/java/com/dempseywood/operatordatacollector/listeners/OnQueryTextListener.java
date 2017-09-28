package com.dempseywood.operatordatacollector.listeners;

import android.content.Context;
import android.content.Intent;
import android.widget.SearchView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.activities.ChooseMachineActivity;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.activities.OperatorDetailActivity;
import com.dempseywood.operatordatacollector.models.DataHolder;

/**
 * Created by musing on 15/08/2017.
 */

public class OnQueryTextListener implements SearchView.OnQueryTextListener{

    private Context context;

    public OnQueryTextListener(Context context){
        this.context = context;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        ChooseMachineActivity activity = (ChooseMachineActivity) context;
        SearchView searchView = (SearchView)activity.findViewById(R.id.machine_search);
        String machineName = searchView.getQuery().toString();
        Equipment machine = new Equipment();
        machine.setName(machineName);
        DataHolder.getInstance().setEquipment(machine);
        Intent intent = new Intent(activity, OperatorDetailActivity.class);
        activity.startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ChooseMachineActivity activity = (ChooseMachineActivity) context;
        activity.getAdapter().getFilter().filter(newText);
        return false;
    }
}
