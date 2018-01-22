package com.dempseywood.operatordatacollector.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.listeners.OnQueryTextListener;
import com.dempseywood.operatordatacollector.adapters.CustomSpinnerAdapter;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentDao;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.service.RequestService;
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
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_machine);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DB.init(getApplicationContext());
        equipmentDao =  DB.getInstance().equipmentDao();



        searchView = (SearchView)findViewById(R.id.machine_search);
        searchView.setIconifiedByDefault(false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.choose_machine_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(tag, "refreshing");
                refreshMachines();
            }
        });
         mListView = (ListView)findViewById(R.id.machine_result_list);

        adapter = new CustomSpinnerAdapter(this, R.layout.equipment_list_item_layout, new ArrayList<Equipment>());
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

        searchView.setOnQueryTextListener(new OnQueryTextListener(this)) ;


        getMachines();

    }

    private void refreshMachines() {


        final String url = UrlHelper.getFetchEquipmentUrl();
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
                        adapter = new CustomSpinnerAdapter(ChooseMachineActivity.this, R.layout.equipment_list_item_layout, equipmentList);

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

                        }.execute(equipments);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, R.string.message_plants_update_failure, Snackbar.LENGTH_LONG );
                snackbar.show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        RequestService.getInstance(this).addToRequestQueue(arrayRequest);

    }

    public CustomSpinnerAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(CustomSpinnerAdapter adapter) {
        this.adapter = adapter;
    }

    public void getMachines(){
        AsyncTask task = new AsyncTask<Void, Void, List<Equipment>>() {
            @Override
            protected void onPostExecute(List<Equipment> equipments) {
                adapter = new CustomSpinnerAdapter(ChooseMachineActivity.this, R.layout.equipment_list_item_layout, equipments);
                mListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            protected List<Equipment> doInBackground(Void... params) {
                Log.i(tag, "doInBackground");
                return equipmentDao.getAll();
            }

        }.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
                

        }
        return super.onOptionsItemSelected(item);
    }
}
