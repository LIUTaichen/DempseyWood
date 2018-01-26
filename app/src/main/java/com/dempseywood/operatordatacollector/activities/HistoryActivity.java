package com.dempseywood.operatordatacollector.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.adapters.HistoryAdapter;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.data.dao.HaulDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.models.Haul;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private HaulDao haulDao;

    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
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
        haulDao = DB.getInstance().haulDao();

        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HistoryAdapter(new ArrayList<Haul>());
        mRecyclerView.setAdapter(mAdapter);
        loadStateFromDatabase();
        // specify an adapter (see also next example)


    }

    public void loadStateFromDatabase(){
        new AsyncTask<Void, Void, List<Haul>>(){
            public HistoryActivity activity = HistoryActivity.this;

            @Override
            protected List<Haul> doInBackground(Void... params) {
                List<Haul> haulList = haulDao.loadAllAfter(DateTimeHelper.getTimeOfStartOfDay(new Date()));
                Log.d(tag, "number of status found " + haulList.size());
                return haulList;
            }

            @Override
            protected void onPostExecute(List<Haul> haulList) {

                activity.acceptData(haulList);

                super.onPostExecute(haulList);
            }
        }.execute();

    }

    public void acceptData(List<Haul> haulList){
        mAdapter = new HistoryAdapter(haulList);
        mRecyclerView.swapAdapter(mAdapter, false);
        Log.d(tag, "new data added");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_history) {
            this.mAdapter.startEditing();
            invalidateOptionsMenu();
        }

        if(id == R.id.cancel_action){
            this.mAdapter.stopEditing();
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(this.mAdapter.isEditing()){
            getMenuInflater().inflate(R.menu.history_menu_editing, menu);
        }else{
            getMenuInflater().inflate(R.menu.history_menu, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}
