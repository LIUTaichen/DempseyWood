package com.dempseywood.operatordatacollector.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.async.CreateEquipmentStatusTask;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.models.DataHolder;

import java.util.Date;
import java.util.List;

public class CountByTapActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    private EquipmentStatusDao equipmentStatusDao;
    private String tag = "CountByTap";

    private Button unloadedButton;
    private Button unloadedMaterialButton;
    private Button loadedButton;
    private Button loadedMaterialButton;
    private TextSwitcher countText;
    private AlertDialog alertDialog;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_by_tap);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,/* DrawerLayout object */
                myToolbar,
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);

        unloadedButton = (Button) findViewById(R.id.unloadedButton);
        unloadedMaterialButton = (Button) findViewById(R.id.unloadedMaterialButton);
        loadedButton = (Button) findViewById(R.id.loadedButton);
        loadedMaterialButton = (Button) findViewById(R.id.loadedMaterialButton);
        countText = (TextSwitcher) findViewById(R.id.textView);
        int count = countText.getChildCount();
        countText.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {

                // Create a new TextView
                TextView t = new TextView(CountByTapActivity.this);
                t.setTextSize(72f);
                return t;
            }
        });
        countText.setInAnimation(this, android.R.anim.fade_in);
        countText.setOutAnimation(this, android.R.anim.fade_out);
        DB.init(getApplicationContext());
        equipmentStatusDao = DB.getInstance().equipmentStatusDao();



        unloadedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity mainActivity =  CountByTapActivity.this;
                mainActivity.changeStatus("Loaded");
                mainActivity.changeViewToLoaded();
                String message = "Haul of " + DataHolder.getInstance().getEquipmentStatus().getTask() + " started.";
                showStickyAlert(message);


            }
        });

        loadedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity mainActivity = CountByTapActivity.this;
                mainActivity.changeStatus("Unloaded");
                mainActivity.incrementLoadCount();
                mainActivity.changeViewToUnloaded();
                String message = "Haul of " + DataHolder.getInstance().getEquipmentStatus().getTask() + " completed.";
                showStickyAlert(message);
            }
        });
        String currentTask = DataHolder.getInstance().getEquipmentStatus().getTask();
        if (currentTask == null || currentTask.isEmpty()) {
            currentTask = "Top soil";
            DataHolder.getInstance().getEquipmentStatus().setTask(currentTask);
        }
        unloadedMaterialButton.setText(currentTask);
        loadedMaterialButton.setText(currentTask);
        loadedMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(CountByTapActivity.this.findViewById(android.R.id.content),
                        getString(R.string.message_alert_unable_to_change_task),
                        Snackbar.LENGTH_LONG).show();
            }
        });
        unloadedMaterialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountByTapActivity activity =  CountByTapActivity.this;
                Intent intent = new Intent(activity, ChooseMaterialActivity.class);
                activity.startActivity(intent);
            }
        });
    }


    public void changeViewToLoaded() {
        unloadedMaterialButton.setVisibility(View.GONE);
        unloadedButton.setVisibility(View.GONE);
        loadedButton.setVisibility((View.VISIBLE));
        loadedMaterialButton.setVisibility((View.VISIBLE));

    }

    public void showAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        TextView title =  new TextView(this);
        title.setText(message);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(40);
        alertDialog.setCustomTitle(title);
        alertDialog.show();
    }

    public void showStickyAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(message);
        builder.setMessage(" This alert will be closed in 5 seconds");
        alertDialog = builder.create();
        TextView title =  new TextView(this);
        title.setText(message);
        title.setGravity(Gravity.CENTER);
        title.setTextSize(40);
        alertDialog.setCustomTitle(title);
        alertDialog.show();

        new CountDownTimer(5100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog.setMessage("This alert will be closed in " + (millisUntilFinished / 1000) + " seconds");
            }
            @Override
            public void onFinish() {
                dismissDialog();
            }
        }.start();
    }

    public void incrementLoadCount() {
        TextView currentView = (TextView)countText.getCurrentView();
        CharSequence currentCount = currentView.getText();
        Integer count = Integer.parseInt(currentCount.toString());
        Integer newCount = count + 1;
        countText.setText(newCount.toString());
    }

    public void changeViewToUnloaded() {
        unloadedMaterialButton.setVisibility(View.VISIBLE);
        unloadedButton.setVisibility(View.VISIBLE);
        loadedButton.setVisibility((View.GONE));
        loadedMaterialButton.setVisibility(View.GONE);

    }

    public void initializeViews() {

        AsyncTask asyncTask = new AsyncTask<Void, Void, List<EquipmentStatus>>(){
            CountByTapActivity activity = CountByTapActivity.this;

            @Override
            protected List<EquipmentStatus> doInBackground(Void... params) {
                List<EquipmentStatus> statusList = equipmentStatusDao.loadAllAfter(DateTimeHelper.getTimeOfStartOfDay(new Date()));
                Log.d(tag, "number of status found " + statusList.size());
                return statusList;
            }

            @Override
            protected void onPostExecute(List<EquipmentStatus> statusList) {

                activity.constructUIFromSavedStatus(statusList);

                super.onPostExecute(statusList);
            }
        }.execute();


    }

    public void constructUIFromSavedStatus(List<EquipmentStatus> statusList){
        int count = statusList.size() /2;
        countText.setText(count + "");
        if(statusList.isEmpty() || "Unloaded".equals(statusList.get(statusList.size() -1).getStatus())){
            changeViewToUnloaded();
        }else {
            changeViewToLoaded();
        }
    }



    private void changeStatus(String statusString) {
        DataHolder.getInstance().getEquipmentStatus().setStatus(statusString);
        new CreateEquipmentStatusTask(this).execute();
    }



    @Override
    protected void onDestroy() {
        dismissDialog();
        super.onDestroy();

    }
    private void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        initializeViews();
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_operator_details:
            case android.R.id.home :
                if(DataHolder.getInstance().getEquipmentStatus().getStatus().equals("Loaded")){
                    Snackbar.make(this.findViewById(android.R.id.content),
                            "Operator details can only be changed when equipment is unloaded",
                            Snackbar.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(this, OperatorDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intent);
                    finish();
                }
                break;
            case R.id.action_history:
                Intent intent = new Intent(this, HistoryActivity.class);
                this.startActivity(intent);
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
