package com.dempseywood.operatordatacollector.choosemachine.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.choosemachine.listener.OnQueryTextListener;
import com.dempseywood.operatordatacollector.database.DbHelper;
import com.dempseywood.operatordatacollector.database.dao.MachineDAO;
import com.dempseywood.operatordatacollector.choosemachine.CustomSpinnerAdapter;
import com.dempseywood.operatordatacollector.operatordetail.Machine;
import com.dempseywood.operatordatacollector.operatordetail.activity.OperatorDetailActivity;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

import java.util.List;

public class ChooseMachineActivity extends AppCompatActivity {

    private MachineDAO machineDAO;



    private CustomSpinnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_machine);

        machineDAO = new MachineDAO(new DbHelper(this));

        SearchView searchView = (SearchView)findViewById(R.id.machine_search);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);


        ListView machineListView = (ListView)findViewById(R.id.machine_result_list);

        List<Machine> machines = machineDAO.findAllMachines();

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_layout, machines);
        this.setAdapter(adapter);
        machineListView.setAdapter(adapter);
        machineListView.setTextFilterEnabled(true);

        machineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Machine machine = (Machine) parent.getItemAtPosition(position);
                DataHolder.getInstance().setMachine(machine);

                ChooseMachineActivity activity = (ChooseMachineActivity) view.getContext();
                Intent intent = new Intent(activity, OperatorDetailActivity.class);
                activity.startActivity(intent);
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseMachineActivity activity = (ChooseMachineActivity) v.getContext();
                Intent intent = new Intent(activity, OperatorDetailActivity.class);
                activity.startActivity(intent);
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChooseMachineActivity activity = (ChooseMachineActivity) v.getContext();
                SearchView searchView = (SearchView)findViewById(R.id.machine_search);
                String machineName = searchView.getQuery().toString();
                DataHolder.getInstance().getMachine().setPlateNo(machineName);


                Intent intent = new Intent(activity, OperatorDetailActivity.class);
                activity.startActivity(intent);
            }
        });


        searchView.setOnQueryTextListener(new OnQueryTextListener(this)) ;




    }

    public CustomSpinnerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CustomSpinnerAdapter adapter) {
        this.adapter = adapter;
    }
}
