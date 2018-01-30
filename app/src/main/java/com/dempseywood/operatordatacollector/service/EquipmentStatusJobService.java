package com.dempseywood.operatordatacollector.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

import com.dempseywood.operatordatacollector.rest.SynchronizeWithServerTask;

/**
 * Created by musing on 10/08/2017.
 */

public class EquipmentStatusJobService extends JobService {

    public static final Integer EquipmentStatusJobId = 23;

    private SynchronizeWithServerTask task;
    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.i("EquipmentStatusJob", "starting job");
        task =  new SynchronizeWithServerTask(this.getApplicationContext()){
            @Override
            protected void onPostExecute(Boolean success){
                if(success) {
                    Log.i("EquipmentStatusJob", "task finished");
                }else{
                    Log.i("EquipmentStatusJob", "task failed, rescheduling  job");
                }
                jobFinished(params, !success);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("EquipmentStatusJob", "onStopJob");
        if(task != null){
            Log.i("EquipmentStatusJob", "cancelling job");
            task.cancel(true);
        }
        return true;
    }
}
