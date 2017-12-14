package com.dempseywood.operatordatacollector.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.listeners.OnQueryTextListener;
import com.dempseywood.operatordatacollector.adapters.CustomSpinnerAdapter;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentDao;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ChooseMachineActivity extends AppCompatActivity {


    private EquipmentDao equipmentDao;



    private CustomSpinnerAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String tag = "ChooseMachineActivity";
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_machine);

        DB.init(getApplicationContext());
        equipmentDao =  DB.getInstance().equipmentDao();


        SearchView searchView = (SearchView)findViewById(R.id.machine_search);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.choose_machine_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(tag, "refreshing");
                refreshMachines();
            }
        });
         mListView = (ListView)findViewById(R.id.machine_result_list);

        List<Equipment> equipments = DataHolder.getInstance().getEquipments();

        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, R.layout.spinner_layout, equipments);
        this.setAdapter(adapter);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void refreshMachines() {


        RequestQueue queue = Volley.newRequestQueue(this);
        final String url = this.getString(R.string.web_service) + this.getString(R.string.api_equipment);
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Equipment[] equipments = gson.fromJson(response.toString(), Equipment[].class);
                        List<Equipment> equipmentList = new ArrayList<Equipment>();
                        for(int i = 0; i < equipments.length; i++){
                            equipmentList.add(equipments[i]);
                        }
                        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(ChooseMachineActivity.this, R.layout.spinner_layout, equipmentList);

                        mListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, R.string.message_plants_updated, Snackbar.LENGTH_LONG );
                        snackbar.show();
                        mSwipeRefreshLayout.setRefreshing(false);

                        AsyncTask task = new AsyncTask<Equipment, Void, Boolean>() {
                            @Override
                            protected Boolean doInBackground(Equipment... equipment) {
                                equipmentDao.deleteAll();
                                equipmentDao.insertAll(equipment);
                                Log.d(tag, "plants updated");
                                return null;
                            }

                        };
                        task.execute(equipments);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, R.string.message_plants_update_failure, Snackbar.LENGTH_LONG );
                snackbar.show();
            }
        });
        queue.add(arrayRequest);

    }

    public CustomSpinnerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CustomSpinnerAdapter adapter) {
        this.adapter = adapter;
    }
}
