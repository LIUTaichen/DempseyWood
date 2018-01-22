package com.dempseywood.operatordatacollector.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.TaskDao;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.Task;
import com.dempseywood.operatordatacollector.service.RequestService;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ChooseMaterialActivity extends AppCompatActivity {

    private ArrayAdapter<CharSequence> adapter;
    private View progressView;
    private TaskDao taskDao;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String tag = "ChooseMaterialActivity";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_material);

        DB.init(this);
        taskDao = DB.getInstance().taskDao();

        listView = (ListView)findViewById(R.id.materialList);
        progressView = findViewById(R.id.progress);

        adapter = new ArrayAdapter<CharSequence>(this,
                R.layout.task_list_item_layout, new ArrayList<CharSequence>());

        listView.setAdapter(adapter);

        //highlight current selected material
        Log.d("ChooseMaterialActivity",  "debug");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataHolder.getInstance().getEquipmentStatus().setTask(adapter.getItem(position).toString());
                ChooseMaterialActivity activity = (ChooseMaterialActivity) view.getContext();
                Intent intent = new Intent(activity, CountByTapActivity.class);
                activity.startActivity(intent);

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.choose_task_swipe_refresh_layout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(tag, "refreshing");
                updateTasks();
                //refreshMachines();
            }
        });


    }

    @Override
    protected void onResume() {
        readTasksFromDb();
        super.onResume();
    }

    private void readTasksFromDb() {
        new AsyncTask<Void, Void, List<CharSequence>>(){


            @Override
            protected List<CharSequence> doInBackground(Void... voids) {
                Log.d(tag, "reading tasks from db");
                List<Task> taskList =  taskDao.getAll();
                List<CharSequence> taskNameList = new ArrayList<>();
                for(Task task : taskList){
                    taskNameList.add(task.getName());
                }
                return taskNameList;
            }

            @Override
            protected void onPreExecute() {
                Log.d(tag, "pre");
                progressView.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(List<CharSequence> taskList){
                Log.d(tag, "post");
                progressView.setVisibility(View.GONE);
                Log.d(tag, "number of tasks found in db " + taskList.size());

                adapter.clear();
                adapter.addAll(taskList);
                adapter.notifyDataSetChanged();
                scrollToSelected(taskList);
            }

        }.execute();
    }

    private void scrollToSelected(List<CharSequence> taskList) {
        for(int i = 0; i < taskList.size(); i ++){
            if(taskList.get(i).equals(DataHolder.getInstance().getEquipmentStatus().getTask())){
                Log.d("ChooseMaterialActivity", DataHolder.getInstance().getEquipmentStatus().getTask() + "  matched");
                listView.setItemChecked(i, true);
                listView.smoothScrollToPositionFromTop(i, 0);
            }
        }
    }

    private void updateTasks() {

        RequestQueue queue = RequestService.getInstance(this).getRequestQueue();
        final String url = UrlHelper.getFetchTaskUrl();
        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Gson gson = new Gson();
                        Task[] tasks = gson.fromJson(response.toString(), Task[].class);
                        new AsyncTask<Task, Void, Boolean>() {

                            @Override
                            protected Boolean doInBackground(Task... task) {
                                taskDao.deleteAll();
                                taskDao.insertAll(task);
                                Log.d(tag, "tasks updated");
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Boolean aBoolean) {
                                Toast toast = Toast.makeText(ChooseMaterialActivity.this, R.string.message_tasks_updated,Toast.LENGTH_LONG);
                                toast.show();
                                mSwipeRefreshLayout.setRefreshing(false);
                                readTasksFromDb();
                                super.onPostExecute(aBoolean);

                            }
                        }.execute(tasks);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Snackbar snackbar = Snackbar.make(mSwipeRefreshLayout, R.string.message_tasks_update_failure, Snackbar.LENGTH_LONG );
                snackbar.show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        queue.add(arrayRequest);
    }

}
