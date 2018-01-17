package com.dempseywood.operatordatacollector.async;

import android.app.Activity;
import android.os.AsyncTask;

import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.EquipmentStatusDao;
import com.dempseywood.operatordatacollector.helpers.DateTimeHelper;
import com.dempseywood.operatordatacollector.models.DataHolder;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.rest.SynchronizeWithServerTask;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Jason.Liu on 19/12/2017.
 */

public class CreateEquipmentStatusTask extends AsyncTask<Void, Void, EquipmentStatus> {
    private Activity activity;
    private EquipmentStatusDao equipmentStatusDAO;

    public CreateEquipmentStatusTask(Activity activity){
        this.activity = activity;
        DB.init(activity.getApplicationContext());
        equipmentStatusDAO = DB.getInstance().equipmentStatusDao();
    }

    @Override
    protected void onPostExecute(EquipmentStatus equipmentStatus) {
        new SynchronizeWithServerTask(activity.getApplicationContext()).execute();

        super.onPostExecute(equipmentStatus);
    }

    @Override
    protected EquipmentStatus doInBackground(Void... voids) {
        EquipmentStatus latestStatus = equipmentStatusDAO.getLastStatusAfter(DateTimeHelper.getTimeOfStartOfDay(new Date()));
        EquipmentStatus newStatus = getEquipmentStatus();
        if(latestStatus != null && "Loaded".equals(latestStatus.getStatus())){
            newStatus.setEquipment(latestStatus.getEquipment());
            newStatus.setOperator(latestStatus.getOperator());
            newStatus.setTask(latestStatus.getTask());
        }
        equipmentStatusDAO.insertAll(newStatus);
        return newStatus;
    }

    private EquipmentStatus getEquipmentStatus() {
        EquipmentStatus equipmentStatus = new EquipmentStatus();
        equipmentStatus.setStatus(DataHolder.getInstance().getEquipmentStatus().getStatus());
        equipmentStatus.setTask(DataHolder.getInstance().getEquipmentStatus().getTask());
        equipmentStatus.setTimestamp(new Date());
        equipmentStatus.setOperator(DataHolder.getInstance().getEquipmentStatus().getOperator());
        equipmentStatus.setEquipment(DataHolder.getInstance().getEquipment().getName());
        equipmentStatus.setImei(DataHolder.getInstance().getEquipmentStatus().getImei());
        equipmentStatus.setLatitude(DataHolder.getInstance().getEquipmentStatus().getLatitude());
        equipmentStatus.setLongitude(DataHolder.getInstance().getEquipmentStatus().getLongitude());
        equipmentStatus.setIsSent(false);
        equipmentStatus.setUuid(UUID.randomUUID().toString());
        return equipmentStatus;

    }
}
