package com.dempseywood.operatordatacollector.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.adapters.HistoryAdapter;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private EquipmentStatusDao equipmentStatusDao;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String tag = "History";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.label_history_appbar_title);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        String a = getSupportActionBar().getTitle().toString();

        DB.init(getApplicationContext());
        equipmentStatusDao = DB.getInstance().equipmentStatusDao();

        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HistoryAdapter(new ArrayList<EquipmentStatus>());
        mRecyclerView.setAdapter(mAdapter);
        loadStateFromDatabase();
        // specify an adapter (see also next example)


    }

    public void loadStateFromDatabase(){
        AsyncTask asyncTask = new AsyncTask<Void, Void, List<EquipmentStatus>>(){
            public HistoryActivity activity = HistoryActivity.this;

            @Override
            protected List<EquipmentStatus> doInBackground(Void... params) {
                List<EquipmentStatus> statusList = equipmentStatusDao.loadAllAfter(DateTimeHelper.getTimeOfStartOfDay(new Date()));
                Log.d(tag, "number of status found " + statusList.size());
                return statusList;
            }

            @Override
            protected void onPostExecute(List<EquipmentStatus> statusList) {

                activity.acceptData(statusList);

                super.onPostExecute(statusList);
            }
        }.execute();

    }

    public void acceptData(List<EquipmentStatus> statusList){
        mAdapter = new HistoryAdapter(statusList);
        mRecyclerView.swapAdapter(mAdapter, false);
        Log.d(tag, "new data added");
    }

}
