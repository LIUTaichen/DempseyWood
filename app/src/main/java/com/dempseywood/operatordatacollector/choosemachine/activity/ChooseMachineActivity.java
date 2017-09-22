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
import com.dempseywood.operatordatacollector.choosemachine.CustomSpinnerAdapter;
import com.dempseywood.operatordatacollector.database.db.DB;
import com.dempseywood.operatordatacollector.database.db.dao.EquipmentDao;
import com.dempseywood.operatordatacollector.database.db.entity.Equipment;
import com.dempseywood.operatordatacollector.operatordetail.activity.OperatorDetailActivity;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

import java.util.List;

public class ChooseMachineActivity extends AppCompatActivity {


    private EquipmentDao equipmentDao;



    private CustomSpinnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_machine);

        DB.init(getApplicationContext());
        equipmentDao =  DB.getInstance().equipmentDao();


        SearchView searchView = (SearchView)findViewById(R.id.machine_search);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);


        ListView machineListView = (ListView)findViewById(R.id.machine_result_list);

        List<Equipment> equipments = DataHolder.getInstance().getEquipments();

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_layout, equipments);
        this.setAdapter(adapter);
        machineListView.setAdapter(adapter);
        machineListView.setTextFilterEnabled(true);

        machineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Equipment equipment = (Equipment) parent.getItemAtPosition(position);
                DataHolder.getInstance().setEquipment(equipment);

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
                String equipmentName = searchView.getQuery().toString();
                DataHolder.getInstance().getEquipment().setName(equipmentName);


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
