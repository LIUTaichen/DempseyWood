package com.dempseywood.operatordatacollector.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.adapters.HistoryAdapter;
import com.dempseywood.operatordatacollector.adapters.TaskAdatpter;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.HaulDao;
import com.dempseywood.operatordatacollector.data.dao.TaskDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.helpers.UpdateTaskRequestBuilder;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.models.Haul;
import com.dempseywood.operatordatacollector.models.Task;
import com.dempseywood.operatordatacollector.models.UpdateTaskRequest;
import com.dempseywood.operatordatacollector.rest.ChangeHaulTaskCommand;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements AsyncActionObserver {
    private HaulDao haulDao;
    private TaskDao taskDao;

    private RecyclerView mRecyclerView;
    private HistoryAdapter historyAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String tag = "History";
    private LinearLayout llBottomSheet;
    private LinearLayout changeButtonLayout;
    private ListView bottomSheetTaskListView;
    private TaskAdatpter taskAdatpter;
    private View progressBar;

    // init the bottom sheet behavior
    private BottomSheetBehavior bottomSheetBehavior;
    private ChangeHaulTaskCommand changeHaulTaskCommand;

    private List<Haul> haulsToBeChanged = new ArrayList<>();
    private Task newTask;

// change the state of the bottom sheet


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
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
        historyAdapter = new HistoryAdapter(new ArrayList<Haul>(), this);
        mRecyclerView.setAdapter(historyAdapter);
        llBottomSheet = (LinearLayout) findViewById(R.id.hitstory_edit_bottome_sheet_layout);
        bottomSheetTaskListView = (ListView)findViewById(R.id.taskList);
        progressBar=(View)findViewById(R.id.toolbar_progress_bar_layout);
        progressBar.setVisibility(View.INVISIBLE);
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
                SparseBooleanArray selected = historyAdapter.getSelectionArray();
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
            }
        });
        loadStateFromDatabase();
        loadTasksFromDatabase();


    }

    public void loadStateFromDatabase(){
        progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.INVISIBLE);
                super.onPostExecute(haulList);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void acceptData(List<Haul> haulList){
        historyAdapter = new HistoryAdapter(haulList, this);
        mRecyclerView.swapAdapter(historyAdapter, false);
        Log.d(tag, "new data added");

        Log.i(tag, bottomSheetBehavior.getState() +"");
        super.onResume();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN) ;
        Log.i(tag, bottomSheetBehavior.getState() +"");
    }

    public void loadTasksFromDatabase(){
        progressBar.setVisibility(View.VISIBLE);
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
                progressBar.setVisibility(View.INVISIBLE);
                super.onPostExecute(taskList);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void acceptTasks(List<Task> taskList) {
        if(taskAdatpter == null){
            taskAdatpter = new TaskAdatpter(this,R.layout.bottom_sheet_task_list_item_layout,R.id.taskName, taskList);
            bottomSheetTaskListView.setAdapter(taskAdatpter);
            bottomSheetTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    newTask = taskAdatpter.getItem(position);
                    Log.d(tag, "selected task :  " + newTask.toString());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(HistoryActivity.this);
                    builder.setTitle("Change the task?");
                    builder.setMessage("A request will be sent to the server to update the load history. The task of the selected loads will be changed.");
                    builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            haulsToBeChanged = historyAdapter.getSelectedHauls();
                            sendUpdateRequest(haulsToBeChanged, newTask);
                            historyAdapter.stopEditing();
                            invalidateOptionsMenu();
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        }
                    });//second parameter used for onclicklistener
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
                    builder.show();
                }
            });
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
            this.historyAdapter.startEditing();
            Log.i(tag, bottomSheetBehavior.getState() +"");
            invalidateOptionsMenu();
        }
        if(id == R.id.cancel_action){
            this.historyAdapter.stopEditing();
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        if(!isTaskRunning()) {
            if (this.historyAdapter.isEditing() || isTaskRunning()) {
                getMenuInflater().inflate(R.menu.history_menu_editing, menu);
            } else {
                getMenuInflater().inflate(R.menu.history_menu, menu);
            }
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

    public void sendUpdateRequest(List<Haul> hauls, Task newTask){
        changeHaulTaskCommand = new ChangeHaulTaskCommand(this.getApplicationContext(), this, hauls, newTask);
        changeHaulTaskCommand.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onPreStart() {
        progressBar.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
    }

    @Override
    public void onComplete() {
        progressBar.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mRecyclerView,"Hauls updated." , Snackbar.LENGTH_LONG );
        snackbar.show();
        invalidateOptionsMenu();
        loadStateFromDatabase();
    }

    @Override
    public void onError() {
        progressBar.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mRecyclerView,"Update failed." , Snackbar.LENGTH_LONG )
                .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendUpdateRequest(haulsToBeChanged, newTask);
                            }
                        });
        snackbar.show();
        invalidateOptionsMenu();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(changeHaulTaskCommand != null){
            changeHaulTaskCommand.cancel(true);
        }
    }

    private boolean isTaskRunning(){
        if(changeHaulTaskCommand == null || changeHaulTaskCommand.getStatus() == AsyncTask.Status.FINISHED){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onTaskCancelled(){
        progressBar.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mRecyclerView,"Request is cancelled" , Snackbar.LENGTH_SHORT );
        snackbar.show();
    }
}
