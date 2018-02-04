package com.dempseywood.operatordatacollector.activities;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.adapters.HistoryAdapter;
import com.dempseywood.operatordatacollector.adapters.TaskAdatpter;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.data.dao.HaulDao;
import com.dempseywood.operatordatacollector.data.dao.TaskDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.Equipment;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.models.Haul;
import com.dempseywood.operatordatacollector.models.Task;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private HaulDao haulDao;
    private TaskDao taskDao;

    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String tag = "History";
    private LinearLayout llBottomSheet;
    private LinearLayout changeButtonLayout;
    private ListView bottomSheetTaskListView;
    private TaskAdatpter taskAdatpter;

    // init the bottom sheet behavior
    private BottomSheetBehavior bottomSheetBehavior;

// change the state of the bottom sheet


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
        taskDao = DB.getInstance().taskDao();

        mRecyclerView = (RecyclerView) findViewById(R.id.history_recycler);
        changeButtonLayout = (LinearLayout)findViewById(R.id.history_edit_bottom_layout) ;
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new HistoryAdapter(new ArrayList<Haul>(), this);
        mRecyclerView.setAdapter(mAdapter);
        llBottomSheet = (LinearLayout) findViewById(R.id.hitstory_edit_bottome_sheet_layout);
        bottomSheetTaskListView = (ListView)findViewById(R.id.taskList);

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.i(tag, "new state is "  + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        changeButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SparseBooleanArray selected = mAdapter.getSelectionArray();
                StringBuilder selectedIndex= new StringBuilder();
                Log.d(tag, "size:" +  selected.size());
                for (int i = 0; i <selected.size(); i ++){
                    int key = selected.keyAt(i);
                    if(selected.get(key)){
                        selectedIndex.append(" ");
                        selectedIndex.append(key);
                    }
                }
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                /*Snackbar snackbar = Snackbar.make(changeButtonLayout, "selected : " + selectedIndex.toString(), Snackbar.LENGTH_LONG );
                snackbar.show();*/
            }
        });
        loadStateFromDatabase();
        loadTasksFromDatabase();


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
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void acceptData(List<Haul> haulList){
        mAdapter = new HistoryAdapter(haulList, this);
        mRecyclerView.swapAdapter(mAdapter, false);
        Log.d(tag, "new data added");

        Log.i(tag, bottomSheetBehavior.getState() +"");
        super.onResume();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) ;
        Log.i(tag, bottomSheetBehavior.getState() +"");
    }

    public void loadTasksFromDatabase(){
        new AsyncTask<Void, Void, List<Task>>(){
            public HistoryActivity activity = HistoryActivity.this;

            @Override
            protected List<Task> doInBackground(Void... params) {
                List<Task> taskList = taskDao.getAll();
                Log.d(tag, "number of tasks found " + taskList.size());
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> taskList) {

                activity.acceptTasks(taskList);

                super.onPostExecute(taskList);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void acceptTasks(List<Task> taskList) {
        if(taskAdatpter == null){
            taskAdatpter = new TaskAdatpter(this,R.layout.bottom_sheet_task_list_item_layout,R.id.taskName, taskList);
            bottomSheetTaskListView.setAdapter(taskAdatpter);
        }
        else{
            taskAdatpter.clear();
            taskAdatpter.addAll(taskList);
            taskAdatpter.notifyDataSetChanged();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit_history) {
            this.mAdapter.startEditing();
            Log.i(tag, bottomSheetBehavior.getState() +"");
          /*  bottomSheetBehavior.getState();
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }*/
            Log.i(tag, bottomSheetBehavior.getState() +"");
            invalidateOptionsMenu();
        }

        if(id == R.id.cancel_action){
            this.mAdapter.stopEditing();
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
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


    @Override
    protected void onResume() {
        Log.i(tag, bottomSheetBehavior.getState() +"");
        super.onResume();

         bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) ;
        Log.i(tag, bottomSheetBehavior.getState() +"");


    }

    public void showChangeTaskButton(boolean needToShow){
        if(needToShow){
            changeButtonLayout.setVisibility(View.VISIBLE);
        }else{
            changeButtonLayout.setVisibility(View.GONE);
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }
    }


}
