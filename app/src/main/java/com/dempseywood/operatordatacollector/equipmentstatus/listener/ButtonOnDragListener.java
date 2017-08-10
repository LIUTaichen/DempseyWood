package com.dempseywood.operatordatacollector.equipmentstatus.listener;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.database.DbHelper;
import com.dempseywood.operatordatacollector.database.dao.EquipmentStatusDAO;
import com.dempseywood.operatordatacollector.equipmentstatus.activity.EquipmentStatusActivity;
import com.dempseywood.operatordatacollector.rest.HttpRequestTask;
import com.dempseywood.operatordatacollector.rest.status.EquipmentStatus;
import com.dempseywood.operatordatacollector.scheduleitem.DataHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by musing on 31/07/2017.
 */

public class ButtonOnDragListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        Button button = (Button)v;
        //check if button receiving the event is the loaded button or the unloaded button
        boolean isLoadedButton = button.getText().equals( v.getContext().getResources().getString(R.string.string_loaded));
        switch(event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                if(event.getClipDescription().getLabel().equals(button.getText())){
                    return false;
                }else{
                    return true;
                }
            case DragEvent.ACTION_DROP:
                EquipmentStatusActivity activity = (EquipmentStatusActivity)v.getContext();
                //switch to unloaded status if the current button is loaded button, loaded status if the current button is unloaded button
                if(isLoadedButton){
                    activity.switchToLoaded();
                }
                else{
                    activity.switchToUnloaded();
                }
                EquipmentStatus status = DataHolder.getInstance().getEquipmentStatus();
                status.setEquipment(DataHolder.getInstance().getMachine().getPlateNo());
                status.setTimestamp( new Date());

                new HttpRequestTask(v.getContext(),status).execute();
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                if(isLoadedButton){
                    v.setBackgroundResource(R.drawable.entered_loaded);
                }
                else{
                    v.setBackgroundResource(R.drawable.entered_unloaded);
                }
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundResource(R.drawable.inactive);
            case DragEvent.ACTION_DRAG_ENDED:

        }
        return true;
    }


}
