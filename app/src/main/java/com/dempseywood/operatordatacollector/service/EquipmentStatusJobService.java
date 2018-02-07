package com.dempseywood.operatordatacollector.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

import com.dempseywood.operatordatacollector.async.SynchronizeWithServerTask;

/**
 * Created by musing on 10/08/2017.
 */

public class EquipmentStatusJobService extends JobService {

    public static final Integer EquipmentStatusJobId = 23;
    private static final String tag = "EquipmentStatusJob";

    private SynchronizeWithServerTask task;

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.i(tag, "starting job");
        task =  new SynchronizeWithServerTask(this.getApplicationContext()){
            @Override
            protected void onPostExecute(Boolean success){
                if(success) {
                    Log.i(tag, "task finished");
                }else{
                    Log.i(tag, "task failed, rescheduling  job");
                }
                jobFinished(params, !success);
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i(tag, "onStopJob");
        if(task != null){
            Log.i(tag, "cancelling job");
            task.cancel(true);
        }
        return true;
    }


}
