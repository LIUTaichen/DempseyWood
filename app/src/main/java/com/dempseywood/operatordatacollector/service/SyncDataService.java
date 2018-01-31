package com.dempseywood.operatordatacollector.service;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Jason.Liu on 31/01/2018.
 */

public class SyncDataService {
    public static final int INITIAL_BACKOFF_MILLIS = 30000;
    private static SyncDataService instance;
    private Context context;
    private static final String tag = "SyncDataService";

    private SyncDataService(Context context) {
        this.context = context;

    }

    public static SyncDataService getInstance(Context context) {
        if(instance == null) {
            synchronized(RequestService.class) {
                if(instance == null) {
                    instance = new SyncDataService(context);
                }
            }
        }
        return instance;
    }

    public void scheduleSyncJob(){
        Log.i(tag, "trying to schedule new job to sync data to server");

        JobScheduler jobScheduler =
                (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        List<JobInfo> scheduledJobs = jobScheduler.getAllPendingJobs();
        Iterator<JobInfo> iterator = scheduledJobs.iterator();
        boolean isJobAlreadyScheduled = false;
        while(iterator.hasNext()){
            JobInfo info = iterator.next();
            if(info.getId() == EquipmentStatusJobService.EquipmentStatusJobId){
                Log.i(tag, "found scheduled sync job.");
                isJobAlreadyScheduled = true;
            }
        }

        if(isJobAlreadyScheduled){
            Log.i(tag, "no need to schedule job again.");
            return;
        }
        Log.i(tag, "no scheduled sync job found. Scheduling a new one");

        jobScheduler.schedule(new JobInfo.Builder(EquipmentStatusJobService.EquipmentStatusJobId,
                new ComponentName(context, EquipmentStatusJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setBackoffCriteria(INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_EXPONENTIAL)
                .build());

    }

}
