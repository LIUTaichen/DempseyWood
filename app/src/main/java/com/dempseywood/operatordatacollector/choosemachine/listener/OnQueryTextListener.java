package com.dempseywood.operatordatacollector.choosemachine.listener;

import android.content.Context;
import android.content.Intent;
import android.widget.SearchView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.choosemachine.activity.ChooseMachineActivity;
import com.dempseywood.operatordatacollector.operatordetail.Machine;
import com.dempseywood.operatordatacollector.operatordetail.activity.OperatorDetailActivity;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

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
        Machine machine = new Machine();
        machine.setPlateNo(machineName);
        DataHolder.getInstance().setMachine(machine);
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
